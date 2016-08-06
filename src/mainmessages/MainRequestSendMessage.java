package mainmessages;

import interfaces.GUIMessage;
import interfaces.MainServerMessage;
import model.GameGuts;
import server.MainServer;

public class MainRequestSendMessage implements MainServerMessage {

	private String msg;
	private GameGuts game;
	
	public MainRequestSendMessage(String message, GameGuts gg) {
		msg = message;
		game = gg;
	}
	
	@Override
	public GUIMessage create() {
		// TODO Auto-generated method stub
		MainServer.sendMessage(new MainSendMessage(msg),game);
		return new MainNullMessage();
	}

}
