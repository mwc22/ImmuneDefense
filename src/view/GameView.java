package view;

import interfaces.TowerDefenseModel;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

import model.MultiPlayer2;
import model.Player;
import builders.AntibodyBuilder;
import builders.CreepBuilder;
import builders.InfluenzaBuilder;
import builders.MacrophageBuilderTest;
import builders.TowerBuilder;

/**
 * Deprecated in favor of SinglePlayerUsingServer
 * @author Travis
 *
 */
public class GameView extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	private JPanel minimap;
	private JTextArea informationPane;
	private JTextArea playerPane;
	private Vector<JButton> gameButtons = new Vector<JButton>();
	private Vector<BuyTowerButtonListener> buyTowerListeners = new Vector<BuyTowerButtonListener>();
	private JPanel gamePanel;
	private Player player;
	private KeyPressListener kpl = new KeyPressListener();

	private ImmuneResponse game;
	private Container cp;

	private ScrollViewScreen view;
	private Timer timer;
	private final int MILLISECONDS_BETWEEN_UPDATES = 33;
	private TowerDefenseModel model;

	// vars for adding BuyTowerButtons
	private static int buyTowerYCoord = 300;
	private static final int buyTowerXCoord = 820;
	private static int buyCreepYCoord = 500;
	private static final int buyCreepXCoord = 820;
	private static final int buyButtonHeight = 20;
	private static final int buyButtonWidth = 150;

	public GameView(ImmuneResponse game, TowerDefenseModel model) {
		this.game = game;
		setupView(model);
	}

	private void clearSelected() {
		// clear all listeners and buttons
		for (int z = 0; z < gameButtons.size(); z++) {
			gameButtons.get(z).setForeground(Color.BLACK);
			gameButtons.get(z).setBackground(Color.WHITE);
		}

		for (int z = 0; z < buyTowerListeners.size(); z++) {
			buyTowerListeners.get(z).clear();
		}
	}
	public void setupView(TowerDefenseModel model) {
		this.setName("Immune Response TD");
		this.setVisible(true);
		this.setLayout(null);
		game.setSize(1000, 735);
		view = new ScrollViewScreen(model);
		view.setSize(800, 500);
		view.addMouseMotionListener(new ClickListener());
		view.addMouseListener(new PlaceTowerListener(buyTowerListeners));
		this.add(view);
		view.setUpTest();
		this.model = view.getGame();
		this.model.addObserver(this);

		player = model.getPlayer();

		// unit information
		informationPane = new JTextArea();
		informationPane.setSize(200, 200);
		informationPane.setLocation(820, 20);
		informationPane.setEditable(false);

		// player information
		playerPane = new JTextArea(500, 200);
		playerPane.setBounds(400, 620, 200, 500);
		playerPane.setMinimumSize(new Dimension(200, 500));
		playerPane.setEditable(false);

		// buttons
		addPurchaseTowerButton(new MacrophageBuilderTest(view.getGame()),
				"Buy Macrophage");
		addPurchaseTowerButton(new AntibodyBuilder(view.getGame()),
				"Buy Antibody");
		
		if(model.isMultiplayer()) {
			// make creep buttons
			addPurchaseCreepButton(new InfluenzaBuilder((MultiPlayer2)view.getGame()), "Buy Influenza");
		}

		//add the minimap pane
		minimap = new Minimap();
		minimap.setLocation(10, 520);

		// add panes
		this.add(informationPane);
		this.add(playerPane);
		this.add(minimap);

		MouseEdgeScroller mes = new MouseEdgeScroller(game, view);
		view.addMouseMotionListener(mes);
		StopScroller ss = new StopScroller(mes);
		game.addMouseMotionListener(ss);

		timer = new Timer(MILLISECONDS_BETWEEN_UPDATES, new MoveListener());
		timer.start();
	}

	/**
	 * Helper method which builds adds a purchaseTowerButton to the GUI.
	 * 
	 * @param tb
	 *            the TowerBuilder to facilitate construction
	 * @param buttonMessage
	 *            the message displayed by the button
	 */
	public void addPurchaseTowerButton(TowerBuilder tb, String buttonMessage) {
		JButton button = new JButton(buttonMessage);
		button.setSize(buyButtonWidth, buyButtonHeight);
		button.setLocation(buyTowerXCoord, buyTowerYCoord);
		buyTowerYCoord += buyButtonHeight;
		BuyTowerButtonListener btbl = new BuyTowerButtonListener(tb);
		buyTowerListeners.add(btbl);
		button.addActionListener(btbl);
		gameButtons.add(button);
		button.setForeground(Color.BLACK);
		button.setBackground(Color.WHITE);
		button.addKeyListener(kpl);
		this.add(button);
	}
	
	public void addPurchaseCreepButton(CreepBuilder cb, String buttonMessage) {
		JButton button = new JButton(buttonMessage);
		button.setSize(buyButtonWidth, buyButtonHeight);
		button.setLocation(buyCreepXCoord, buyCreepYCoord);
		buyTowerYCoord += buyButtonHeight;
		BuyCreepButtonListener bcbl = new BuyCreepButtonListener(cb);
		//buyTowerListeners.add(bcbl); TODO: consider adding container for creep listeners
		button.addActionListener(bcbl);
		gameButtons.add(button);
		button.setForeground(Color.BLACK);
		button.setBackground(Color.WHITE);
		this.add(button);
	}

	private void updateInformation(String info) {
		informationPane.setText(info);
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("Game ended");
		timer.stop();
		game.showPanel(new DefaultView(game));
	}	


	private class StopScroller extends MouseAdapter {
		private MouseEdgeScroller mes;

		public StopScroller(MouseEdgeScroller mes) {
			this.mes = mes;
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mes.stopMoving();
		}
	}

	private class MoveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			model.update(MILLISECONDS_BETWEEN_UPDATES / 1000.0);
			playerPane.setText(player.getInformation());
			view.repaint();
			minimap.repaint();

		}

	}

	private class MouseEdgeScroller extends MouseAdapter implements Runnable {

		// vars
		private volatile boolean movingUp = false, movingDown = false;
		private volatile boolean movingLeft = false, movingRight = false;
		private volatile boolean isRunning;
		private Thread thread;
		private volatile Rectangle theRect = new Rectangle();
		private int maxX;
		private int maxY;
		private Point upperLeft = new Point(0, 0);
		// scroll vars
		private int SCROLL_BORDER_SIZE = 50;
		private int SCROLL_PIXEL_AMOUNT = 5;
		private int SCROLL_THREAD_SLEEP_AMOUNT = 5;

		// debug bool -- set true when you wish to debug
		boolean debug = false;

		public MouseEdgeScroller(JFrame j, ScrollViewScreen p) {
			isRunning = false;
			BufferedImage img = p.getBackgroundImage();
			p.setBasePoint(upperLeft);
			maxX = img.getWidth() - p.getWidth();
			maxY = img.getHeight() - p.getHeight();

			theRect.setBounds(p.getLocation().x, p.getLocation().y,
					p.getWidth(), p.getHeight());
			System.out.println(theRect.toString());

		}
		public void stopMoving() {
			movingUp = movingDown = movingRight = movingLeft = false;
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			Integer theX = e.getX(), theY = e.getY();

			// check location
			if (e.getY() < SCROLL_BORDER_SIZE) {
				movingUp = true;
			} else {
				movingUp = false;
			}
			if (e.getY() > theRect.height - SCROLL_BORDER_SIZE) {
				movingDown = true;
			} else {
				movingDown = false;
			}
			if (e.getX() < SCROLL_BORDER_SIZE) {
				movingLeft = true;
			} else {
				movingLeft = false;
			}
			if (e.getX() > theRect.width - SCROLL_BORDER_SIZE) {
				movingRight = true;
			} else {
				movingRight = false;
			}

			// start thread if we should be moving and if it isn't alive
			if (movingUp || movingDown || movingRight || movingLeft) {
				if (!isRunning) {
					thread = new Thread(this);
					thread.start();
				}
			}

			// move the viewport
			if (debug) {
				System.out.println("MouseMoved : " + theX.toString() + " "
						+ theY.toString());
			}
		}

		@Override
		public void run() {
			// notify begin
			isRunning = true;

			while (movingUp || movingDown || movingLeft || movingRight) {
				// sleep the thread
				//*
				try {
					Thread.sleep(SCROLL_THREAD_SLEEP_AMOUNT);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//*/
				if (movingUp) {
					upperLeft.translate(0, -SCROLL_PIXEL_AMOUNT);
				}
				if (movingDown) {
					upperLeft.translate(0, SCROLL_PIXEL_AMOUNT);
				}
				if (movingLeft) {
					upperLeft.translate(-SCROLL_PIXEL_AMOUNT, 0);
				}
				if (movingRight) {
					upperLeft.translate(SCROLL_PIXEL_AMOUNT, 0);
				}

				//check too far
				if (upperLeft.getX() > maxX)
					upperLeft.x = maxX;

				if (upperLeft.getY() > maxY)
					upperLeft.y = maxY;
				// check negative
				if (upperLeft.getX() < 0) {
					upperLeft.x = 0;
				}
				if (upperLeft.getY() < 0) {
					upperLeft.y = 0;
				}

				// move the visible rect
				theRect.setLocation(upperLeft);
				//				label.scrollRectToVisible(theRect);

				// debug
				if (debug) {
					//					System.out.println(thePoint.toString());
					//					System.out.println(label.getVisibleRect().toString());
				}

			}

			// done
			isRunning = false;
		}
	}
	
	private class ClickListener extends MouseAdapter {

		@Override
		public void mouseMoved(MouseEvent e) {
			Point location = ScrollViewScreen.adjustPointToScreen(e.getPoint());
			updateInformation(view.hover(location));
		}
	}

	private class PlaceTowerListener extends MouseAdapter {

		// vars
		Vector<BuyTowerButtonListener> buyTowerListeners;

		public PlaceTowerListener(Vector<BuyTowerButtonListener> listeners) {
			buyTowerListeners = listeners;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// iterate through listeners to see if one is selected
			for (int z = 0; z < buyTowerListeners.size(); z++) {
				BuyTowerButtonListener bt = buyTowerListeners.get(z);
				if (bt.isSelected()) {
					if (bt.buildTower(ScrollViewScreen.adjustPointToScreen(e
							.getPoint()))
							&& (kpl.getLastKey() != KeyEvent.VK_SHIFT))
						clearSelected();
				}

			}
		}
	}

	private class KeyPressListener implements KeyListener {

		private int lastKey;

		@Override
		public void keyPressed(KeyEvent arg0) {
			lastKey = arg0.getKeyCode();
			System.out.println("Key pressed: " + lastKey);
			System.out.println("Shift is " + KeyEvent.VK_SHIFT);
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			lastKey = -1;
			System.out.println("Key released");
		}

		@Override
		public void keyTyped(KeyEvent arg0) {

		}

		public int getLastKey() {
			return lastKey;
		}

	}

	private class BuyCreepButtonListener implements ActionListener {

		// vars
		private CreepBuilder builder;
		
		public BuyCreepButtonListener(CreepBuilder cb) {
			builder = cb;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			builder.spawnCreep();
		}
		
	}

	private class BuyTowerButtonListener implements ActionListener {

		// vars
		private boolean currentlySelected = false;
		private TowerBuilder builder;

		public BuyTowerButtonListener(TowerBuilder tb) {
			builder = tb;
		}

		// gets
		public boolean isSelected() {
			return currentlySelected;
		}

		// methods
		public void clear() {
			currentlySelected = false;
		}

		public boolean buildTower(Point p) {
			return builder.buildTower(p);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// get button reference
			if (currentlySelected) {
				clearSelected();
				currentlySelected = false;
			} else {
				JButton buttonRef = (JButton) arg0.getSource();
				clearSelected();
				buttonRef.setForeground(Color.WHITE);
				buttonRef.setBackground(Color.BLACK);
				currentlySelected = true;
			}
		}
	}
}
