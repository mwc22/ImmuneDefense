package view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.naming.InvalidNameException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DefaultView extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2305573186530020797L;
	private JButton instructions = new JButton("Instructions");
	private JButton singlePlayer = new JButton("Single Player");
	private JButton multiPlayer = new JButton("Multiple Players");
	private BufferedImage img;
	private String imgName = "TitlePageBackground.jpg";
	private ImmuneResponse view;
	
	public DefaultView(ImmuneResponse view) {
		this.view = view;
		setupView();
	}
	
	public void setupView() {
//		view.setSize(600, 200);
		this.setName("Main Menu");
		view.setSize(675, 535);
		this.setVisible(true);
		
		try {
			img = ImageIO.read(new File("images" + File.separator
					+ imgName));
		} catch (IOException e) {

		}
		
		instructions.addActionListener(new InstructionsListener());
		singlePlayer.addActionListener(new ChooseMapListener(view));
		multiPlayer.addActionListener(new MultiplayerListener(view));
		instructions.setSize(150, 50);
		instructions.setLocation(95, 420);
		singlePlayer.setSize(150, 50);
		singlePlayer.setLocation(245, 420);
		multiPlayer.setSize(150, 50);
		multiPlayer.setLocation(395, 420);
		
		this.setLayout(null);
		this.add(instructions);
		this.add(singlePlayer);
		this.add(multiPlayer);
		this.setFocusable(true);
	}
	
	@Override
	public void paintComponent(Graphics page)
	{
	    super.paintComponent(page);
	    page.drawImage(img, 0, 0, null);
	}
	
	
	private class InstructionsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null,
					wrapText(45, " Build towers. Kill mobs. Get money."));
		}

		private String wrapText(int limit, String str) {
			String wrap = "";
			int lastEnding = 0;
			int prevSpace = 0;
			int count = 0;
			for (int i = 0; i < str.length(); i++) {
				count++;
				if (str.charAt(i) == ' ' || str.charAt(i) == '\n') {
					prevSpace = i;
				}
				if (str.charAt(i) == '\n')
					count = 0;
				if (count > limit) {
					wrap += str.substring(lastEnding, prevSpace) + "\n";
					count = 0;
					lastEnding = prevSpace + 1;
				}
			}
			wrap += str.substring(lastEnding);
			return wrap;
		}
	}

	private class MultiplayerListener implements ActionListener {
		private ImmuneResponse game;

		public MultiplayerListener(ImmuneResponse game) {
			this.game = game;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			try {
				game.showPanel(new GameSearch(game));
			} catch(InvalidNameException e) {
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "The connection to the server failed, please try again later or reinstall the program.");
			}				
		}		
	}
	
	
	private class ChooseMapListener implements ActionListener {
		private ImmuneResponse game;

		public ChooseMapListener(ImmuneResponse game) {
			this.game = game;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			game.showPanel(new MapSelectionView(game));
		}
		
	}
}
