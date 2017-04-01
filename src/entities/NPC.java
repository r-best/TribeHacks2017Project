package entities;

import events.Event;
import graphics.Animation;
import graphics.Assets;
import utils.Preferences;

import java.util.ArrayList;

public class NPC extends Entity {

	public NPC(int x, int y, Animation[] anims, ArrayList<Event> events){
		super(x, y, anims, events);
	}

	public NPC(int x, int y, Animation[] anims){
		this(x, y, anims, null);
	}

	@Override
	public void update(){
		super.update();
	}
}