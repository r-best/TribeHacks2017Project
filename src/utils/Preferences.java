package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Preferences {
	public static int gameheight, gamewidth;
	public static long textSpd;
	public static double scale;

	/**
	 * Loads the game preferences from the config file
	 */
	public static void loadPrefs(){
		String[] prefs = loadFileAsString("config.txt").split("\n");
		for(int i = 0; i < prefs.length; i++){
			String[] str = prefs[i].split("=");
			switch(str[0]){
				case "gameheight":
					gameheight = Integer.parseInt(str[1]);
					break;
				case "gamewidth":
					gamewidth = Integer.parseInt(str[1]);
					break;
				case "textSpd":
					switch(str[1]){
						case "slow":
							textSpd = 500000000L;
							break;
						case "medium":
							textSpd = 250000000L;
							break;
						case "fast":
							textSpd = 100000000L;
							break;
						case "warpspeed":
							textSpd = 10000000L;
							break;
					}
					break;
				case "scale":
					scale = Double.parseDouble(str[1]);
					break;
				default:
					break;
			}
		}
		validatePreferences();
	}

	/**
	 * Validates the preferences loaded from the config file to make sure all
	 * variables have been assigned a value properly
	 */
	public static void validatePreferences(){
		if(gamewidth == 0 || gameheight == 0){
			gamewidth = 1600;
			gameheight = 1200;
		}
		if(textSpd == 0)
			textSpd = 250000000L;
		if(scale == 0) {
			//Regression equation to get scale factor from area of game window
			//Determined by what scale factors I thought looked good at different resolutions
			scale = 0.402291 * Math.log(.0000119942 * gamewidth * gameheight);
			if (scale < 1)
				scale = 1;
		}
	}

	public static String loadFileAsString(String path){
		StringBuilder builder = new StringBuilder();

		try{
			File file = new File(path);
			if(!file.exists())
				file.createNewFile();

			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			while((line = br.readLine()) != null){
				builder.append(line + "\n");
			}
			br.close();
		} catch(IOException e){
			e.printStackTrace();
		}
		return builder.toString();
	}
}