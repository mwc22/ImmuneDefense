package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.naming.InvalidNameException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mainmessages.MainRequestOpenGamesMessage;
import model.GameGuts;
import model.Player;

public class GameSearch extends JPanel {
	public static Vector<GameGuts> opengames = new Vector<GameGuts>();

	private JButton returnButton = new JButton("Back");
	private JButton refreshButton = new JButton("Refresh");
	private JButton createGameButton = new JButton("Create Game");
	private static JPanel games = new JPanel();
	private ImmuneResponse view;
	private BackListener bl = new BackListener();
	private CreateGameListener cgl = new CreateGameListener();
	private RefreshListener rl = new RefreshListener();
	private JScrollPane gameScroll;

	private BufferedImage img;
	private String imgName = "Background.jpg";

	public GameSearch(ImmuneResponse outer) throws Exception {
		view = outer;
		if (ImmuneResponse.player == null) {
			String name = JOptionPane.showInputDialog("Please choose a name");
			if (name == null || name.equals(JOptionPane.CANCEL_OPTION)
					|| name.equals(JOptionPane.CLOSED_OPTION))
				throw new InvalidNameException();
			ImmuneResponse.player = new Player(name);
			view.connect();
		}
		view.sendMessage(new MainRequestOpenGamesMessage()); // TODO: Set a
																// time-out
																// limit?
		setupView();
		this.setSize(820, 700);
		this.validate();
	}

	public void setupView() {
		this.setName("Search for a game");
		this.setLayout(null);

		try {
			img = ImageIO.read(new File("images" + File.separator + imgName));
		} catch (IOException e) {

		}

		// JPanel buttonPanel = new JPanel();
		returnButton.addActionListener(bl);
		refreshButton.addActionListener(rl);
		createGameButton.addActionListener(cgl);

		returnButton.setSize(150, 40);
		refreshButton.setSize(150, 40);
		createGameButton.setSize(150, 40);

		returnButton.setLocation(50, 10);
		refreshButton.setLocation(250, 10);
		createGameButton.setLocation(450, 10);

		// buttonPanel.add(returnButton);
		// buttonPanel.add(refreshButton);
		// buttonPanel.add(createGameButton)

		gameScroll = setupGames();
		gameScroll.setSize(gameScroll.getPreferredSize());

		// gameScroll.setMinimumSize(new Dimension(550,600));
		gameScroll.setMaximumSize(new Dimension(550, 600));
		gameScroll.setLocation(50, 50);
		// System.out.println(gameScroll);
		// JPanel container = new JPanel();
		// container.setLayout(new GridLayout(2, 1, 10, 10));
		// container.add(buttonPanel);
		// container.add(gameScroll);
		// System.out.println(gameScroll.getSize());

		this.add(returnButton);
		this.add(refreshButton);
		this.add(createGameButton);
		this.add(gameScroll);

		// this.add(container, BorderLayout.NORTH);
		// this.add(gameScroll, BorderLayout.NORTH);
		this.revalidate();
		this.repaint();

		System.out.println(gameScroll.getPreferredSize());
		System.out.println(gameScroll.getComponentCount());
	}

	public static void rebuildLabels(ImmuneResponse game) {
		games.removeAll();
		for (int i = 0; i < opengames.size(); i++) {
			games.add(new GameLabel(opengames.get(i), game));
		}
	}

	@Override
	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		page.drawImage(img, 0, 0, null);
	}

	private JScrollPane setupGames() {
		games.removeAll();
		// games.setSize(550, 50*opengames.size());
		games.setLayout(new BoxLayout(games, BoxLayout.Y_AXIS));
		System.out
				.println("There are " + opengames.size() + " games available");
		if (opengames.isEmpty()) {
			games.add(new JLabel(
					"There are currently no open games. Please create your own, or refresh this screen."));
		}
		for (int i = 0; i < opengames.size(); i++) {
			System.out.println(opengames.get(i).getInfo());
			GameLabel gameLabel = new GameLabel(opengames.get(i), view);
			games.add(gameLabel);
			System.out.println(gameLabel.getSize());
			System.out.println(gameLabel.getComponentCount());
		}
		games.validate();

		return new JScrollPane(games);
	}

	public void addGame(GameLabel gl) {
		games.add(gl);
	}

	public void removeGame(GameLabel gl) {
		games.remove(gl);
	}

	private class BackListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			view.showPanel(new DefaultView(view));
		}

	}

	private class RefreshListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				view.showPanel(new GameSearch(view));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	private class CreateGameListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			view.showPanel(new GameCreationView(view));
		}

	}

}
