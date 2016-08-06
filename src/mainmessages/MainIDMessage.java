package mainmessages;

import server.UpdateScheme;
import view.ImmuneResponse;
import interfaces.GUIMessage;

public class MainIDMessage implements GUIMessage {

	private int ID;
	
	public MainIDMessage(int ID) {
		this.ID = ID;
	}
	
	@Override
	public void execute(ImmuneResponse model) throws Exception {
		// TODO Auto-generated method stub
		ImmuneResponse.player.ID = ID;
	}

	@Override
	public UpdateScheme getUpdateScheme() {
		// TODO Auto-generated method stub
		return UpdateScheme.Self;
	}

	
}
