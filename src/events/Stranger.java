package events;

import entities.Projectile;
import entities.combatableEntities.Player;
import graphics.Assets;
import rooms.RoomManager;
import rooms.Tile;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Created by Bobby on 4/1/2017.
 */
public class Stranger extends Event{
	private BufferedImage stranger;
	private int x, y, rotation, delay;
	private double scalex, scaley;

	public Stranger(int x, int y){
		super();
		stranger = Assets.loadImage("/resources/stranger.png");
		this.x = x;
		this.y = y;
	}

	@Override
	public void trigger() {
		super.trigger();
		rotation = 0;
		scalex = 0.0001;
		scaley = 0.0001;
	}

	@Override
	public void update() {
		if(delay == 0) {
			rotation += 25;
			scalex += .045;
			scaley += .045;

			if (scalex >= 1.5 || scaley >= 1.5) {
				delay = 199;
				rotation = 0;
			}
		}
		else if(delay > 0)
			delay-=2;
		else {
			//RoomManager.getRoom().addEntity(new Projectile((x + stranger.getWidth() / 2)/ Tile.width, (y + stranger.getHeight() / 2)/Tile.height));
			Player.getInstance().directDamage(40);
			finish();
		}
	}

	@Override
	public void draw(Graphics2D graphics) {
		AffineTransform scale = AffineTransform.getScaleInstance(scalex, scaley);
		AffineTransformOp opScale = new AffineTransformOp(scale, AffineTransformOp.TYPE_BILINEAR);

		AffineTransform rotate = AffineTransform.getRotateInstance(Math.toRadians(rotation), stranger.getWidth()/2, stranger.getHeight()/2);
		AffineTransformOp opRotate = new AffineTransformOp(rotate, AffineTransformOp.TYPE_BILINEAR);

		BufferedImage sprite = opScale.filter(opRotate.filter(stranger, null), null);
		graphics.drawImage(sprite, x+sprite.getWidth()/2, y+sprite.getHeight()/2, null);
	}
}
