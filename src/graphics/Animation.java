package graphics;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Animation {
	private int index;
	private BufferedImage[] frames;

	public Animation(int speed, BufferedImage[] frames){
		this.frames = frames;
		index = 0;

		new Timer(speed, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				index++;
				if(index >= frames.length)
					index = 0;
			}
		}).start();
	}

	/**
	 * Takes in a 2D array of BufferedImages (an array of sets of frames, each set representing a separate animation,
	 * creates an array of animations from them and returns it
	 */
	public static Animation[] createAnimations(BufferedImage[][] frames){
		Animation[] anims = new Animation[2];
		anims[0] = new Animation(150, frames[0]);
		anims[1] = new Animation(150, frames[1]);
		return  anims;
	}

	public BufferedImage getCurrentFrame(){
		return frames[index];
	}

	public BufferedImage getFrame(int x){
		return frames[x];
	}
}