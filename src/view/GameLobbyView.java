package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import mainmessages.MainRequestLeaveGameMessage;
import mainmessages.MainRequestSendMessage;
import mainmessages.MainRequestStartGameMessage;
import mainmessages.MainRequestSwitchTeamMessage;
import model.GameGuts;
import model.Player;

public class GameLobbyView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5174626362236813280L;

	private ImmuneResponse view;
	private JButton leaveButton = new JButton("Leave");
	private JButton switchTeamsButton = new JButton("Switch");
	private JButton startGameButton = new JButton("Start");
	private static JTextArea display = new JTextArea();
	private static Vector<Player> team1Players = new Vector<Player>();
	private static Vector<Player> team2Players = new Vector<Player>();

	private JTextField input = new JTextField();
	private static JPanel team1 = new JPanel();
	private static JPanel team2 = new JPanel();
//	private PlayerSocket player;
	private boolean owner;

	private BufferedImage img;
	private String imgName = "Background.jpg";

	public static GameGuts game;

	public GameLobbyView(ImmuneResponse view, GameGuts game, boolean owner) {
		this.view = view;
		GameLobbyView.game = game;
		this.owner = owner;
		setupView();
//		startMessageThread();
	}

	public void setupView() {
		this.setName("Game Lobby");
		this.setLayout(null);

		try {
			img = ImageIO.read(new File("images" + File.separator + imgName));
		} catch (IOException e) {

		}

		leaveButton.addActionListener(new BackListener());
		leaveButton.setSize(100, 20);
		leaveButton.setLocation(20, 20);
		switchTeamsButton.addActionListener(new SwitchTeamListener());
		switchTeamsButton.setSize(100, 20);
		switchTeamsButton.setLocation(100, 270);
		if (owner) {
			startGameButton.addActionListener(new StartGameListener());
			startGameButton.setSize(100, 20);
			startGameButton.setLocation(530, 20);
			this.add(startGameButton);
		}

		input.addActionListener(new InputListener());
		input.setBounds(420, 420, 200, 20);
		display.setWrapStyleWord(true);
		display.setLineWrap(true);
		display.setEditable(false);
		display.setFocusable(false);
		display.setOpaque(false);
		display.setBounds(10, 10, 200, 200);
		JScrollPane displayScroll = new JScrollPane(display,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		displayScroll.setBounds(420, 100, 200, 300);
		input.setForeground(Color.GREEN);
		display.setForeground(Color.GREEN);
		input.setBackground(Color.BLACK);
		display.setBackground(Color.BLACK);
		displayScroll.setBackground(Color.BLACK);
		displayScroll.getViewport().setBackground(Color.BLACK);

		team1.setBounds(0, 0, 200, 200);
		team1.setLayout(new GridLayout(4, 1));
		JScrollPane team1pane = new JScrollPane(team1);
		team1pane.setBounds(20, 90, 200, 150);
		team1.setOpaque(false);
		team1pane.setOpaque(false);
		team1pane.getViewport().setOpaque(false);

		team2.setBounds(0, 0, 200, 200);
		team2.setLayout(new GridLayout(4, 1));
		JScrollPane team2pane = new JScrollPane(team2);
		team2pane.setBounds(20, 320, 200, 150);
		team2.setOpaque(false);
		team2pane.setOpaque(false);
		team2pane.getViewport().setOpaque(false);

		JLabel team1label = new JLabel("Team 1");
		JLabel team2label = new JLabel("Team 2");
		team1label.setOpaque(false);
		team2label.setOpaque(false);
		team1label.setForeground(Color.ORANGE);
		team2label.setForeground(Color.ORANGE);
		team1label.setBounds(20, 70, 100, 20);
		team2label.setBounds(20, 300, 100, 20);

		this.add(team2label);
		this.add(team1label);
		this.add(input);
		this.add(displayScroll);
		this.add(leaveButton);
		this.add(switchTeamsButton);
		this.add(team1pane);
		this.add(team2pane);
		this.revalidate();
		this.repaint();
	}

	@Override
	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		page.drawImage(img, 0, 0, null);
	}

//	public void startMessageThread() {
//		player = ImmuneResponse.connection;
//		UpdateManager mm = new UpdateManager();
//		Thread t = new Thread(mm);
//		t.start();
//	}

	public static void addMessage(String message) {
		display.append(message + "\n");
	}

	public static void addPlayer(Player player, int team) {
		if (team == 0)
			team1Players.add(player);
		else
			team2Players.add(player);
		updatePanels();
		
	}

	public static void updatePanels() {
		team1.removeAll();
		team2.removeAll();
		for (Player player : team1Players) {
			JLabel newPlayer = new JLabel(player.getName());
			newPlayer.setBackground(Color.BLACK);
			newPlayer.setForeground(Color.RED);
			newPlayer.setHorizontalAlignment(JLabel.CENTER);
			newPlayer.setOpaque(true);
			team1.add(newPlayer);
		}
		for (Player player : team2Players) {
			JLabel newPlayer = new JLabel(player.getName());
			newPlayer.setBackground(Color.BLACK);
			newPlayer.setForeground(Color.RED);
			newPlayer.setHorizontalAlignment(JLabel.CENTER);
			newPlayer.setOpaque(true);
			team2.add(newPlayer);
		}
	}
	
	public static void switchTeams(Player player, int newTeam) {
		if(team1Players.remove(player))
			team2Players.add(player);
		else if(team2Players.remove(player))
			team1Players.add(player);
		updatePanels();
		
	}
	
	public static void removePlayer(Player player) {
		team1Players.remove(player);
		team2Players.remove(player);
		updatePanels();
	}

	public String readMessage() {
		String message = ImmuneResponse.player.getName() + ": "
				+ input.getText();
		return message;
	}

	private class BackListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				view.sendMessage(new MainRequestLeaveGameMessage(ImmuneResponse.player, game));
				view.showPanel(new GameSearch(view));
			} catch (Exception e1) {
				JOptionPane
						.showMessageDialog(
								null,
								"The connection to the server failed, please try again later or reinstall the program.");
				view.showPanel(new DefaultView(view));
			}
		}
	}

	private class SwitchTeamListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// MainRequestSwitchTeamMessage(game, ImmuneResponse.player,
			// ImmuneResponse.player.);
			
			try {
				view.sendMessage(new MainRequestSwitchTeamMessage(game, ImmuneResponse.player));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	private class StartGameListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				view.sendMessage(new MainRequestStartGameMessage(game));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	private class InputListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			try {
				view.sendMessage(new MainRequestSendMessage(readMessage(),
						game));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			input.setText("");
		}

	}

}
