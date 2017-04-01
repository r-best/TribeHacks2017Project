package rooms;

import java.awt.*;
import java.util.ArrayList;
import entities.Entity;
import entities.EntityManager;

public class Room {

	protected boolean initialized = false;
	protected int width, height;
	protected Tile[][] tiles;
	private EntityManager entities;

	public void entityInit(int roomNum){
		entities = new EntityManager(roomNum);
		initialized = true;
	}

	public void update(){
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				tiles[i][j].update(i, j);
			}
		}

		entities.update();
	}

	public void draw(Graphics2D graphics){
//		int xStart = /*0;*/ (int)Math.min(0, Camera.getXOffset() / Tile.width);
//		int xEnd = /*width;*/ (int)Math.max(width, (Camera.getXOffset() - Game.getGameWidth()) / Tile.width + 1);
//		int yStart = /*0;*/ (int)Math.min(0, Camera.getYOffset() / Tile.height);
//		int yEnd = /*height;*/ (int)Math.max(height, (Camera.getYOffset() - Game.getGameHeight()) / Tile.height + 1);
//
//		for(int y = yStart; y < yEnd;y++){
//			for(int x = xStart; x < xEnd; x++){
//				getTile(x, y).draw(graphics, (int)(x * Tile.width + Camera.getXOffset()), (int)(y * Tile.height + Camera.getYOffset()));
//			}
//		}

		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				tiles[i][j].draw(graphics, i, j);
			}
		}

		entities.draw(graphics);
	}

	/**
	 * @param x X Tile coordinate of tile
	 * @param y Y Tile coordinate of tile
	 * @return the type of Tile present at that (X, Y) coordinate in the room.
	Returns grassTile if the coordinates are outside the room.
	 */
	public Tile getTile(int x, int y){
		if(x < 0 || y < 0 || x >= width || y >= height){
			return Tile.tileData[0];
		}

		/*Tile t = Tile.tileData[tiles[x][y]];
		if(t == null){
			return Tile.tileData[0];
		}
		return t;*/
		return tiles[x][y];
	}

	public ArrayList<Entity> getEntities(){
		return entities.getEntities();
	}

	public int getWidthInTiles(){
		return width;
	}
	public int getWidthInPixels(){ return width * Tile.width; }
	public int getHeightInTiles(){
		return height;
	}
	public int getHeightInPixels(){ return height * Tile.height; }
}