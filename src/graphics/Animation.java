package graphics;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Animation {
	protected int index;
	protected BufferedImage[] frames;

	protected Animation(int speed, BufferedImage[] frames){
		this.frames = frames;
		index = 0;

		new Timer(speed, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				index++;
				if(index >= frames.length) {
					index = 0;
				}
			}
		}).start();
	}

	public void reset(){
		index = 0;
	}

	/**
	 * Takes in a 2D array of BufferedImages (an array of sets of frames, each set representing a separate animation,
	 * creates an array of animations from them and returns it
	 */
	public static Animation[] createAnimations(BufferedImage[][] frames){
		Animation[] anims = new Animation[frames.length];
		for(int i = 0; i < frames.length; i++)
			anims[i] = new Animation(150, frames[i]);
		return anims;
	}

	public BufferedImage getCurrentFrame(){
		return frames[index];
	}

	public BufferedImage getFrame(int x){
		return frames[x];
	}
}