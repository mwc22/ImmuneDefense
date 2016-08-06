package view;

import gamemaps.Map1;
import gamemaps.Map2;
import gamemaps.TestMapMultiplayer1;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import server.PlayerSocket;
import server.Server;
import model.MultiPlayer;
import model.SinglePlayerUsingServer;

public class MapSelectionView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 812112311267987207L;
	private ImmuneResponse view;
	private JButton map1= new JButton("Map 1");
	private JButton map2= new JButton("Map 2");
	private JButton back = new JButton("Back");
	private BufferedImage img;
	private String imgName = "Background.jpg";
	
	public MapSelectionView(ImmuneResponse view) {
		System.out.println("creating mapselecter");
		this.view = view;
		setupView();
		this.setSize(650,500);
	}
	
	public void setupView() {
		view.setSize(675, 535);
		try {
			img = ImageIO.read(new File("images" + File.separator
					+ imgName));
		} catch (IOException e) {

		}
		this.setName("Choose Map");
		this.setVisible(true);
		this.setLayout(null);
		
		map1.setLocation(275, 430);
		map1.setSize(100,50);
		map1.addActionListener(new StartGameListenerMap1(view));
		
		map2.setLocation(375,430);
		map2.setSize(100,50);
		map2.addActionListener(new StartGameListenerMap2(view));
		
		back.setLocation(175, 430);
		back.setSize(100, 50);
		back.addActionListener(new DefaultViewListener(view));
		

//		this.setSize(675, 535);
//		this.setName("Choose Map");
//		this.setVisible(true);
		
		
		
		this.add(map1);
		this.add(map2);
		this.add(back);
	}
	
	@Override
	public void paintComponent(Graphics page)
	{
	    super.paintComponent(page);
	    page.drawImage(img, 0, 0, null);
	}
	
	
	private class StartGameListenerMap1 implements ActionListener {
		private ImmuneResponse game;
		private Server server;
		Thread serverThread;

		public StartGameListenerMap1(ImmuneResponse game) {
			this.game = game;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {			
			game.showPanel(new GameView(game, new SinglePlayerUsingServer("player", new Map1())));
			/*
			server = new Server(4000, 2);
			serverThread = new Thread(server);
			serverThread.start();
			
			PlayerSocket pc;
			try {
				pc = new PlayerSocket(new Socket("localhost", 4000));
				JPanel theGame = new GameView(game, new MultiPlayer(pc, new TestMapMultiplayer1(), 0));
				game.showPanel(theGame);
			} catch (IOException e) {
				e.printStackTrace();
			}
			*/
		}
	}
	
	private class StartGameListenerMap2 implements ActionListener {
		private ImmuneResponse game;

		public StartGameListenerMap2(ImmuneResponse game) {
			this.game = game;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			game.showPanel(new GameView(game, new SinglePlayerUsingServer("player", new Map2())));			
			/*
			PlayerSocket pc;
			try {
				pc = new PlayerSocket(new Socket("localhost", 4000));
				JPanel theGame = new GameView(game, new MultiPlayer(pc, new TestMapMultiplayer1(), 1));
				game.showPanel(theGame);
			} catch (IOException e) {
				e.printStackTrace();
			}
			*/
		}
	}
	
	private class DefaultViewListener implements ActionListener {
		private ImmuneResponse game;

		public DefaultViewListener(ImmuneResponse game) {
			this.game = game;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			game.showPanel(new DefaultView(game));
		}		
	}	
}
