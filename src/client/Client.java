package client;

// imports
import interfaces.ClientMessage;
import interfaces.ServerMessage;
import interfaces.TowerDefenseModel;

import java.io.IOException;
import java.net.Socket;

import server.PlayerSocket;

/**
 * Encapsulates commununication to the server
 * 
 * @author Travis
 * 
 */
public class Client implements Runnable {

	// vars
	PlayerSocket ps;
	private TowerDefenseModel theGame;
	private Thread runner;

	public Client(int portNumber, String playerName, int teamId, TowerDefenseModel game) {

		// reference to the game
		this.theGame = game;

		try {
			Socket theSocket = new Socket("localhost", portNumber);
			ps = new PlayerSocket(theSocket);
			ps.writeObject(playerName);
			ps.writeObject(teamId);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public Client(PlayerSocket ps, TowerDefenseModel game) {
		this.theGame = game;
		this.ps = ps;
	}

	/**
	 * Sends a ServerMessage to be processed. This will affect all other players
	 * in the game.
	 * 
	 * @param message
	 *            the message to be processed
	 */
	public void sendMessageToServer(ServerMessage message) {
		try {
			System.out.println("Sending message: " + message);
			ps.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		for (;;) {
			// infinite loop accepting messages from the server
			try {
				System.out.println("Client trying to read");
				ClientMessage message = (ClientMessage) ps.readObject();
				System.out.println("Message received: " + message);
				message.execute(theGame);
			} catch (ClassCastException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

}
