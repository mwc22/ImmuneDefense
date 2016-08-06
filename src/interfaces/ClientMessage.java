package interfaces;

import java.io.Serializable;

import server.UpdateScheme;

/**
 * Interface facilitating client updating.
 * @author Travis
 *
 */
public interface ClientMessage extends Serializable{
	
	/**
	 * Executes this ClientMessage
	 * @param model the model to execute this message on
	 * @throws Exception 
	 */
	public abstract void execute(TowerDefenseModel model) throws Exception;
	
	/**
	 * Returns the ClientMessage's UpdateScheme.
	 * @return
	 */
	public abstract UpdateScheme getUpdateScheme();
	
	/**
	 * Get the user name of the player which sent this message.
	 * @return
	 */
	public abstract int getSendersId();
}
