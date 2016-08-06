package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import mainmessages.MainRequestJoinGameMessage;
import mainmessages.MainRequestLobbyPlayersMessage;
import model.GameGuts;

public class GameLabel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9189328276615325160L;

	public static Dimension size = new Dimension(550, 50);
	private GameGuts gg;
	private ImmuneResponse view;
	private JButton joinGameButton = new JButton();
	private JTextArea info;
	private ActionListener jgl = new JoinGameListener();
//	private Vector<Player> players = new Vector<Player>();
	public int ID;
	
	public GameLabel(GameGuts gg, ImmuneResponse view) {
		this.gg = gg;
		this.view = view;
		setUpView();
		this.validate();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return size;
	}
	
	private void setUpView() {
		this.setSize(size);
		this.setLayout(new GridLayout(2, 2, 5, 5));
		
		info = new JTextArea(50,2);
		info.setText(gg.getInfo());
		info.setEditable(false);
		
		joinGameButton = new JButton("Join game");
		joinGameButton.addActionListener(jgl);
		
		
		this.add(info);
		this.add(joinGameButton);
//		this.setVisible(true);
	}
	
	public void updatePlayers(int numPlayers) {
		gg.setCurrentPlayers(numPlayers);
		info.setText(gg.getInfo());
		if(gg.getCurrentPlayers() >= gg.getMaxNumberOfPlayers()) {
			joinGameButton.setEnabled(false);
		} else
			joinGameButton.setEnabled(true);
	}
	
	
	private class JoinGameListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Joining game");
			try {
				view.sendMessage(new MainRequestLobbyPlayersMessage(gg));
				view.sendMessage(new MainRequestJoinGameMessage(gg, ImmuneResponse.player));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
