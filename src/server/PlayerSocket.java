package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class to encapsulate communication between server and clients. 
 * @author Travis
 *
 */
public class PlayerSocket {
	
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public PlayerSocket(Socket player) {
		socket = player;
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeObject(Object o) throws IOException {
		System.out.println("PlayerSocket sending message: " + o);
		oos.writeObject(o);
		oos.flush();
	}
	
	public synchronized Object readObject() throws ClassNotFoundException, IOException {
		return ois.readObject();
	}
}