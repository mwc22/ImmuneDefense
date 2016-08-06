package messages;

import interfaces.ClientMessage;
import interfaces.ServerMessage;
import server.Server;

public class RequestGameOverMessage implements ServerMessage {

	private int teamID;
	private boolean won;
	
	public RequestGameOverMessage(int teamID, boolean won) {
		this.teamID = teamID;
		this.won = won;
	}
	
	@Override
	public ClientMessage create(Server server) {
		// TODO Auto-generated method stub
		server.endGame();
		return new GameOverMessage(teamID, won);
	}

}
