package entities;

import graphics.Assets;
import utils.KeyManager;
import utils.Preferences;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Entity {

	private static Player instance = new Player();

	public static Player getInstance(){ return instance; }

	private Player() {
		super(0, 0, Assets.getEntityWalkingAnimation("player"));
	}

	@Override
	public void update(){
		super.update();
		setPlayerMovement();
		currentFrame = getCurrentAnimationFrame();
		move();
	}

	@Override
	public void draw(Graphics2D graphics) {
		super.draw(graphics);
	}

	/**
	 * Sets the XSpd and YSpd of the controlled character depending on
	 what keys are being pressed
	 */
	public void setPlayerMovement(){
		int W = KeyEvent.VK_W, A = KeyEvent.VK_A, S = KeyEvent.VK_S, D = KeyEvent.VK_D;
		int UP = KeyEvent.VK_UP, LEFT = KeyEvent.VK_LEFT, RIGHT = KeyEvent.VK_RIGHT;

		if(grounded)
			if (KeyManager.checkKeyWithoutReset(W) || KeyManager.checkKeyWithoutReset(UP))
				YSpd = -4 * Preferences.scale;

		if(KeyManager.checkKeyWithoutReset(A) || KeyManager.checkKeyWithoutReset(LEFT))
			XSpd = -3 * Preferences.scale;
		if(KeyManager.checkKeyWithoutReset(D) || KeyManager.checkKeyWithoutReset(RIGHT))
			XSpd = 3 * Preferences.scale;

		if(KeyManager.checkKeyWithoutReset(KeyEvent.VK_SHIFT)){
			XSpd *= 3;
		}
		/*if(keys[KeyEvent.VK_SHIFT] &&
				keys[KeyEvent.VK_SPACE]){
			XSpd *= 5;
			YSpd *= 5;
		}*/
	}
}