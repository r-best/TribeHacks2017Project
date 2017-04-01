package entities;

import graphics.Animation;
import graphics.Camera;
import rooms.RoomManager;
import rooms.Tile;
import events.Event;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import utils.KeyManager;
import utils.Preferences;

public abstract class Entity {
	protected Rectangle bounds;
	public int boundsXOffset, boundsYOffset; //used to shrink the bounds Rectangle into the entity, to reduce the size of the hitbox
	protected int width, height;
	public float x, y; //stored in PIXELS, not tileData
	protected double XSpd = 0, YSpd = -1;
	public boolean isSolid = true; //to determine if other entities can collide with this entity (true by default, subclass must define as false)

	public static final int DEFAULT_WIDTH = 50, DEFAULT_HEIGHT = 50;
	protected int direction;
	protected Animation anims[];//animUp, animDown;
	protected BufferedImage tempAnim, currentFrame;
	protected ArrayList<Event> events; //What happens when the player interacts with this entity

	protected boolean grounded = false;

	/**
	 * @param x starting X position in tileData
	 * @param y starting Y position in tileData
	 */
	public Entity(int x, int y, Animation[] anims, ArrayList<Event> events){
		this.x = x * Tile.width;
		this.y = y * Tile.height;
		this.width = (int)(DEFAULT_WIDTH*Preferences.scale);
		this.height = (int)(DEFAULT_HEIGHT*Preferences.scale);
		boundsXOffset = (int)(this.width*.3);
		boundsYOffset = (int)(this.height*.3);
		bounds = new Rectangle((int)(this.x + boundsXOffset), (int)(this.y + boundsYOffset), (int)(this.width*.4), (int)(this.height*.6));
		this.anims = anims;
		tempAnim = anims[0].getFrame(0);
		if(events != null)
			this.events = events;
		else
			this.events = new ArrayList<>();
	}
	public Entity(int x, int y, Animation[] anims){
		this(x, y, anims, null);
	}

	public void update(){
		currentFrame = getCurrentAnimationFrame();

		if(playerInRange() && events.size() != 0 && KeyManager.checkKeyWithoutReset(KeyEvent.VK_E)){
			KeyManager.reset(KeyEvent.VK_E);
			for(int i = events.size()-1; i >= 0; i--)
				events.get(i).trigger();
		}
		move();
	}

	public void draw(Graphics2D graphics){
		graphics.drawImage(currentFrame, (int)(x + Camera.getXOffset()), (int)(y + Camera.getYOffset()), null);
		//draw hitbox (for debugging)
		graphics.fillRect(bounds.x + (int)Camera.getXOffset(), bounds.y + (int)Camera.getYOffset(), bounds.width, bounds.height);
	}

	public void move(){
		if(XSpd != 0 || YSpd != 0) {
			moveX();
			moveY();
			XSpd = 0;
			if(!grounded)
				YSpd += .1;
			bounds.x = (int) x + boundsXOffset;
			bounds.y = (int) y + boundsYOffset;
		}
	}

	public void moveX(){
		if(XSpd > 0){
			int tempX = (int)(x + XSpd + boundsXOffset + bounds.width) / Tile.width;
			if(!collisionWithTile(tempX, (int)(y + boundsYOffset) / Tile.height) &&
					!collisionWithTile(tempX, (int)(y + boundsYOffset + bounds.height) / Tile.height)){
				grounded = false;
				bounds.x += XSpd;
				x += XSpd;
				if(collidesWithAnEntity()){
					x -= XSpd;
					bounds.x -= XSpd;
				}
			}
			else{
				x = tempX * Tile.width - boundsXOffset - bounds.width - 1;
			}
		}
		else if(XSpd < 0){
			int tempX = (int)(x + XSpd + boundsXOffset) / Tile.width;
			if(!collisionWithTile(tempX, (int)(y + boundsYOffset) / Tile.height) &&
					!collisionWithTile(tempX, (int)(y + boundsYOffset + bounds.height) / Tile.height)){
				grounded = false;
				bounds.x += XSpd;
				x += XSpd;
				if(collidesWithAnEntity()){
					x -= XSpd;
					bounds.x -= XSpd;
				}
			}
			else{
				x = tempX * Tile.width + Tile.width - boundsXOffset;
			}
		}
	}

	public void moveY(){
		if(YSpd < 0){
			int tempY = (int)(y + YSpd + boundsYOffset) / Tile.height;
			if(!collisionWithTile((int)(x + boundsXOffset) / Tile.width, tempY) &&
					!collisionWithTile((int)(x + boundsXOffset + bounds.width) / Tile.width, tempY)){
				bounds.y += YSpd;
				y += YSpd;
				grounded = false;
				if(collidesWithAnEntity()){
					y -= YSpd;
					bounds.y -= YSpd;
				}
			}
			else{
				y = tempY * Tile.height + Tile.height - boundsYOffset;
				YSpd = 0;
			}
		}
		else if(YSpd > 0){
			int tempY = (int)(y + YSpd + boundsYOffset + bounds.height) / Tile.height;
			if(!collisionWithTile((int)(x + boundsXOffset) / Tile.width, tempY) &&
					!collisionWithTile((int)(x + boundsXOffset + bounds.width) / Tile.width, tempY)){
				bounds.y += YSpd;
				y += YSpd;
				if(collidesWithAnEntity()){
					y -= YSpd;
					bounds.y -= YSpd;
				}
			}
			else{
				y = tempY * Tile.height - boundsYOffset - bounds.height - 1;
				grounded = true;
			}
		}
	}

	/**
	 * Moves an entity to the given (x, y) tile
	 * @param x x tile coord to move to
	 * @param y y tile coord to move to
	 */
	public void moveTo(int x, int y){
		setXInTiles(x);
		setYInTiles(y);
		bounds.x = (int) this.x + boundsXOffset;
		bounds.y = (int) this.y + boundsYOffset;
	}

	/**
	 * Tests to see if the entity intersects any of the other entities in the room,
	 then if it intersects the player
	 *@return true if any collision, false if none
	 */
	public boolean collidesWithAnEntity(){
		for(Entity ent : RoomManager.getRoom().getEntities()){
			if(this.bounds.intersects(ent.getBounds()) && ent != this){
				return ent.isSolid;
			}
		}
		if(this.collidesWith(Player.getInstance()) && !this.equals(Player.getInstance()))
			return true;
		return false;
	}

	/**
	 * @param ent any existing entity
	 * @return true if this intersects the passed entity
	 */
	public boolean collidesWith(Entity ent){
		if(this.bounds.intersects(ent.getBounds())){
			return true;
		}
		return false;
	}

	/**
	 * @return true if the player is within range of and facing towards the npc
	 */
	private boolean playerInRange(){
		float x = getXInPixels();
		float y = getYInPixels();

		float px = Player.getInstance().getXInPixels();
		float py = Player.getInstance().getYInPixels();
		int pwidth = Player.getInstance().getWidth();
		int pheight = Player.getInstance().getHeight();
		int pdirection = Player.getInstance().getDirection();

		//This code really sucks, I need to figure out a better way to detect if the player is in range

		//player is above & to left
		if((px > x - pwidth - 10 && px < x) &&
				(py > y - pheight - 10 && py < y)){
			//System.out.println("UP/LEFT");
			if(pdirection == 3 || pdirection == 0)
				return true;
		}
		//player is above & to right
		else if((px > x + width && px < x + width + pwidth) &&
				(py > y - pheight - 10 && py < y)){
			//System.out.println("UP/RIGHT");
			if(pdirection == 3 || pdirection == 2)
				return true;
		}
		//player is down & to left
		else if((px > x - pwidth - 10 && px < x) &&
				(py > y + height && py < y + height + pheight + 10)){
			//System.out.println("DOWN/LEFT");
			if(pdirection == 1 || pdirection == 0)
				return true;
		}
		//player is down & to right
		else if((px > x + width && px < x + width + pwidth) &&
				(py > y + height && py < y + height + pheight + 10)){
			//System.out.println("DOWN/RIGHT");
			if(pdirection == 1 || pdirection == 2)
				return true;
		}

		//player is above
		else if((px >= x && px <= x + width) &&
				(py >= y - pheight - 10 && py <= y)){
			//System.out.println("UP");
			if(pdirection == 3)
				return true;
		}
		//player is below
		else if((px >= x && px <= x + width) &&
				(py >= y + height && py <= y + height + pheight + 10)){
			//System.out.println("DOWN");
			if(pdirection == 1)
				return true;
		}
		//player is to the left
		else if((px >= x - pwidth - 10 && px <= x) &&
				(py >= y && py <= y + height)){
			//System.out.println("LEFT");
			if(pdirection == 0)
				return true;
		}
		//player is to the right
		else if((px >= x + width && px <= x + width + pwidth) &&
				(py >= y && py <= y + height)){
			//System.out.println("RIGHT");
			if(pdirection == 2)
				return true;
		}

		/*else{
			System.out.println("NONE");
		}*/
		return false;
	}

	/**
	 *
	 * @return the appropriate animation frame of the entity depending on its
	XSpd and YSpd (AKA which direction it is moving)
	 */
	public BufferedImage getCurrentAnimationFrame(){
		if(XSpd > 0){ //moving right
			tempAnim = anims[0].getFrame(0);
			direction = 0;
			return anims[0].getCurrentFrame();
		}
		else if(XSpd < 0){ //moving left
			tempAnim = anims[1].getFrame(0);
			direction = 1;
			return anims[1].getCurrentFrame();
		}
		else
			return tempAnim;
	}

	/**
	 *
	 * @param x X Tile coordinate of tile
	 * @param y Y Tile coordinate of tile
	 * @return the isSolid() value of the tile at the (X, Y) tile coordinate of the room
	 */
	public boolean collisionWithTile(int x, int y){
		return RoomManager.getRoom().getTile(x, y).isSolid();
	}
	public boolean collisionWithTile(Tile t){ return t.isSolid(); }

	public float getXInPixels(){
		return x;
	}
	public int getXInTiles(){
		return (int)(x /  Tile.width);
	}
	public void setXInPixels(float x){
		this.x = x;
	}
	public void setXInTiles(int x){
		this.x = x * Tile.width;
	}

	public float getYInPixels(){
		return y;
	}
	public int getYInTiles(){
		return (int)(y / Tile.height);
	}
	public void setYInPixels(float y){
		this.y = y;
	}
	public void setYInTiles(int y){
		this.y = y * Tile.height;
	}

	public Rectangle getBounds(){
		return bounds;
	}

	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}

	public int getDirection(){
		return direction;
	}
}