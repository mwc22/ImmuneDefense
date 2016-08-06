package model;

import java.io.Serializable;

/**
 * Controls the moves of the player
 * 
 * @author Bhavana Gorti
 * @author Patrick Martin
 * @author Travis Woodrow
 * @author Michael Curtis
 * 
 */
public class Player implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7421322803697140186L;

	public int ID;
	
	private int money;
	private int life;
	private String name;
//	private TowerDefenseModel game;


	public Player(String name, int startingGold, int startingLife) {
		money = startingGold;
		life = startingLife;
		this.setName(name);
//		this.game = game;
	}
	public Player(String name)
	{
		money = 500;
		life = 100;
		this.setName(name);
//		this.game = game;
	}
//	
//	public Player(String name) {
//		this.setName(name);
//	}
	
	public boolean equals(Player p) {
		return this.ID == p.ID;
	}
	
	public void prepareForGame(int startingGold, int startingLife) {
		money = startingGold;
		life = startingLife;
//		this.game = game;
	}
	
//	public void prepareForGame(TowerDefenseModel game) { //TODO: This method shouldn't be used; rather, the game should give the players gold explicitly
//		money = 500;
//		life = 100;
//		this.game = game;
//	}

	public void buy(int cost) {
		money -= Math.min(cost, money);
	}

	public int getLife() {
		return life;
	}

	public void loseLife(int removeLife) {
		life -= removeLife;
		if(life <= 0) {
			life = 0;
//			game.lose();
		}
	}

	public int receiveMoney() {
		return life;
	}

	public void addMoney(int money) {
		this.money += money;
	}

	public int getMoney() {
		return money;
	}
	
	public String getInformation() {
		return getName() + "\n" + money + "\n" + life;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
