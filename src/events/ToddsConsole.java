package events;

import states.DebugConsole;
import utils.Preferences;

import java.awt.*;

/**
 * Created by Bobby on 4/1/2017.
 */
public class ToddsConsole extends DialogueEvent{

	private String command;
	private double timer;

	public ToddsConsole(String command){
		super(new String[]{command});
		this.command = command;
		timer = 100;
	}

	@Override
	public void update() {
		if((System.nanoTime() - lastChange) > Preferences.textSpd && !currentChars.equals(text[dialogueIndex])){
			currentChars += text[dialogueIndex].charAt(charIndex);
			charIndex++;
			lastChange = System.nanoTime();
		}

		if(currentChars.equals(text[dialogueIndex])){
			timer--;
		}

		if(timer <= 0){
			advanceText();
			timer = 100;
		}
	}
	@Override
	public void advanceText(){
		dialogueIndex++;
		charIndex = 0;
		currentChars = "";
		if(dialogueIndex == text.length) {
			new DebugConsole().parseInput(command);
			finish();
			dialogueIndex = 0;
		}
	}

	@Override
	public void draw(Graphics2D graphics) {
		super.draw(graphics);
	}
}