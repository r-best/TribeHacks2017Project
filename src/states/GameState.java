package states;

import graphics.Camera;
import rooms.RoomManager;

import java.awt.*;
import java.awt.event.KeyEvent;

import entities.Player;
import utils.KeyManager;

public class GameState implements State {

	public GameState(){
		RoomManager.init();
	}

	/**
	 * If the event queue is empty, updates the game (entities, movement, etc.)
	 * Else updates the frontmost event instead
	 */
	@Override
	public void update(){
		Player.getInstance().update();
		RoomManager.update();
	}

	/**
	 * Draws the game (entities, etc.)
	 * Also draws the frontmost event if there is one
	 */
	@Override
	public void draw(Graphics2D graphics){
		Camera.centerOnEntity(Player.getInstance());
		RoomManager.draw(graphics);
		Player.getInstance().draw(graphics);
	}

	@Override
	public void onEnter() {

	}

	@Override
	public void onExit() {

	}
}