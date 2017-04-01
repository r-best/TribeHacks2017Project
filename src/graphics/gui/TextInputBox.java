package graphics.gui;

import graphics.Assets;
import utils.KeyManager;
import utils.Preferences;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by Bobby on 1/21/2017.
 */
public class TextInputBox{

	private RoundRectangle2D parent;
	private float fontsize;
	private int maxLength; //-1 means no max length, type as many characters as you want
	private StringBuilder stringBuilder;

	public TextInputBox(RoundRectangle2D parent, int maxLength, float fontsize){
		this.parent = parent;
		this.fontsize = (float)(fontsize * Preferences.scale);
		this.maxLength = maxLength;
		this.stringBuilder = new StringBuilder();
	}
	public TextInputBox(RoundRectangle2D parent, int fontsize){
		this(parent, -1, fontsize);
	}

	public String getText(){
		return stringBuilder.toString();
	}

	public void setText(String text){
		clear();
		for(int i = 0; i < text.length(); i++){
			stringBuilder.append(text.charAt(i));
		}
	}

	public void clear(){
		stringBuilder.delete(0, stringBuilder.length());
	}

	public void update(){
		if(maxLength == -1 || stringBuilder.length() < maxLength) {
			for (int i = 0; i < KeyManager.getLength(); i++) {
				//Disable some keys that aren't printable/not used in this situation:
				if (i != 8 && //Backspace
						i != 27 && //Escape
						i != 10 && //Enter
						i != 16 && //Shift
						i != 37 && //Left
						i != 38 && //Up
						i != 39 && //Right
						i != 40 && //Down
						i != 20 && //Caps lock
						i != 17 && //Ctrl
						i != 18)   //Alt
					if (KeyManager.checkKeyAndReset(i)) {
						//Key press defaults to uppercase, so if shift is not pressed,
						//then convert to lowercase by adding 32 to the ASCII value
						if (!KeyManager.checkKeyWithoutReset(KeyEvent.VK_SHIFT)) {
							if (i >= 65 && i <= 90) //Any uppercase letter
								stringBuilder.appendCodePoint(i + 32); //Change to lowercase
							else
								stringBuilder.appendCodePoint(i);
						}
						else
							stringBuilder.appendCodePoint(convertASCIIToShiftHeldValue(i));
					}
			}
		}
		if(KeyManager.checkKeyAndReset(KeyEvent.VK_BACK_SPACE) && stringBuilder.length() > 0){
			stringBuilder.deleteCharAt(stringBuilder.length()-1);
		}
	}

	public void draw(Graphics2D graphics){
		graphics.setColor(Color.gray);
		graphics.fill(parent);
		graphics.setColor(Color.BLACK);
		graphics.setFont(Assets.timesNewRoman.deriveFont(fontsize));
		graphics.drawString(stringBuilder.toString(), (int)parent.getX()+10, (int)parent.getY()+50);
	}

	/**
	 * A large string of case statements determining what a given ASCII value should be
	 * changed to if the shift key is being held, all based entirely on my keyboard
	 * @param i the ASCII value
	 * @return
	 */
	public int convertASCIIToShiftHeldValue(int i){
		if(i >= 65 && i <= 90) //Any uppercase letter
			return i;
		if(i >= 97 && i <= 122) //Any lowercase letter (shouldn't be possible, but why not?)
			return i + 32;
		switch(i){
			case 96: //`
				return 126; //~
			case KeyEvent.VK_1:
				return 33; //!
			case KeyEvent.VK_2:
				return 64; //@
			case KeyEvent.VK_3:
				return 35; //#
			case KeyEvent.VK_4:
				return 36; //$
			case KeyEvent.VK_5:
				return 37; //%
			case KeyEvent.VK_6:
				return 94; //^
			case KeyEvent.VK_7:
				return 38; //&
			case KeyEvent.VK_8:
				return 42; //*
			case KeyEvent.VK_9:
				return 40; //(
			case KeyEvent.VK_0:
				return 41; //)
			case KeyEvent.VK_MINUS:
				return 95; //_
			case KeyEvent.VK_EQUALS:
				return 43; //+
			case KeyEvent.VK_OPEN_BRACKET:
				return 123; //{
			case KeyEvent.VK_CLOSE_BRACKET:
				return 125; //}
			case KeyEvent.VK_BACK_SLASH:
				return 124; //|
			case KeyEvent.VK_SEMICOLON:
				return 58; //:
			case 39: //'
				return 34; //"
			case 44: //,
				return 60; //<
			case 46: //.
				return 62; //>
			case 47: ///
				return 63; //?
			default:
				return i;
		}
	}
}