package interfaces;
import java.io.Serializable;

import server.Server;

// imports

/**
 * Interface for server messages being passed from client to server.
 * @author Travis
 *
 */
public interface ServerMessage extends Serializable{

	/**
	 * Creates a ClientMessage for the server to pass to relevant clients
	 */
	public abstract ClientMessage create(Server server);
	
}
