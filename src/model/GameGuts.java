package model;

import interfaces.GameMap;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JTextArea;

import server.GameLobbyServer;
import builders.GameMapBuilder;
import builders.GameMapBuilder.GameMapSelector;

public class GameGuts implements Serializable {

	private static final long serialVersionUID = 9189328276615325160L;

	public static Dimension size = new Dimension(550, 50);
//	public static Player user;
	
	private int maxNumberOfPlayersPerTeam;
	private int maxNumberOfPlayers;
	private int currentPlayers;
	private GameMapSelector game;
	private Player owner;
	private String title;
	private Vector<Player> players = new Vector<Player>();
	public int ID;
	
	public GameGuts(Player owner, int numberOfPlayersPerTeam, String title, GameMapSelector gm) {
		this.owner = owner;
		this.maxNumberOfPlayersPerTeam = numberOfPlayersPerTeam;
		this.maxNumberOfPlayers = maxNumberOfPlayersPerTeam * GameLobbyServer.NUMBER_OF_TEAMS;
		this.currentPlayers = 0;
		this.title = title;
		this.game = gm;
	}
	
	public GameGuts(Player owner, int numberOfPlayersPerTeam, int currentPlayers, String title, GameMapSelector gm) {
		this.owner = owner;
		this.maxNumberOfPlayersPerTeam = numberOfPlayersPerTeam;
		this.maxNumberOfPlayers = maxNumberOfPlayersPerTeam * GameLobbyServer.NUMBER_OF_TEAMS;
		this.currentPlayers = currentPlayers;
		this.title = title;
		this.game = gm;
	}
	
	public GameMap getGame() throws Exception {
		return GameMapBuilder.getMap(game);
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public int getTeamSize() {
		return maxNumberOfPlayersPerTeam;
	}
	
	public boolean join(Player player) {
		if(currentPlayers < maxNumberOfPlayers) {
			players.add(player);
			currentPlayers++;
			return true;
		}
		else
			return false;
	}
	
	public boolean remove(Player player) {
		if(players.remove(player)) {
			currentPlayers--;
			return true;
		} else
			return false;
	}
	
	public boolean removePlayer(int PlayerID) {
		for(int i=0;i<players.size();i++) {
			if(players.get(i).ID == PlayerID) {
				players.remove(i);
				return true;
			}
				
		}
		return false;
	}
	
	public boolean equals(GameGuts gg) {
		return ID == gg.ID;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}

	public int getCurrentPlayers() {
		// TODO Auto-generated method stub
		return currentPlayers;
	}

	public int getMaxNumberOfPlayers() {
		// TODO Auto-generated method stub
		return maxNumberOfPlayers;
	}
	
	public String getInfo() {
		return title + "\t" + currentPlayers + "/" + maxNumberOfPlayers;
	}

	public void setCurrentPlayers(int numPlayers) {
		// TODO Auto-generated method stub
		currentPlayers = numPlayers;
	}
	
}
