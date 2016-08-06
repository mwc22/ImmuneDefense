package view;

import interfaces.GUIMessage;
import interfaces.MainServerMessage;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mainmessages.MainRequestLeaveGameMessage;
import model.GameGuts;
import model.MultiPlayer2;
import model.Player;
import server.MainServer;
import server.PlayerSocket;


public class ImmuneResponse extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5999048388066329973L;

	public static Player player;

	private JPanel currentView;
	public static PlayerSocket connection;
	

	/**
	 * Dat game bro.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame game = new ImmuneResponse();
		game.setVisible(true);
	}

	public ImmuneResponse() {
		this.setTitle("Immune Defense");
		this.setResizable(false);
		setupView();
		repaint();
	}

	public void connect() throws Exception {
		connection = new PlayerSocket(new Socket(MainServer.DOMAIN, MainServer.PORT_NUMBER));
		Thread t = new Thread(new ConnectionHandler());
		t.start();
		
//		GUIMessage guim = readMessage();
//		System.out.println("Receiving message: " + guim);
//		guim.execute(this);
	}
	
	private class ConnectionHandler implements Runnable {

		private boolean running = true;
		
		@Override
		public void run() {
			while(running) {
			try {
				GUIMessage guim = readMessage();
				System.out.println("Receiving message: " + guim);
				guim.execute(ImmuneResponse.this);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			
		}
		
	}

	public void sendMessage(MainServerMessage msm) throws Exception {
		System.out.println("Sending message: " + msm);
		connection.writeObject(msm);
//		GUIMessage guim = readMessage();
//		System.out.println("Receiving message: " + guim);
//		guim.execute(this);
	}

	public GUIMessage readMessage() throws ClassNotFoundException, IOException {
		System.out.println("ImmuneResponse trying to read");
		return (GUIMessage) connection.readObject();
	}
	
	
	public void addMessage(String message) {
		GameLobbyView.addMessage(message);
	}
	
	
	public void addPlayerToLobby(Player player, int team) {
		GameLobbyView.addPlayer(player, team);
		GameLobbyView.addMessage(player.getName() + " has joined the lobby.");
		updateCurrentView();
	}
	public void addPlayersToLobby(Vector<Player> players, int team) {
		for(Player player : players) {
			GameLobbyView.addPlayer(player, team);
		}
		updateCurrentView();
	}
	
	public void updateGameSearch(Vector<GameGuts> gg) {
		GameSearch.opengames = gg;
		GameSearch.rebuildLabels(this);
		updateCurrentView();
	}
	
	public void removePlayerFromLobby(Player player) {
		GameLobbyView.removePlayer(player);
		updateCurrentView();
	}
	
	public void playerSwitchedTeams(Player player, int team) {
		GameLobbyView.switchTeams(player, team);
		updateCurrentView();
	}
	public void updateCurrentView() {
		currentView.repaint();
		currentView.validate();
	}
	
	public void startMultiplayerGame(GameGuts gg, int teamId) {
		//TODO: Fancy pre-start stuff
		try {
			showPanel(new GameView(this, new MultiPlayer2(connection, gg.getGame(), teamId)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setupView() {
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		WindowClosingListener windowClose = new WindowClosingListener();
		this.addWindowListener(windowClose);
		this.setSize(600, 200);
		this.setLocation(400, 200);
		currentView = new DefaultView(this);
		this.add(currentView);
	}

	public void showPanel(JPanel toShow) {
		this.remove(currentView);
		currentView = toShow;
		this.add(currentView);
		this.revalidate();
//		this.repaint();
	}
	
	private class WindowClosingListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			if(connection != null && GameLobbyView.game != null) {
				try {
					sendMessage(new MainRequestLeaveGameMessage(player, GameLobbyView.game));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
//					e1.printStackTrace();
				}
			}
			System.exit(0);
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
