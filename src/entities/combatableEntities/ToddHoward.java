package entities.combatableEntities;

import graphics.Assets;
import states.MysteriousStranger;
import states.StateManager;
import utils.Preferences;

import java.util.Random;

/**
 * Created by Bobby on 4/1/2017.
 */
public class ToddHoward extends CombatableEntity {

	double maxSpd = 3 * Preferences.scale;

	public ToddHoward(int x, int y) {
		super(x, y, Assets.getEntityAnimation("toddhoward"), Assets.getEntityAnimation("toddhoward"));
		width = 241;
		height = 245;
	}

	@Override
	public void attack(int attackType) {
		super.attack(attackType);

		if(new Random().nextInt(100) > 98)
			StateManager.push(new MysteriousStranger());
	}

	@Override
	public void defeat() {
		//Win
	}

	@Override
	public void update() {
		super.update();

		float px = Player.getInstance().getXInPixels();
		float py = Player.getInstance().getYInPixels();
		int pwidth = Player.getInstance().getWidth();
		int pheight = Player.getInstance().getHeight();

		if(px+Player.getInstance().getWidth() < getXInPixels() && XSpd > -maxSpd){
			XSpd -= .2 * Preferences.scale;
		}
		else if(px > getXInPixels()+width && XSpd < maxSpd){
			XSpd += .2 * Preferences.scale;
		}

		if(!attacking && (getXInPixels() - (px+pwidth) < 10 || px - (getXInPixels()+width) < 10)){
			if(grounded && py < getYInPixels()-height/2)
				jump();
			attack(1);
		}
	}
}