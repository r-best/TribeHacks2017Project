package utils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener{

	private static boolean keys[];
	private static KeyManager instance = null;

	public static KeyManager getInstance(){
		return instance == null ? instance = new KeyManager() : instance;
	}

	private KeyManager(){
		keys = new boolean[200];
	}

	/**
	 * Returns whether or not a key is pressed.
	 * If true, resets the key to false so that it won't keep firing every frame
	 * (example: use with menu controls so a single key press doesn't scroll you all the way to the opposite end of the menu)
	 * @param key the key code of the key to check
	 */
	public static Boolean checkKeyAndReset(int key){
		if(keys[key]){
			keys[key] = false;
			return true;
		}
		return false;
	}

	/**
	 * Returns whether or not a key is pressed.
	 * DOES NOT reset the key to false
	 * (example: use with player movement controls so a continued key press is counted every frame and the player keeps walking)
	 * @param key the key code of the key to check
	 */
	public static Boolean checkKeyWithoutReset(int key){
		return keys[key];
	}

	/**
	 * Takes in a key code and if true, resets it to false
	 * @param key
	 */
	public static void reset(int key){
		if(keys[key])
			keys[key] = false;
	}

	public static int getLength(){ return keys.length; }

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		//System.out.println(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}