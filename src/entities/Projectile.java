package entities;

import Game.Game;
import entities.combatableEntities.Player;
import graphics.Animation;
import graphics.Assets;
import rooms.Room;
import rooms.RoomManager;

import java.awt.*;

import static Game.Game.getGameWidth;

/**
 * Created by Bobby on 4/1/2017.
 */
public class Projectile extends Entity{

	public Projectile(int x, int y) {
		super(x, y, 10, 10, Assets.getEntityAnimation("bullet"));
	}

	@Override
	public void update() {
		super.update();

		double targetSpeed = 30;

		double playerX = Game.getGameWidth()/2;
		double playerY = Game.getGameHeight()/2;
		double c = Math.sqrt(Math.pow(playerX, 2) + Math.pow(playerY, 2));

		double ratio = c / targetSpeed;

		XSpd = playerX / ratio;
		YSpd = playerY / ratio;

		if(collidesWith(Player.getInstance())) {
			Player.getInstance().damage(10);
			RoomManager.getRoom().getEntities().remove(this);
		}
	}

	@Override
	public void draw(Graphics2D graphics) {
		super.draw(graphics);
	}
}