package mainmessages;

import interfaces.GUIMessage;
import interfaces.MainServerMessage;
import model.GameGuts;
import server.MainServer;
import view.GameLabel;

public class MainRequestStartGameMessage implements MainServerMessage {

	private GameGuts gl;
	
	public MainRequestStartGameMessage(GameGuts gl) {
		this.gl = gl;
	}

	@Override
	public GUIMessage create() {
		MainServer.startGame(gl);
		return new MainNullMessage();
	}
	
	
	
}
