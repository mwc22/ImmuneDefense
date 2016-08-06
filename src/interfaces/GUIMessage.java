package interfaces;

import java.io.Serializable;

import server.UpdateScheme;
import view.ImmuneResponse;

/**
 * Interface facilitating client updating.
 * @author Patrick
 *
 */
public interface GUIMessage extends Serializable{
	
	/**
	 * Executes this GUIMessage to update something in the gui
	 * @param model the model to execute this message on
	 * @throws Exception 
	 */
	public abstract void execute(ImmuneResponse model) throws Exception;

	public abstract UpdateScheme getUpdateScheme();
	
}
