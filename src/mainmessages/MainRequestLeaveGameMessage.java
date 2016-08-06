package mainmessages;

import interfaces.GUIMessage;
import interfaces.MainServerMessage;
import model.GameGuts;
import model.Player;
import server.MainServer;

public class MainRequestLeaveGameMessage implements MainServerMessage {

	private Player player;
	private GameGuts game;
	
	public MainRequestLeaveGameMessage(Player player, GameGuts toLeave) {
		this.player = player;
		this.game = toLeave;
	}
	
	@Override
	public GUIMessage create() {
		// TODO Auto-generated method stub
		MainServer.removeFromGame(player, game);
		return new MainNullMessage();
	}

	
	
}
