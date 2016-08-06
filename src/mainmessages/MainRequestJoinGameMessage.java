package mainmessages;

import interfaces.GUIMessage;
import interfaces.MainServerMessage;
import model.GameGuts;
import model.Player;
import server.MainServer;

public class MainRequestJoinGameMessage implements MainServerMessage {

	private GameGuts gameToJoin;
	private Player player;
	
	public MainRequestJoinGameMessage(GameGuts toJoin, Player player) {
		gameToJoin = toJoin;
		this.player = player;
	}

	@Override
	public GUIMessage create() {
		boolean successful = MainServer.addPlayerToGame(player,gameToJoin);
		return new MainJoinGameMessage(gameToJoin,successful, false);
	}
	
}
