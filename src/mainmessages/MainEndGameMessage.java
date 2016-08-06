package mainmessages;

import interfaces.GUIMessage;
import server.UpdateScheme;
import view.GameSearch;
import view.ImmuneResponse;

public class MainEndGameMessage implements GUIMessage {

	public MainEndGameMessage() {
		
	}

	@Override
	public void execute(ImmuneResponse model) throws Exception {
		// TODO Auto-generated method stub
		model.showPanel(new GameSearch(model));
	}

	@Override
	public UpdateScheme getUpdateScheme() {
		// TODO Auto-generated method stub
		return UpdateScheme.All;
	}
	
	
	
}
