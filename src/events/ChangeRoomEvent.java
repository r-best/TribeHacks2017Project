package events;

import rooms.RoomManager;

import java.awt.*;

/**
 * Created by Bobby on 12/5/2016.
 */
public class ChangeRoomEvent extends Event{
	private int destinationRoom, destPlayerX, destPlayerY;

	public ChangeRoomEvent(int destinationRoom, int destPlayerX, int destPlayerY){
		this.destinationRoom = destinationRoom;
		this.destPlayerX = destPlayerX;
		this.destPlayerY = destPlayerY;
	}

	@Override
	public void trigger(){
		super.trigger();
		RoomManager.setRoom(destinationRoom, destPlayerX, destPlayerY);
		finish();
	}

	@Override
	public void update(){

	}

	@Override
	public void draw(Graphics2D graphics){

	}
}
