package model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class FriendlyMinimap {

	private static Block[][] blocks;
	private static BufferedImage baseimage;
	private static final int DIM_OF_BLOCK = 2;
	private static BufferedImage minimap; 
	private static int BLOCK_SIZE;
	private static final int DEFAULT_COLOR = Color.BLACK.getRGB();
	
	public FriendlyMinimap(Block[][] blocks) {
//		System.out.println("Minimap set");
		FriendlyMinimap.blocks = blocks;
		constructMinimap();
//		this.baseimage = baseimage;
//		this.BLOCK_SIZE = blockSize;
//		minimap = BufferedImage.
		
	}
	
	public static void setBackground(BufferedImage background) {
		baseimage = background;
		BLOCK_SIZE = baseimage.getWidth() / blocks.length;
	}
	
	public static Dimension getMinimapSize() {
		return new Dimension(minimap.getWidth(), minimap.getHeight());
	}
	
	public static void constructMinimap() {
//		System.out.println("Constructing minimap");
		minimap = new BufferedImage(blocks.length * DIM_OF_BLOCK, blocks[0].length * DIM_OF_BLOCK, BufferedImage.TYPE_3BYTE_BGR);
		for(int i=0;i<blocks.length;i++) {
			for(int j=0;j<blocks[0].length;j++) {
				/*
				 * The heirarchy is:
				 * If it has a turret, then color it the turret color
				 * If it has
				 */
				if(!blocks[i][j].getTowers().isEmpty()) {
					setBlockColor(i*DIM_OF_BLOCK, j*DIM_OF_BLOCK, Tower.COLOR);
				} else if(!blocks[i][j].getCreeps().isEmpty()) {
					setBlockColor(i*DIM_OF_BLOCK, j*DIM_OF_BLOCK, Creep.COLOR);
				}
				else if(baseimage != null){
					setBlockColor(i*DIM_OF_BLOCK, j*DIM_OF_BLOCK, baseimage.getRGB(i * BLOCK_SIZE, j*BLOCK_SIZE));
				} else {
					setBlockColor(i*DIM_OF_BLOCK, j*DIM_OF_BLOCK, DEFAULT_COLOR);
				}
			}
		}
	}
	
	private static void setBlockColor(int x, int y, int color) {
		for(int i=0;i<DIM_OF_BLOCK;i++) {
			for(int j=i;j<DIM_OF_BLOCK;j++) {
			minimap.setRGB(x+j, y, color);
			minimap.setRGB(x, y+j, color);
			}
		}
	}
	
	public static BufferedImage getMinimap() {
		constructMinimap();
//		System.out.println("Image: " + minimap);
		return minimap;
	}
	
	
	
}
