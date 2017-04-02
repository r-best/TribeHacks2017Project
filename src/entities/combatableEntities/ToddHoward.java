package entities.combatableEntities;

import Game.Game;
import events.DialogueEvent;
import events.Stranger;
import events.ToddsConsole;
import graphics.Assets;
import rooms.Room;
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
		Random r = new Random();
		if(r.nextInt(100) > 90) {
			int chance = r.nextInt(100);
			if(chance <= 30)
				new Stranger(Game.getGameWidth() / 2, Game.getGameHeight() / 2).trigger();
			else
				generateConsoleCommand();
		}
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
			if(grounded && py < getYInPixels())
				jump();
			if(Math.abs(py - getYInPixels()) < 300)
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

	public void generateConsoleCommand() {
		String TODDSFUNCOMMAND = "";
		Random r = new Random();

		int token1Chance = r.nextInt(100);
		int token2Chance = r.nextInt(100);

		if (token1Chance <= 5){
			TODDSFUNCOMMAND += "event ";
			if(token2Chance >= 0 && token2Chance <= 100)
			TODDSFUNCOMMAND += "dialogue " + generateDialogue();
		}
		else if (token1Chance > 5 && token1Chance <= 32){
			TODDSFUNCOMMAND += "spawn ";
			int x = r.nextInt(RoomManager.getRoom().getWidthInTiles()-1);
			int y = r.nextInt(RoomManager.getRoom().getHeightInTiles()-1);
			int quantity = r.nextInt(300);
			TODDSFUNCOMMAND += "npc " + x + " " + y + " " + quantity;
		}
		else if (token1Chance > 32 && token1Chance <= 64){
			TODDSFUNCOMMAND += "gravity ";
			if(token2Chance <= 25)
				TODDSFUNCOMMAND += "restore";
			else if(token2Chance > 25 && token2Chance <= 50)
				TODDSFUNCOMMAND += "magnify " + (r.nextInt(4)-2);
			else if(token2Chance > 50 && token2Chance <= 75)
				TODDSFUNCOMMAND += "reverse";
			else
				TODDSFUNCOMMAND += "zero";
		}
		else {
			TODDSFUNCOMMAND += "player ";
			if(token2Chance <= 33)
				TODDSFUNCOMMAND += "flipcontrols";
			else if(token2Chance > 33 && token2Chance <= 66)
				TODDSFUNCOMMAND += "multiplyspeed " + r.nextInt(200)/100;
			else
				TODDSFUNCOMMAND += "multiplyjump " + r.nextInt(200)/100;
		}

		//StateManager.push(new ToddsConsole(TODDSFUNCOMMAND));
		new ToddsConsole(TODDSFUNCOMMAND).trigger();
	}

	public String generateDialogue(){
		return "Hello!";
	}
}