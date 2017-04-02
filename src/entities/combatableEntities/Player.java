package entities.combatableEntities;

import graphics.Assets;
import states.LoseState;
import states.StateManager;
import utils.KeyManager;
import utils.Preferences;
import java.awt.event.KeyEvent;

public class Player extends CombatableEntity {

	public static double SPEED = 3;
	public static int UpKey = KeyEvent.VK_W, DownKey = KeyEvent.VK_S, LeftKey = KeyEvent.VK_A, RightKey = KeyEvent.VK_D;

	private static Player instance = new Player();

	public static Player getInstance(){ return instance; }

	private Player() {
		super(0, 0, 50, 50, Assets.getEntityAnimation("player"), Assets.getEntityAnimation("playerAttack"));
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
		StateManager.push(new LoseState());
	}

	/**
	 * Sets the XSpd and YSpd of the controlled character depending on
	 what keys are being pressed
	 */
	public void setPlayerMovement(){
		if(grounded)
			if (KeyManager.checkKeyWithoutReset(UpKey) || KeyManager.checkKeyWithoutReset(KeyEvent.VK_SPACE))
				jump();

		if(KeyManager.checkKeyWithoutReset(LeftKey))
			XSpd = -SPEED * Preferences.scale;
		if(KeyManager.checkKeyWithoutReset(RightKey))
			XSpd = SPEED * Preferences.scale;

		if(KeyManager.checkKeyWithoutReset(KeyEvent.VK_SHIFT)){
			XSpd *= 2;
		}
	}
}