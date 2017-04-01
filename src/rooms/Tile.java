package rooms;

import graphics.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import utils.Preferences;

public class Tile {
	public static Tile[] tiles;

	public static final int width = (int)(32* Preferences.scale), height = (int)(32*Preferences.scale);

	private BufferedImage texture;
	private boolean isSolid;

	public Tile(BufferedImage texture, boolean isSolid){
		this.texture = texture;
		this.isSolid = isSolid;
	}

	public static void tileInit(){
		tiles = new Tile[256];

		try{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.parse("src/resources/data/tiles.xml");
			document.normalize();

			NodeList tileData = document.getElementsByTagName("tile");

			for(int i = 0; i < tileData.getLength(); i++){
				Element tile = (Element)tileData.item(i);
				tiles[i] = new Tile(Assets.loadImage("/resources/tiles/" + tile.getAttribute("sprite")),
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

	public void draw(Graphics2D graphics, int x, int y){
		graphics.drawImage(texture, x, y, width, height, null);
	}

	public boolean isSolid(){
		return isSolid;
	}
}