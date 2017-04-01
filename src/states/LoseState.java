package states;

import Game.Game;

import java.awt.*;

/**
 * Created by Bobby on 4/1/2017.
 */
public class LoseState implements State{
	@Override
	public void update() {

	}

	@Override
	public void draw(Graphics2D g) {
		g.setFont(g.getFont().deriveFont(80f));
		String str = "Congratulations, You Lose!";
		g.drawString(str, Game.getGameWidth()/2-g.getFontMetrics().stringWidth(str)/2, Game.getGameHeight()/2-g.getFontMetrics().getHeight()/2);
	}

	@Override
	public void onEnter() {

	}

	@Override
	public void onExit() {

	}
}
