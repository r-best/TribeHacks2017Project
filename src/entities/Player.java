package entities;

import graphics.Assets;
import graphics.Camera;
import rooms.RoomManager;
import utils.KeyManager;
import utils.Preferences;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Player extends Entity {

	private static Player instance = new Player();
	private boolean acting = false; //true if the player is attacking, cannot move while true

	public static Player getInstance(){ return instance; }

	private Player() {
		super(0, 0, Assets.getEntityWalkingAnimation("player"));
	}

	@Override
	public void update(){
		super.update();
		setPlayerMovement();
		currentFrame = getCurrentAnimationFrame();

		if (KeyManager.checkKeyAndReset(KeyEvent.VK_1)) {
			acting = true;
			Timer t = new Timer(100, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					acting = false;
				}
			});
			t.setRepeats(false);
			t.start();
		}
		move();
	}

	@Override
	public void draw(Graphics2D graphics) {
		super.draw(graphics);
		if(acting)
			graphics.fill(new Rectangle((int)(x+width+Camera.getXOffset()), (int)(y+Camera.getYOffset()), width, height));
	}

	/**
	 * Sets the XSpd and YSpd of the controlled character depending on
	 what keys are being pressed
	 */
	public void setPlayerMovement(){
		int W = KeyEvent.VK_W, A = KeyEvent.VK_A, S = KeyEvent.VK_S, D = KeyEvent.VK_D;
		int UP = KeyEvent.VK_UP, DOWN = KeyEvent.VK_DOWN, LEFT = KeyEvent.VK_LEFT, RIGHT = KeyEvent.VK_RIGHT;

		if(KeyManager.checkKeyWithoutReset(W) || KeyManager.checkKeyWithoutReset(UP))
			YSpd = -3 * Preferences.scale;
		if(KeyManager.checkKeyWithoutReset(S) || KeyManager.checkKeyWithoutReset(DOWN))
			YSpd = 3 * Preferences.scale;
		if(KeyManager.checkKeyWithoutReset(A) || KeyManager.checkKeyWithoutReset(LEFT))
			XSpd = -3 * Preferences.scale;
		if(KeyManager.checkKeyWithoutReset(D) || KeyManager.checkKeyWithoutReset(RIGHT))
			XSpd = 3 * Preferences.scale;

		if(KeyManager.checkKeyWithoutReset(KeyEvent.VK_SHIFT)){
			XSpd *= 3;
			YSpd *= 3;
		}
		/*if(keys[KeyEvent.VK_SHIFT] &&
				keys[KeyEvent.VK_SPACE]){
			XSpd *= 5;
			YSpd *= 5;
		}*/
	}
}