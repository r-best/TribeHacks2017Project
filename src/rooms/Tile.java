package rooms;

import entities.Player;
import events.Event;
import graphics.Assets;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import graphics.Camera;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import utils.KeyManager;
import utils.Preferences;

public class Tile {
	public static Tile[] tileData;
	public static final int width = (int)(32* Preferences.scale), height = (int)(32*Preferences.scale);

	private BufferedImage texture;
	private boolean isSolid;

	private ArrayList<Event> walkOverEvents, keyPressEvents;

	public Tile(BufferedImage texture, boolean isSolid){
		this.texture = texture;
		this.isSolid = isSolid;
	}
	public Tile(Tile t){
		this.texture = t.getTexture();
		this.isSolid = t.isSolid();
		this.walkOverEvents = new ArrayList<>();
		this.keyPressEvents = new ArrayList<>();
	}

	public void addWalkOverEvent(Event event){ walkOverEvents.add(event); }
	public void addKeyPressEvent(Event event){ keyPressEvents.add(event); }

	public static void tileInit(){
		tileData = new Tile[256];

		try{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.parse("src/resources/data/tiles.xml");
			document.normalize();

			NodeList tileData = document.getElementsByTagName("tile");

			for(int i = 0; i < tileData.getLength(); i++){
				Element tile = (Element)tileData.item(i);
				Tile.tileData[i] = new Tile(Assets.loadImage("/resources/tiles/" + tile.getAttribute("sprite")),
						Boolean.parseBoolean(tile.getAttribute("solid")));
			}
		}
		catch(ParserConfigurationException e){
			e.printStackTrace();
		}catch(SAXException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void update(int x, int y){
		if(!walkOverEvents.isEmpty() && Player.getInstance().collisionWithTile(x, y))
			walkOverEvents.get(0).trigger();

		if(!keyPressEvents.isEmpty() && Player.getInstance().collisionWithTile(x, y) && KeyManager.checkKeyAndReset(KeyEvent.VK_S))
			keyPressEvents.get(0).trigger();
	}

	public void draw(Graphics2D graphics, int x, int y){
		graphics.drawImage(texture, (int)(x*Tile.width + Camera.getXOffset()), (int)(y*Tile.height + Camera.getYOffset()), width, height, null);
	}

	public boolean isSolid(){
		return isSolid;
	}
	public BufferedImage getTexture(){ return texture; }
}