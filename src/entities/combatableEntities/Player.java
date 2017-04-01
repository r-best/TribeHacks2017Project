package entities.combatableEntities;

import graphics.Assets;
import utils.KeyManager;
import utils.Preferences;
import java.awt.event.KeyEvent;

public class Player extends CombatableEntity {

	private static Player instance = new Player();

	public static Player getInstance(){ return instance; }

	private Player() {
		super(0, 0, Assets.getEntityAnimation("player"), Assets.getEntityAnimation("playerAttack"));
	}

	@Override
	public void update(){
		super.update();
		setPlayerMovement();
		if(attacking)
			currentFrame = getCurrentAttackAnimationFrame();
		else
			currentFrame = getCurrentAnimationFrame();
		if(!attacking && KeyManager.checkKeyAndReset(KeyEvent.VK_E))
			attack(1);


	}

	@Override
	public void defeat() {
		System.out.println("Game over");
	}

	/**
	 * Sets the XSpd and YSpd of the controlled character depending on
	 what keys are being pressed
	 */
	public void setPlayerMovement(){
		int W = KeyEvent.VK_W, A = KeyEvent.VK_A, S = KeyEvent.VK_S, D = KeyEvent.VK_D;
		int UP = KeyEvent.VK_UP, LEFT = KeyEvent.VK_LEFT, RIGHT = KeyEvent.VK_RIGHT;

		if(grounded)
			if (KeyManager.checkKeyWithoutReset(W) || KeyManager.checkKeyWithoutReset(UP) || KeyManager.checkKeyWithoutReset(KeyEvent.VK_SPACE))
				jump();

		if(KeyManager.checkKeyWithoutReset(A) || KeyManager.checkKeyWithoutReset(LEFT))
			XSpd = -3 * Preferences.scale;
		if(KeyManager.checkKeyWithoutReset(D) || KeyManager.checkKeyWithoutReset(RIGHT))
			XSpd = 3 * Preferences.scale;

		if(KeyManager.checkKeyWithoutReset(KeyEvent.VK_SHIFT)){
			XSpd *= 2;
		}
	}
}