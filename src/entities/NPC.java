package entities;

import events.Event;
import graphics.Animation;
import graphics.Assets;
import utils.Preferences;

import java.util.ArrayList;
import java.util.Random;

public class NPC extends Entity {

	private float movingTimer;
	private boolean direction, wander;

	public NPC(int x, int y, ArrayList<Event> events, boolean wander){
		super(x, y, 50, 50, Assets.getEntityAnimation("npc"), events);
		this.wander = wander;
	}

	public NPC(int x, int y){
		this(x, y, null, true);
	}

	@Override
	public void update(){
		super.update();

		if(wander) {
			Random r = new Random();

			if (movingTimer <= 0 && r.nextInt(100) > 98) {
				movingTimer = r.nextInt(100);
				if (r.nextInt(100) > 50)
					direction = !direction;
			}

			if (movingTimer > 0) {
				if (direction)
					XSpd = 3 * Preferences.scale;
				else
					XSpd = -3 * Preferences.scale;
				movingTimer--;
			}
		}
	}
}