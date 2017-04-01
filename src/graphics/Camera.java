package graphics;

import entities.Entity;
import rooms.RoomManager;
import Game.Game;

/**
 * Created by Bobby on 10/13/2016.
 */
public class Camera{
	private static float xoffset, yoffset;

	public static void centerOnEntity(Entity ent){
		xoffset =  Game.getGameWidth() / 2 - ent.getWidth() / 2 - ent.getXInPixels();
		yoffset =  Game.getGameHeight() / 2 - ent.getHeight() / 2 - ent.getYInPixels();
	}

	public void move(float x, float y){
		xoffset += x;
		yoffset += y;
	}

	public static float getXOffset(){
		return xoffset;
	}
	public static float getYOffset(){
		return yoffset;
	}
}