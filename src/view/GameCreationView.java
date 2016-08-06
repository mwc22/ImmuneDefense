package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import mainmessages.MainRequestCreateGameMessage;
import model.GameGuts;
import builders.GameMapBuilder.GameMapSelector;

public class GameCreationView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1001785544598427516L;
	private JButton returnButton = new JButton("Back");
	private JButton createGameButton = new JButton("Create Game");
//	private JLabel portTag = new JLabel("Port Number:");
//	private JTextField portField = new JTextField();
	private ImmuneResponse view;
	private BackListener bl = new BackListener();
	private CreateGameListener cgl = new CreateGameListener();
	// Game name: defaults to Player's name's game.
	private JTextField gameNameField = new JTextField(ImmuneResponse.player.getName() + "'s Game");
	private JLabel gameNameTag = new JLabel("Game Name:");
	// Number of players
	private JComboBox numberPlayersField;
	private JLabel numberPlayersTag = new JLabel("Max # players per team:");
	// Map selection
	private JRadioButton map1 = new JRadioButton("Map 1", true);
	private JRadioButton map2 = new JRadioButton("Map 2", false);
	private ButtonGroup buttonGroup = new ButtonGroup();
	
	private BufferedImage img;
	private String imgName = "Background.jpg";

	public GameCreationView(ImmuneResponse outer) {
		view = outer;
		setupView();
		this.setVisible(true);
		view.repaint();
	}

	public void setupView() {
		this.setName("Create Game");
		
		try {
			img = ImageIO.read(new File("images" + File.separator
					+ imgName));
		} catch (IOException e) {

		}
		
		JPanel inputs = new JPanel();
		
		buttonGroup.add(map1);
		buttonGroup.add(map2);
		JPanel mapButtons = new JPanel();
		mapButtons.add(map1);
		mapButtons.add(map2);
		
		returnButton.addActionListener(bl);
		createGameButton.addActionListener(cgl);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(returnButton);
		buttonPanel.add(createGameButton);
		
		gameNameField.setEditable(true);
//		gameNameField.setText("Hi everyone");
		
		inputs.setLayout(new GridLayout(2, 2, 5, 5));

		inputs.add(gameNameTag);
		gameNameTag.setForeground(Color.ORANGE);
		inputs.add(gameNameField);
		inputs.add(numberPlayersTag);
		numberPlayersTag.setForeground(Color.ORANGE);
		Integer[] intArray = {1,2,3,4,5,6,7,8};
		numberPlayersField = new JComboBox<Object>(intArray);
		numberPlayersField.setSelectedIndex(0);
		inputs.add(numberPlayersField);

		JPanel container = new JPanel();
		container.setLayout(new GridLayout(3,1, 10, 10));
		
		buttonPanel.setOpaque(false);
		inputs.setOpaque(false);
		mapButtons.setOpaque(false);
		container.add(buttonPanel);
		container.add(inputs);
		container.add(mapButtons);
		container.setOpaque(false);
		this.add(container);
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void paintComponent(Graphics page)
	{
	    super.paintComponent(page);
	    page.drawImage(img, 0, 0, null);
	}
	
	public GameMapSelector getMap() {
		if(map1.isSelected())
			return GameMapSelector.Map1;
		if(map2.isSelected())
			return GameMapSelector.Map2;
		else
			return null;
	}

	private class BackListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				view.showPanel(new GameSearch(view));
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, "The connection to the server failed, please try again later or reinstall the program.");
				view.showPanel(new DefaultView(view));
			}
		}
	}

	private class CreateGameListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO: Put creation of multiplayer game here
			int numberPlayer = (int) numberPlayersField.getSelectedItem();
			String name = gameNameField.getText();
			GameMapSelector game = getMap();
			GameGuts gg = new GameGuts(ImmuneResponse.player, numberPlayer, name, game);
			System.out.println(gg.getInfo());
//			GameLabel gl = new GameLabel(gg);
			try {
				view.sendMessage(new MainRequestCreateGameMessage(gg, ImmuneResponse.player));
			} catch ( Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

}
