package states;

import java.awt.*;

/**
 * Created by Bobby on 4/1/2017.
 */
public class MysteriousStranger implements State{



	@Override
	public void update() {

	}

	@Override
	public void draw(Graphics2D graphics) {
		StateManager.peek(1).draw(graphics);

	}

	@Override
	public void onEnter() {
//		try {
//			Clip clip = AudioSystem.getClip();
//			AudioInputStream inputStream = AudioSystem.getAudioInputStream(Main.class.getResourceAsStream("src/resources/audio/stranger.wav"));
//			clip.open(inputStream);
//			clip.start();
//		} catch (LineUnavailableException e) {
//			e.printStackTrace();
//		} catch (UnsupportedAudioFileException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		try {
//			InputStream in = new FileInputStream("src/resources/audio/stranger.ogg");
//			AudioStream as = new AudioStream(in);
//			AudioPlayer.player.start(as);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void onExit() {

	}
}