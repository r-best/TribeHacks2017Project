package states;

import Game.Game;
import entities.Entity;
import entities.NPC;
import entities.combatableEntities.Player;
import entities.combatableEntities.ToddHoward;
import events.DialogueEvent;
import graphics.Assets;
import graphics.gui.TextInputBox;
import rooms.RoomManager;
import utils.KeyManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class DebugConsole implements State{

	private TextInputBox textBox;
	private static ArrayList<String> history;
	private int historyPos; //keeps track of where in the history list you are, -1 means not in history

	private String message; //Message that shows up on completion of a command (ex: "Command not found")
	private int msgAlpha; //Alpha value of the message, slowly fades out until message is gone

	public DebugConsole(){
		RoundRectangle2D rect = new RoundRectangle2D.Double(
				(int)(Game.getGameWidth()*.01),
				(int)(Game.getGameHeight()*.69),
				(int)(Game.getGameWidth()*.98),
				(int)(Game.getGameHeight()*.3), 50, 50);
		this.textBox = new TextInputBox(rect, 30);
		if(history == null)
			history = new ArrayList<>();
		historyPos = -1;
	}

	public void parseInput(String str){
		String[] tokens = str.split("\\s+");
		switch(tokens[0]){
			case "settargetfps":
				if(tokens.length < 2){
					showMessage("Format: settargetfps [FPS]");
					break;
				}
				Game.setTargetFPS(Integer.parseInt(tokens[1]));
				showMessage("Target FPS set to " + tokens[1]);
				break;
			case "event":
				if(tokens.length < 2){
					showMessage("Format: event [EVENTTYPE] [EVENTARGS...]");
					break;
				}
				switch(tokens[1]){
					case "dialogue":
						if(tokens.length < 3){
							showMessage("Format: event dialogue [DIALOGUETEXT]");
							break;
						}
						String[] text = new String[tokens.length-2];
						for(int i = 2; i < tokens.length; i++) {
							text[i-2] = tokens[i];
						}
						new DialogueEvent(text).trigger();
						break;
					default:
						showMessage("Event '" +tokens[1]+ "' not recognized");
						break;
				}
				break;
			case "spawn":
				if(tokens.length < 4){
					showMessage("Format: spawn [ENTITY] [X] [Y] [QUANTITY]");
					break;
				}
				if(!(StateManager.peek(1) instanceof GameState)){
					showMessage("Must be in game to use this command");
					break;
				}
				int quantity = 1;
				if(tokens.length >= 5)
					quantity = Integer.parseInt(tokens[4]);
				for(int i = 0; i < quantity; i++){
					switch (tokens[1]) {
						case "npc":
							RoomManager.getRoom().getEntities().add(new NPC(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]) - i*2));
							showMessage("Spawned " + tokens[1]);
							break;
						case "todd":
							RoomManager.getRoom().getEntities().add(new ToddHoward(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]) - i*2));
							showMessage("Spawned " + tokens[1]);
							break;
						default:
							showMessage("Entity '" + tokens[1] + "' not recognized");
							break;
					}
				}
				break;
			case "gravity":
				if(tokens.length < 2){
					showMessage("Format: gravity [arg]");
					break;
				}
				switch (tokens[1]){
					case "restore":
						Entity.gravity = 0.4;
						break;
					case "magnify":
						if(tokens.length < 3){
							showMessage("Format: gravity magnify [MULTIPLIER]");
							break;
						}
						Entity.gravity *= Double.parseDouble(tokens[2]);
						break;
					case "reverse":
						Entity.gravity = -Entity.gravity;
						break;
					case "zero":
						Entity.gravity = 0;
						break;
					default:
						showMessage("Token '" + tokens[1] + "' not recognized");
						break;
				}
				break;
			case "player":
				if(tokens.length < 2){
					showMessage("Format: player [arg]");
					break;
				}
				switch (tokens[1]){
					case "flipcontrols":
						int temp = Player.UpKey;
						Player.UpKey = Player.DownKey;
						Player.DownKey = temp;
						temp = Player.LeftKey;
						Player.LeftKey = Player.RightKey;
						Player.RightKey = temp;
						break;
					case "multiplyspeed":
						if(tokens.length < 3){
							showMessage("Format: player multiplySpeed [MULTIPLIER]");
							break;
						}
						Player.SPEED *= Double.parseDouble(tokens[2]);
						break;
					case "multiplyjump":
						if(tokens.length < 3){
							showMessage("Format: player multiplyJump [MULTIPLIER]");
							break;
						}
						Player.getInstance().JUMPMULT *= Double.parseDouble(tokens[2]);
						break;
					default:
						showMessage("Token '" + tokens[1] + "' not recognized");
						break;
				}
				break;
			default:
				showMessage("Command '" +tokens[0]+ "' not recognized");
				break;
		}
		history.add(0, str);
		historyPos = -1;
		textBox.clear();
	}

	public void showMessage(String text){
		message = text;
		msgAlpha = 255;
	}

	@Override
	public void update() {
		if(KeyManager.checkKeyAndReset(192))
			StateManager.pop();

		if(msgAlpha > 0)
			msgAlpha -= 2;

		textBox.update();
		if(KeyManager.checkKeyAndReset(KeyEvent.VK_ENTER)){
			String text = textBox.getText();
			if(!text.equals(""))
				parseInput(textBox.getText());
		}

		if(KeyManager.checkKeyAndReset(KeyEvent.VK_UP) && history.size() > 0){
			if(historyPos < history.size()-1) {
				historyPos++;
				textBox.setText(history.get(historyPos));
			}
		}

		if(KeyManager.checkKeyAndReset(KeyEvent.VK_DOWN) && historyPos > -1){
			historyPos--;
			if(historyPos > -1)
				textBox.setText(history.get(historyPos));
			else
				textBox.clear();
		}
	}

	@Override
	public void draw(Graphics2D graphics) {
		StateManager.peek(1).draw(graphics);
		textBox.draw(graphics);
		if(msgAlpha > 0) {
			Color oldColor = graphics.getColor();
			graphics.setColor(new Color(oldColor.getRed(), oldColor.getGreen(), oldColor.getBlue(), msgAlpha));
			graphics.drawString(message, (int)(Game.getGameWidth()*.01), (int)(Game.getGameHeight()*.05));
			graphics.setColor(oldColor);
		}
	}

	@Override
	public void onEnter() {

	}

	@Override
	public void onExit() {

	}
}
