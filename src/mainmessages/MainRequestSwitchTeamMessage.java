package mainmessages;

import interfaces.GUIMessage;
import interfaces.MainServerMessage;
import model.GameGuts;
import model.Player;
import server.MainServer;

public class MainRequestSwitchTeamMessage implements MainServerMessage {

	private GameGuts game;
	private Player toMove;
	
	public MainRequestSwitchTeamMessage(GameGuts game, Player toMove) {
		this.game = game;
		this.toMove = toMove;
	}

	@Override
	public GUIMessage create() {
		if(MainServer.switchTeam(game,toMove))
			return new MainNullMessage();
		else
			return new MainFailedSwitchTeamMessage();
	}
}
