package states;

import Game.Game;
import graphics.Camera;

import java.awt.*;

import rooms.RoomManager;

import entities.combatableEntities.Player;

public class GameState implements State {

	private Rectangle healthMeter, healthMeterFill;
	int test = 100;

	public GameState(){
		RoomManager.init();

		healthMeter = new Rectangle(
				(int)(Game.getGameWidth()*.01),
				(int)(Game.getGameHeight()*.01),
				(int)(Game.getGameWidth()*.05),
				(int)(Game.getGameHeight()*.98)
		);
		healthMeterFill = new Rectangle(healthMeter);
	}

	/**
	 * Updates the game (entities, movement, etc.)
	 */
	@Override
	public void update(){
		Player.getInstance().update();
		RoomManager.update();
		healthMeterFill.setBounds(
				healthMeterFill.x,
				healthMeter.y+(int)(healthMeter.height*((100-Player.getInstance().getHealth()))/100),
				healthMeterFill.width, (int)(healthMeter.height*(Player.getInstance().getHealth()/100))
		);
	}

	/**
	 * Draws the game (entities, etc.)
	 */
	@Override
	public void draw(Graphics2D g){
		Camera.centerOnEntity(Player.getInstance());
		RoomManager.draw(g);
		Player.getInstance().draw(g);

		g.setColor(Color.white);
		g.draw(healthMeter);
		//Health Gradient
		GradientPaint health = new GradientPaint(0,0, new Color(30,128,30),60, 0,Color.green, true);
		g.setPaint(health);
		g.fill(healthMeterFill);
	}

	@Override
	public void onEnter() {

	}

	@Override
	public void onExit() {

	}
}