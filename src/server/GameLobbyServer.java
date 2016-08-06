package server;

import interfaces.GUIMessage;

import java.io.IOException;
import java.util.Vector;

import mainmessages.MainPlayerJoinedMessage;
import mainmessages.MainPlayerSwitchedTeamsMessage;
import mainmessages.MainStartGameMessage;
import model.GameGuts;
import model.Player;

public class GameLobbyServer {

	//I need the GameLabel

	private GameGuts game;
	private PlayerSocket[][] players;
	private Player[][] playerIDs;
	private Vector<PlayerSocket> playerVector = new Vector<PlayerSocket>();
	private Vector<Integer> playerIDVector = new Vector<Integer>();

	public static final int NUMBER_OF_TEAMS = 2;

	public GameLobbyServer(GameGuts gg) {
		game = gg;
		players = new PlayerSocket[NUMBER_OF_TEAMS][gg.getTeamSize()];
		playerIDs = new Player[NUMBER_OF_TEAMS][gg.getTeamSize()];
	}

	public GameGuts getGameLabel() {
		return game;
	}

	public void sendMessage(GUIMessage message) {
		for (int i = 0; i < playerVector.size(); i++) {
			try {
				playerVector.get(i).writeObject(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean add(PlayerSocket ps, Player player, int ID) {
		if (game.join(player)) {
			for (int i = 0; i < players.length; i++) {
				for (int j = 0; j < players[i].length; j++) {
					if (players[i][j] == null) {
						players[i][j] = ps;
						playerIDs[i][j] = player;
						playerVector.add(ps);
						playerIDVector.add(ID);
						sendMessage(new MainPlayerJoinedMessage(player, i));
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean removePlayer(Player player, PlayerSocket ps) {
		boolean removed = false;
		for (int i = 0; i < players.length; i++) {
			for (int j = 0; j < players[i].length; j++) {
				if (players[i][j] != null && players[i][j].equals(ps)) {
					players[i][j] = null;
					playerIDs[i][j] = null;
					playerVector.remove(ps);
					playerIDVector.remove(player);
					removed = true;
				}
			}
		}
		if(removed) {
			game.remove(player);
		}
		if(game.getCurrentPlayers() <= 0) {
			MainServer.removeGame(game);
		}
		return removed;
		
	}

	public boolean switchTeam(Player moved, PlayerSocket toMove) {
		//first find the playersocket and remove it
		int x = -1;
		int y = -1;

		for (int i = 0; i < players.length; i++) {
			for (int j = 0; j < players[i].length; j++) {
				if (players[i][j] != null && players[i][j].equals(toMove)) {
					x = i;
					y = j;
				}
			}
		}
		if (x < 0 || y < 0)
			return false;
		int toJoin=0;
		if(x == 0)
			toJoin = 1;
		//then put it somewhere else
		for (int j = 0; j < players[toJoin].length; j++) {
			if (players[toJoin][j] == null) {
				players[toJoin][j] = toMove;
				playerIDs[toJoin][j] = moved;
				players[x][y] = null;
				playerIDs[x][y] = null;
				sendMessage(new MainPlayerSwitchedTeamsMessage(moved, toJoin));
				return true;
			}
		}
		return false;
	}

	public Vector<Integer> getPlayerIDs() {
		return playerIDVector;
	}

	public Vector<Vector<Player>> getPlayersByTeam() {
		Vector<Vector<Player>> playersByTeam = new Vector<Vector<Player>>();
		for (int i = 0; i < playerIDs.length; i++) {
			Vector<Player> team = new Vector<Player>();
			for (int j = 0; j < playerIDs[i].length; j++) {
				if (playerIDs[i][j] != null)
					team.add(playerIDs[i][j]);
			}
			playersByTeam.add(team);

		}
		return playersByTeam;

	}

	public Server startGame() {
		//		Vector<PlayerSocket> allPlayers = new Vector<PlayerSocket>();
		//		for(int i=0;i<players.length;i++) {
		//			for(int j=0;j<players[i].length;j++) {
		//				if(players[i][j] == null) {
		//					allPlayers.add(players[i][j]);
		//				}
		//			}
		//		}
		Vector<Vector<PlayerSocket>> playersByTeam = new Vector<Vector<PlayerSocket>>();
		for (int i = 0; i < players.length; i++) {
			Vector<PlayerSocket> team = new Vector<PlayerSocket>();
			for (int j = 0; j < players[i].length; j++) {
				if (players[i][j] != null)
					team.add(players[i][j]);
			}
			playersByTeam.add(team);

		}
		
		Server server = new Server(playersByTeam, playerIDVector);
		for (int i = 0; i < players.length; i++) {
			for (int j = 0; j < players[i].length; j++) {
				if (players[i][j] != null) {
					try {
						players[i][j].writeObject(new MainStartGameMessage(
								game, i));
					} catch (IOException e) {
						// please no errors
						e.printStackTrace();
					}
				}
			}
		}
		return server;

	}

}
