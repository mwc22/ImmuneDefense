package mainmessages;

import server.UpdateScheme;
import view.ImmuneResponse;
import interfaces.GUIMessage;

public class MainSendMessage implements GUIMessage {

	private String msg;
	
	public MainSendMessage(String message) {
		msg = message;
	}
	
	@Override
	public void execute(ImmuneResponse model) throws Exception {
		model.addMessage(msg);
		
	}

	@Override
	public UpdateScheme getUpdateScheme() {
		// TODO Auto-generated method stub
		return UpdateScheme.All;
	}
	
}
