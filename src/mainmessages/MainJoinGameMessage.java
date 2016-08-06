package mainmessages;

import interfaces.GUIMessage;

import javax.swing.JOptionPane;

import model.GameGuts;
import server.UpdateScheme;
import view.GameLobbyView;
import view.ImmuneResponse;

public class MainJoinGameMessage implements GUIMessage {

	private boolean successful;
	private GameGuts toJoin;
	private boolean owner;
	
	public MainJoinGameMessage(GameGuts toJoin, boolean successful, boolean owner) {
		this.toJoin = toJoin;
		this.successful = successful;
		this.owner = owner;
	}
	
	@Override
	public void execute(ImmuneResponse model) throws Exception {
		// TODO Auto-generated method stub
		if(successful) {
			model.showPanel(new GameLobbyView(model, toJoin, owner));
		} else {
			JOptionPane.showMessageDialog(model, "The selected game is full");
		}
	}

	@Override
	public UpdateScheme getUpdateScheme() {
		// TODO Auto-generated method stub
		return UpdateScheme.Self;
	}

}
