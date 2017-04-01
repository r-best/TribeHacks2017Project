package entities.combatableEntities;

import entities.Entity;
import graphics.Animation;
import graphics.Camera;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by Bobby on 4/1/2017.
 */
public abstract class CombatableEntity extends Entity{

	protected double maxHP = 100, currentHP = 100;
	protected Animation[] atkAnims;
	protected boolean attacking;
	protected int attackType;
	protected double meleeCooldown, damageCooldown, attackTimer;

	protected Rectangle meleeHitbox;

	public CombatableEntity(int x, int y, int width, int height, Animation[] anims, Animation[] atkAnims) {
		super(x, y, width, height, anims);
		this.atkAnims = atkAnims;
		attacking = false;
	}

	@Override
	public void update() {
		super.update();

		if(damageCooldown > 0)
			damageCooldown--;
		if(meleeCooldown > 0)
			meleeCooldown--;
		if(attackTimer > 0)
			attackTimer--;

		if(attacking){
			currentFrame = getCurrentAttackAnimationFrame();
			switch (attackType){
				case 1:
					if (direction == 0) {
						meleeHitbox.setLocation((int) (getXInPixels() + width / 2), (int) (getYInPixels() + height / 4));
					} else if (direction == 1)
						meleeHitbox.setLocation((int) (getXInPixels() - width / 2), (int) (getYInPixels() + height / 4));
					break;
			}
		}
		else
			currentFrame = getCurrentAnimationFrame();

		if(attackTimer <= 0){
			attacking = false;
			switch (attackType) {
				case 1:
					meleeHitbox = null;
					break;
			}
		}
	}

	@Override
	public void draw(Graphics2D graphics) {
		super.draw(graphics);
		if(meleeHitbox != null) {
			graphics.setColor(Color.MAGENTA);
			graphics.fill(new Rectangle((int)(meleeHitbox.x+Camera.getXOffset()), (int)(meleeHitbox.y+Camera.getYOffset()), meleeHitbox.width, meleeHitbox.height));
		}
	}

	public void attack(int attackType){
		switch (attackType) {
			case 1:
				if (meleeCooldown <= 0) {
					meleeCooldown = 50;
					if (direction == 0)
						meleeHitbox = new Rectangle((int) (getXInPixels() + width / 2), (int) (getYInPixels() + height / 4), width, height / 2);
					else if (direction == 1)
						meleeHitbox = new Rectangle((int) (getXInPixels() - width / 2), (int) (getYInPixels() + height / 4), width, height / 2);
					this.attackType = attackType;
					attacking = true;
					attackTimer = 37;
				}
				break;
		}
	}

	public abstract void defeat();

	public void damage(int damage){
		if(damageCooldown <= 0) {
			System.out.println("Damage!");
			currentHP -= damage;

			damageCooldown = 75;

			if (currentHP <= 0)
				defeat();
		}
	}

	public void heal(int heal){
		currentHP += heal;

		if(currentHP > maxHP)
			currentHP = maxHP;
	}

	public double getHealth(){ return currentHP; }

	public BufferedImage getCurrentAttackAnimationFrame(){
		if(direction == 0){ //moving right
			return atkAnims[0].getCurrentFrame();
		}
		else if(direction == 1){ //moving left
			return atkAnims[1].getCurrentFrame();
		}
		return null;
	}
}
