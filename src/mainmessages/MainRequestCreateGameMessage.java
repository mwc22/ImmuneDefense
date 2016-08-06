package mainmessages;

import interfaces.GUIMessage;
import interfaces.MainServerMessage;
import model.GameGuts;
import model.Player;
import server.MainServer;

public class MainRequestCreateGameMessage implements MainServerMessage {

	private GameGuts gg;
	private Player player;
	
	public MainRequestCreateGameMessage(GameGuts gl, Player player) {
		this.gg = gl;
		this.player = player;
	}
	
	@Override
	public GUIMessage create() {
		// TODO Auto-generated method stub
		GameGuts created = MainServer.addGame(gg, player);
		System.out.println("Sending joingamemessage");
		return new MainJoinGameMessage(created,true, true);
	}

}
