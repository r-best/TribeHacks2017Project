package entities.combatableEntities;

import Game.Game;
import events.Stranger;
import graphics.Assets;
import rooms.RoomManager;
import states.StateManager;
import states.WinState;
import utils.Preferences;

import java.awt.*;
import java.util.Random;

/**
 * Created by Bobby on 4/1/2017.
 */
public class ToddHoward extends CombatableEntity {

	private Rectangle toddHPBar, toddHPBarFill;
	double maxSpd = 3 * Preferences.scale;

	public ToddHoward(int x, int y) {
		super(x, y, 75, 75,/*241, 245,*/ Assets.getEntityAnimation("toddhoward"), Assets.getEntityAnimation("toddhoward"));

		toddHPBar = new Rectangle(
				(int)(Game.getGameWidth()*.2),
				(int)(Game.getGameHeight()*.01),
				(int)(Game.getGameWidth()*.6),
				(int)(Game.getGameHeight()*.03)
		);
		toddHPBarFill = new Rectangle(toddHPBar);
	}

	@Override
	public void attack(int attackType) {
		super.attack(attackType);

		if(new Random().nextInt(100) > 90)
			new Stranger(Game.getGameWidth()/2, Game.getGameHeight()/2).trigger();
	}

	@Override
	public void defeat() {
		//RoomManager.getRoom().getEntities().remove(this);
		StateManager.push(new WinState());
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

		if(!attacking && (Math.abs(getXInPixels() - (px+pwidth)) < 3 || Math.abs(px - (getXInPixels()+width)) < 3)){
			System.out.println("True");
			if(grounded && py < getYInPixels())
				jump();
			attack(1);
		}

		if(Player.getInstance().meleeHitbox != null && bounds.intersects(Player.getInstance().meleeHitbox))
			damage(3);
		if(meleeHitbox != null && meleeHitbox.intersects(Player.getInstance().getBounds()))
			Player.getInstance().damage(10);

		toddHPBarFill.setBounds(
				(int)(toddHPBar.x+toddHPBar.width*(((maxHP-currentHP)/maxHP)/2)),
				toddHPBar.y,
				(int)(toddHPBar.width-toddHPBar.width*((maxHP-currentHP)/maxHP)),
				toddHPBarFill.height
		);
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);

		g.setColor(Color.white);
		g.draw(toddHPBar);
		//Health Gradient
		GradientPaint health = new GradientPaint(0,0, new Color(128,30,30),0, 20,Color.red, true);
		g.setPaint(health);
		g.fill(toddHPBarFill);
	}
}