package events;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Bobby on 1/6/2017.
 */
public class EventQueue {
	private static ArrayList<Event> list = new ArrayList<>();


	public static void update(){
		peek().update();
	}

	public static void draw(Graphics2D graphics){
		peek().draw(graphics);
	}

	public static void add(Event e){
		list.add(e);
	}

	public static Event remove(){
		return list.remove(list.size()-1);
	}

	public static boolean remove(Event event){ return list.remove(event); }

	public static Event peek(){
		return list.size() > 0 ? list.get(list.size()-1) : null;
	}

	public static boolean isEmpty(){
		return list.size() == 0;
	}
}