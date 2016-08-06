package view;

//imports

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.FriendlyMinimap;

/**
 * Constructs how the game works
 * 
 * @author Bhavana Gorti
 * @author Travis Woodrow
 * @author Michael Curtis
 * @author Patrick Martin
 * 
 */

public class Minimap extends JPanel {

	// vars
//	private volatile static Icon icon = new Icony();
//	private volatile static JLabel theLabel = new JLabel(icon);
	public static TexturePaint theTexture;
	private volatile static BufferedImage backgroundImage;

	// constructor
	public Minimap() {
//		loadBackground(imageName);
		
//		this.setSize(width, height);
//		System.out.println(this.getSize());
		makeScroller();
		// initial setup
//		this.imageName = imageName;
		//this.scroller = makeScroller(imageName);
		//setUpTest();

	}

	public void paintComponent(Graphics g) {
		//		long dur = System.nanoTime();
		super.paintComponent(g);
		//		System.out.println("PaintComponent called");
		Graphics2D gr = (Graphics2D) g;
		gr.drawImage(FriendlyMinimap.getMinimap(), 0, 0, null);
		//start with the background image, add creeps, towers, projectiles but not really that last one
		//		System.out.println("resetting");
	}

	public void makeScroller() {

		// load in image to use for background
//		loadBackground(fileName);
//		System.out.println("Entered makeScroller.");
		backgroundImage = FriendlyMinimap.getMinimap();
		theTexture = new TexturePaint(backgroundImage, new Rectangle(
				backgroundImage.getWidth(), backgroundImage.getHeight()));
		// create label
//		icon = new Icony();

//		theLabel = new JLabel(icon);
		this.setBounds(10, 10, 640, 480);

//		mes = new MouseEdgeScroller(this, getTexture().getImage());
		
//		this.addMouseMotionListener(mes);
//		scroller.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);
	}

	

	private static class Icony implements Icon {
		TexturePaint TEXTURE = theTexture;

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setPaint(TEXTURE);
			g2.fillRect(x, y, c.getWidth(), c.getHeight());
			g2.dispose();
		}

		@Override
		public int getIconWidth() {
			return backgroundImage.getWidth();
		}

		@Override
		public int getIconHeight() {
			return backgroundImage.getHeight();
		}
	};
}
