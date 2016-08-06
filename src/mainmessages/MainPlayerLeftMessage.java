package mainmessages;

import interfaces.GUIMessage;
import model.Player;
import server.UpdateScheme;
import view.ImmuneResponse;

public class MainPlayerLeftMessage implements GUIMessage {

	private Player player;
	
	public MainPlayerLeftMessage(Player player) {
		this.player = player;
	}
	
	@Override
	public void execute(ImmuneResponse model) throws Exception {
		// TODO Auto-generated method stub
		model.addMessage(player.getName() + " has left.");
		model.removePlayerFromLobby(player);
	}

	@Override
	public UpdateScheme getUpdateScheme() {
		// TODO Auto-generated method stub
		return UpdateScheme.All;
	}

}
