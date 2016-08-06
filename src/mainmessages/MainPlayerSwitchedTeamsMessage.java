package mainmessages;

import server.UpdateScheme;
import view.ImmuneResponse;
import interfaces.GUIMessage;
import model.Player;

public class MainPlayerSwitchedTeamsMessage implements GUIMessage {

	private Player player;
	private int team;
	
	public MainPlayerSwitchedTeamsMessage(Player switched, int newTeam) {
		player = switched;
		team = newTeam;
	}

	@Override
	public void execute(ImmuneResponse model) throws Exception {
		// TODO Auto-generated method stub
		model.playerSwitchedTeams(player, team);
	}

	@Override
	public UpdateScheme getUpdateScheme() {
		// TODO Auto-generated method stub
		return UpdateScheme.All;
	}
}
