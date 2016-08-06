package mainmessages;

import interfaces.GUIMessage;

import java.util.Vector;

import model.GameGuts;
import server.UpdateScheme;
import view.GameLabel;
import view.GameSearch;
import view.ImmuneResponse;


public class MainOpenGamesMessage implements GUIMessage {

	private Vector<GameGuts> gg;
	
	public MainOpenGamesMessage(Vector<GameGuts> gg) {
		this.gg = gg;
	}
	
	@Override
	public void execute(ImmuneResponse model) throws Exception {
//		GameSearch.opengames = gg;
		model.updateGameSearch(gg);
	}

	@Override
	public UpdateScheme getUpdateScheme() {
		// TODO Auto-generated method stub
		return UpdateScheme.Self;
	}

}
