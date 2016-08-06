package mainmessages;

import interfaces.GUIMessage;

import java.util.Vector;

import server.UpdateScheme;
import view.ImmuneResponse;

import model.Player;

public class MainLobbyPlayersMessage implements GUIMessage {

	private Vector<Vector<Player>> players;
	
	public MainLobbyPlayersMessage(Vector<Vector<Player>> players) {
		this.players = players;
	}

	@Override
	public void execute(ImmuneResponse model) throws Exception {
		// TODO Auto-generated method stub
		for(int i=0;i<players.size();i++) {
			model.addPlayersToLobby(players.get(i),i);
		}
	}

	@Override
	public UpdateScheme getUpdateScheme() {
		return UpdateScheme.Self;
	}
	
}
