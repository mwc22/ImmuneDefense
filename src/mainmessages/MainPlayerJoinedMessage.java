package mainmessages;

import interfaces.GUIMessage;
import model.Player;
import server.UpdateScheme;
import view.GameLobbyView;
import view.ImmuneResponse;

public class MainPlayerJoinedMessage implements GUIMessage {

	private Player player;
	private int team;
	
	public MainPlayerJoinedMessage(Player joined, int team) {
		this.player = joined;
		this.team = team;
	}
	
	@Override
	public void execute(ImmuneResponse model) throws Exception {
		// TODO Auto-generated method stub
		model.addPlayerToLobby(player, team);
	}

	@Override
	public UpdateScheme getUpdateScheme() {
		// TODO Auto-generated method stub
		return UpdateScheme.All;
	}

}
