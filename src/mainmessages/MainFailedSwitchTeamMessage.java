package mainmessages;

import interfaces.GUIMessage;

import javax.swing.JOptionPane;

import server.UpdateScheme;
import view.ImmuneResponse;

public class MainFailedSwitchTeamMessage implements GUIMessage {

	@Override
	public void execute(ImmuneResponse model) throws Exception {
		JOptionPane.showMessageDialog(model, "The other team is full.");
		
	}

	@Override
	public UpdateScheme getUpdateScheme() {
		// TODO Auto-generated method stub
		
		return UpdateScheme.Self;
	}


	
}
