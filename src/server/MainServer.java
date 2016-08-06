package server;

import interfaces.GUIMessage;
import interfaces.MainServerMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

import mainmessages.MainEndGameMessage;
import mainmessages.MainIDMessage;
import mainmessages.MainPlayerLeftMessage;
import model.GameGuts;
import model.Player;

/**
 * This server must be able to keep track of all the game lobbies that are
 * currently open and then set up the game for them.
 * 
 * @author Patrick
 * 
 */
public class MainServer {

	private ServerSocket server;
	public static Vector<GameGuts> openGames = new Vector<GameGuts>();
	private Vector<PlayerCommunicator> players = new Vector<PlayerCommunicator>();
	private static HashMap<Integer, GameLobbyServer> gameLobbyHash = new HashMap<Integer, GameLobbyServer>();
	private static HashMap<Integer, PlayerCommunicator> playersHash = new HashMap<Integer, PlayerCommunicator>();
	private static Vector<Server> runningGames = new Vector<Server>();
	private int playerID = 1;
	private static int gameID = 1;
	private Vector<Thread> clientThreads = new Vector<Thread>();

	public static int PORT_NUMBER = 4000;
	public static String DOMAIN = "localhost";

	public static void main(String[] args) {
		new MainServer();
	}

	public MainServer() {
		try {
			server = new ServerSocket(PORT_NUMBER);
			LoginRunner loginThread = new LoginRunner(server);
			Thread login = new Thread(loginThread);
			login.start(); //start accepting users
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void removePlayer(PlayerCommunicator pc) {
		System.out.println("Player left");
		players.remove(pc);
		playersHash.remove(pc.ID);
		for (int i = 0; i < openGames.size(); i++) {
			openGames.get(i).removePlayer(pc.ID);
		}
	}

	public static synchronized boolean addPlayerToGame(Player player,
			GameGuts gl) {
		int glID = gl.ID;
		GameLobbyServer lobby = gameLobbyHash.get(glID);
		if (lobby == null)
			return false;
		int plID = player.ID;
		PlayerSocket ps = playersHash.get(plID).getPlayerSocket();
		return lobby.add(ps, player, plID);
	}

	public static synchronized GameGuts addGame(GameGuts gl, Player player) {
		while (gameLobbyHash.containsKey(gameID))
			gameID++;
		gl.ID = gameID;
		openGames.add(gl);
		GameLobbyServer glv = new GameLobbyServer(gl);
		gameLobbyHash.put(gl.ID, glv);
		int plID = player.ID;
		PlayerSocket ps = playersHash.get(plID).getPlayerSocket();
		glv.add(ps, player, plID);
		return gl;
	}

	public static synchronized void startGame(GameGuts gl) {
		int glID = gl.ID;
		GameLobbyServer lobby = gameLobbyHash.remove(glID);
		openGames.remove(lobby.getGameLabel());
		Vector<Integer> plIDs = lobby.getPlayerIDs();
		for(Integer ID : plIDs) {
			PlayerCommunicator pc = playersHash.get(ID);
			pc.stop();
		}
		Server server = lobby.startGame();
		runningGames.add(server);
	}
	
	public static synchronized void endGame(Server server) {
		runningGames.remove(server);
		for(Integer ID : server.getPlayers()) {
			PlayerCommunicator pc = playersHash.get(ID);
			pc.start();
			try {
				pc.sendMessage(new MainEndGameMessage());
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static Vector<Vector<Player>> getPlayersByTeam(GameGuts gg) {
		GameLobbyServer gls = getLobby(gg);
		return gls.getPlayersByTeam();
	}
	
	public static void sendMessage(GUIMessage message, GameGuts gl) {
		int glID = gl.ID;
		GameLobbyServer lobby = gameLobbyHash.get(glID);
		lobby.sendMessage(message);
	}
	
	public static boolean switchTeam(GameGuts game, Player toMove) {
		GameLobbyServer gls = getLobby(game);
		PlayerCommunicator pc = getPlayer(toMove);
		PlayerSocket ps = pc.getPlayerSocket();
		return gls.switchTeam(toMove, ps);
	}
	
	public static void removeGame(GameGuts gg) {
		openGames.remove(gg);
	}
	
	public static void removeFromGame(Player player, GameGuts game) {
		GameLobbyServer gls = getLobby(game);
		PlayerCommunicator pc = getPlayer(player);
		PlayerSocket ps = pc.getPlayerSocket();
		if(gls.removePlayer(player, ps))
			gls.sendMessage(new MainPlayerLeftMessage(player));
		
	}
	
	public static GameLobbyServer getLobby(GameGuts game) {
		return gameLobbyHash.get(game.ID);
	}
	
	public static PlayerCommunicator getPlayer(Player player) {
		return playersHash.get(player.ID);
	}

	/**
	 * 
	 * Connects sockets to the server and creates a thread dedicated to
	 * listening to it.
	 * 
	 */
	private class LoginRunner implements Runnable {
		private ServerSocket server;

		public LoginRunner(ServerSocket server) {
			this.server = server;
		}

		public void run() {
			while (true) {
				try {
					Socket temp = server.accept();
					while (playersHash.containsKey(playerID))
						playerID++;
					PlayerCommunicator pc = new PlayerCommunicator(
							new PlayerSocket(temp), playerID);
					playersHash.put(playerID, pc);
					players.add(pc);
					Thread t = new Thread(pc);
					clientThreads.add(t);
					t.start();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
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
		private int ID;
		private boolean running = true;

		public PlayerCommunicator(PlayerSocket clientSocket, int ID) {

			this.ID = ID;
			player = clientSocket;
			try {
				sendMessage(new MainIDMessage(ID));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public PlayerSocket getPlayerSocket() {
			return player;
		}
		
		public void stop() {
			running = false;
		}
		
		public void start() {
			running = true;
			run();
		}

		@Override
		public void run() {
			// infinite loop to process ServerMessages from client
			while(running) {
				// wait for message
				try {
					// receive message and send to clients
					System.out.println("MainServer trying to read");
					MainServerMessage msm = (MainServerMessage) player
							.readObject();
					GUIMessage guim = msm.create();
					System.out.println("Receiving message: " + msm);
					System.out.println("Sending message: " + guim);
					System.out.println("There are " + gameLobbyHash.size() + " games available");
					sendMessage(guim);

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					removePlayer(this);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		private void sendMessage(Object obj) throws IOException,
				InterruptedException {
			player.writeObject(obj);
		}
		


	}
}
