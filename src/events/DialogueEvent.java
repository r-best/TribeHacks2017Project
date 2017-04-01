package events;

import Game.Game;
import utils.KeyManager;
import utils.Preferences;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Bobby on 11/17/2016.
 */
public class DialogueEvent extends Event{
	private String[] text;
	private int dialogueIndex, charIndex;
	private String currentChars;
	private long lastChange;

	public DialogueEvent(String text[]){
		super();
		this.text = text;
		currentChars = text[0].charAt(0)+"";
		charIndex = 1;
		lastChange = System.nanoTime();
	}

	@Override
	public void trigger(){
		super.trigger();
	}

	@Override
	public void update(){
		if((System.nanoTime() - lastChange) > Preferences.textSpd && !currentChars.equals(text[dialogueIndex])){
			currentChars += text[dialogueIndex].charAt(charIndex);
			charIndex++;
			lastChange = System.nanoTime();
		}

		if(KeyManager.checkKeyAndReset(KeyEvent.VK_E)){
			if(currentChars.equals(text[dialogueIndex]))
				advanceText();
			else
				currentChars = text[dialogueIndex];
		}
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(
				(int)(Game.getGameWidth()*.01),
				(int)(Game.getGameHeight()*.69),
				(int)(Game.getGameWidth()*.98),
				(int)(Game.getGameHeight()*.3));
		graphics.setColor(Color.white);

		int maxLineLength = (int)(Game.getGameWidth() * .96);
		int maxLines = (int)(Game.getGameHeight() * .3) / (graphics.getFont().getSize()+20);

		//Woo functional text wrapping!
		String[] words = currentChars.split(" "); //Split the current string on spaces to get words (we want to wrap in between words, not in the middle of one)
		ArrayList<String> lines = new ArrayList<>();
		int jtemp = 0; //current index in the list of words, inner for loop starts at it
		for(int i = 0; i < maxLines; i++) {
			String result = "";
			for(int j = jtemp; j < words.length; j++) {
				String temp = result + " " + words[j];
				if (graphics.getFontMetrics().stringWidth(temp) <= maxLineLength) //If the line is less than max length accept it and continue adding words
					result = temp;
				else //else break, put string into line list, and start filling next line
					break;
				jtemp = j+1;
			}
			lines.add(result);
			if(jtemp >= words.length)
				break;
		}

		//Overflow out of box, we want to move it into a new box to be shown after the current full one
		if(jtemp <= words.length-1){
			//Trim overflow from full box
			String temp = "";
			for(int i = 0; i < jtemp; i++){
				temp += words[i] + " ";
			}
			text[dialogueIndex] = temp;
			currentChars = temp;
			//Resize array of boxes
			text = Arrays.copyOf(text, text.length+1);
			text[text.length-1] = "";
			//Shift all current queued boxes down one to make room for new overflow box
			for(int j = text.length-1; j > dialogueIndex+1; j--){
				temp = text[j];
				text[j] = text[j-1];
				text[j-1] = temp;
			}
			//Get overflow and store in temp variable
			temp = "";
			for(int i = jtemp; i < words.length; i++){
				temp += words[i] + " ";
			}
			//Assign overflow into the empty box
			text[dialogueIndex+1] = temp;
		}

		//Draw all the lines
		int y = (int)((Game.getGameHeight()*.7)+graphics.getFont().getSize());
		for(String s : lines){
			graphics.drawString(
					s,
					(int)(Game.getGameWidth()*.02),
					y);
			y += graphics.getFont().getSize()+20;
		}
	}

	public void advanceText(){
		dialogueIndex++;
		charIndex = 0;
		currentChars = "";
		if(dialogueIndex == text.length) {
			finish();
			dialogueIndex = 0;
		}
	}
}