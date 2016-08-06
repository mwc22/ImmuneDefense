package messages;

import server.UpdateScheme;
import interfaces.ClientMessage;
import interfaces.TowerDefenseModel;

public class GameOverMessage implements ClientMessage {

	private int teamID;
	private boolean won;
	
	public GameOverMessage(int teamID, boolean won) {
		this.teamID = teamID;
		this.won = won;
	}
	
	@Override
	public void execute(TowerDefenseModel model) throws Exception {
		// TODO Auto-generated method stub
		int myID = model.getTeamID();
		if(won == (myID == teamID)) { //don't judge. If this is the team that sent the message, then it 
			model.win();
		} else {
			model.lose();
		}
	}

	@Override
	public UpdateScheme getUpdateScheme() {
		// TODO Auto-generated method stub
		return UpdateScheme.All;
	}

	@Override
	public int getSendersId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
