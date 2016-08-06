package mainmessages;

import interfaces.GUIMessage;
import interfaces.MainServerMessage;
import server.MainServer;

public class MainRequestOpenGamesMessage implements MainServerMessage {
	
	public MainRequestOpenGamesMessage() {
		
	}
	
	@Override
	public GUIMessage create() {
		// TODO Auto-generated method stub
		return new MainOpenGamesMessage(MainServer.openGames);
	}


}
