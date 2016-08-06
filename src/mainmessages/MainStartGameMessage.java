package mainmessages;

import interfaces.GUIMessage;
import model.GameGuts;
import server.UpdateScheme;
import view.ImmuneResponse;

public class MainStartGameMessage implements GUIMessage {

	private GameGuts gg;
	private int teamId;
	
	public MainStartGameMessage(GameGuts gl, int teamId) {
		this.gg = gl;
		this.teamId = teamId;
	}
	
	@Override
	public void execute(ImmuneResponse model) throws Exception {
		model.startMultiplayerGame(gg, teamId);
	}
	
	@Override
	public UpdateScheme getUpdateScheme() {
		// TODO Auto-generated method stub
		return UpdateScheme.All;
	}

}
