package entities.combatableEntities;

import entities.Entity;
import graphics.Animation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by Bobby on 4/1/2017.
 */
public abstract class CombatableEntity extends Entity{

	protected int maxHP = 100, currentHP = 100;
	protected Animation[] atkAnims;
	protected boolean attacking;
	protected int attackType;

	protected Rectangle meleeHitbox;

	public CombatableEntity(int x, int y, Animation[] anims, Animation[] atkAnims) {
		super(x, y, anims);
		this.atkAnims = atkAnims;
		attacking = false;
	}

	@Override
	public void update() {
		super.update();
		if(attacking){
			currentFrame = getCurrentAttackAnimationFrame();
			switch (attackType){
				case 1:
					if(direction == 0)
						meleeHitbox.setLocation((int)(getXInPixels()+width/2), (int)(getYInPixels()+height/4));
					else if(direction == 1)
						meleeHitbox.setLocation((int)(getXInPixels()-width/2), (int)(getYInPixels()+height/4));
					break;
			}
		}
		else
			currentFrame = getCurrentAnimationFrame();
	}

	public void attack(int attackType){
		attacking = true;
		switch (attackType){
			case 1:
				if(direction == 0)
					meleeHitbox = new Rectangle((int)(getXInPixels()+width/2), (int)(getYInPixels()+height/4), width, height/2);
				else if(direction == 1)
					meleeHitbox = new Rectangle((int)(getXInPixels()-width/2), (int)(getYInPixels()+height/4), width, height/2);
				break;
		}

		Timer t = new Timer(900, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				attacking = false;
				switch (attackType){
					case 1:
						meleeHitbox = null;
						break;
				}
			}
		});
		t.setRepeats(false);
		t.start();
	}

	public abstract void defeat();

	public void damage(int damage){
		currentHP -= damage;

		if(currentHP <= 0)
			defeat();
	}

	public void heal(int heal){
		currentHP += heal;

		if(currentHP > maxHP)
			currentHP = maxHP;
	}

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
