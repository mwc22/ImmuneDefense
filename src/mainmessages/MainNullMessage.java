package mainmessages;

import server.UpdateScheme;
import view.ImmuneResponse;
import interfaces.ClientMessage;
import interfaces.GUIMessage;
import interfaces.TowerDefenseModel;

public class MainNullMessage implements GUIMessage, ClientMessage {

	@Override
	public void execute(ImmuneResponse model) throws Exception {
		//nothing
	}

	@Override
	public UpdateScheme getUpdateScheme() {
		// TODO Auto-generated method stub
		return UpdateScheme.Self;
	}

	@Override
	public void execute(TowerDefenseModel model) throws Exception {
		// nothing
		
	}

	@Override
	public int getSendersId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
