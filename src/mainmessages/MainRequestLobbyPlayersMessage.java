package mainmessages;

import interfaces.GUIMessage;
import interfaces.MainServerMessage;

import java.util.Vector;

import model.GameGuts;
import model.Player;
import server.MainServer;

public class MainRequestLobbyPlayersMessage implements MainServerMessage {

	private GameGuts gg;
	
	public MainRequestLobbyPlayersMessage(GameGuts gg) {
		this.gg = gg;
	}
	
	@Override
	public GUIMessage create() {
		// TODO Auto-generated method stub
		Vector<Vector<Player>> players = MainServer.getPlayersByTeam(gg);
		return new MainLobbyPlayersMessage(players);
	}

}
