package events;

import java.awt.*;

/**
 * Created by Bobby on 11/17/2016.
 */
public abstract class Event {
	public Event(){

	}

	public void trigger(){
		EventQueue.add(this);
	}

	public void finish(){
		EventQueue.remove(this);
	}

	public abstract void update();

	public abstract void draw(Graphics2D graphics);
}