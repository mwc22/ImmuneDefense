package server;

//imports
import interfaces.ServerMessage;
import interfaces.ClientMessage;
import model.Tower;
import interfaces.TowerDefenseModel;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * Constructs the Server that will link multiple players when playing
 * multiplayer
 * 
 * @author Bhavana Gorti
 * @author Travis Woodrow
 * @author Michael Cutis
 * @author Patrick Martin
 * 
 */
public class Server implements Runnable {

	// vars
	private TowerDefenseModel theMainGame; //TODO: decide if we really need a version of the game on the server
	private ServerSocket ss;
	private Vector<PlayerCommunicator> players;
	private Vector<Integer> playerIDs;
	private Vector<Thread> clientThreads;
	private int numberOfPlayers;

	/**
	 * Constructor
	 * 
	 * @param numPlayers
	 *            the number of players this server must handle.
	 */
	public Server(int socketNumber, int numPlayers) {
		try {
			ss = new ServerSocket(socketNumber);
			numberOfPlayers = numPlayers;
			players = new Vector<PlayerCommunicator>();
			clientThreads = new Vector<Thread>();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Server(Vector<Vector<PlayerSocket>> players,
			Vector<Integer> playerIDs) {
		this.players = new Vector<PlayerCommunicator>();
		this.clientThreads = new Vector<Thread>();
		this.playerIDs = playerIDs;
		numberOfPlayers = players.size();
		for (int i = 0; i < players.size(); i++) {
			Vector<PlayerSocket> team = players.get(i);
			for (PlayerSocket ps : team) {
				PlayerCommunicator pc = new PlayerCommunicator(ps, i);
				Thread t = new Thread(pc);
				this.players.add(pc);
				clientThreads.add(t);
				t.start();
			}
		}
	}

	/**
	 * Accepts new clients
	 */
	@Override
	public void run() {
		// infinite loop accepting clients
		//TODO: is this the best way?
		for (;;) {
			try {
				// blocks here until client attempts to connect
				Socket temp = ss.accept();
				PlayerCommunicator pc = new PlayerCommunicator(
						new PlayerSocket(temp));
				players.add(pc);
				Thread t = new Thread(pc);
				clientThreads.add(t);
				t.start();

				if (players.size() == numberOfPlayers) {
					//TODO: Send start message and begin game
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void endGame() {
		MainServer.endGame(this);
	}

	public Vector<Integer> getPlayers() {
		return playerIDs;
	}

	/**
	 * Class to encapsulate communication between server and clients.
	 * 
	 * @author Travis
	 * 
	 */
	private class PlayerCommunicator implements Runnable {

		// vars
		private PlayerSocket player;
		//		private String userName;
		private int teamId;

		public PlayerCommunicator(PlayerSocket clientSocket) {
			player = clientSocket;
		}

		public PlayerCommunicator(PlayerSocket clientSocket, int ID) {
			this(clientSocket);
			this.teamId = ID;
		}

		@Override
		public void run() {
			// infinite loop to process ServerMessages from client
			for (;;) {
				// wait for message
				try {
					// receive message and send to clients
					System.out.println("Server trying to read");
					ServerMessage sm = (ServerMessage) player.readObject();
					ClientMessage cm = sm.create(Server.this);
					System.out.println("Receiving message: " + sm);
					System.out.println("Sending message: " + cm);
					updateClients(cm);

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					MainServer.endGame(Server.this);
					break;
					//					System.exit(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		private void sendMessage(Object obj) throws IOException,
				InterruptedException {
			System.out.println("Server sending message: " + obj);
			player.writeObject(obj);
		}

		/**
		 * 
		 * @param cm
		 *            object containing information for the update
		 * @throws Exception
		 */
		void updateClients(ClientMessage cm) throws Exception {
			switch (cm.getUpdateScheme()) {
			case All:
				for (int z = 0; z < players.size(); z++) {
					System.out.println(z);
					PlayerCommunicator pc = players.get(z);
					pc.sendMessage(cm);
					System.out.println(z);
				}
				break;

			case Else:
				for (int z = 0; z < players.size(); z++) {
					PlayerCommunicator pc = players.get(z);
					if (z != cm.getSendersId()) {
						pc.sendMessage(cm);
						break;
					}
				}
				break;
				
			case OpposingTeam:
				for (int z = 0; z < players.size(); z++) {
					System.out.println("Opposing");
					PlayerCommunicator pc = players.get(z);
					if (pc.teamId != cm.getSendersId()) {
						pc.sendMessage(cm);
					}
				}
				break;
				
			case YourTeam:
				for (int z = 0; z < players.size(); z++) {
					System.out.println("Your");
					PlayerCommunicator pc = players.get(z);
					if (pc.teamId == cm.getSendersId()) {
						pc.sendMessage(cm);
					}
				}
				break;
			case Self:
				PlayerCommunicator pc = players.get(cm.getSendersId());
				pc.sendMessage(cm);
				break;

			default:
				throw new Exception(
						"The ClientMessage did not have a valid UpdateScheme.");
			}
		}
	}
}
