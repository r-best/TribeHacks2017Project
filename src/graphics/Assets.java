package graphics;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import utils.Preferences;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Assets {

	public static Font timesNewRoman, papyrus, comicSans;

	public static final int playerSpriteWidth = 50, playerSpriteHeight = 50;

	private static HashMap<String, String[]> dialogue;
	public static HashMap<String, BufferedImage> enemySprites;
	private static HashMap<String, Animation[]> entityWalkAnims;

	public static String[] getDialogue(String key){
		return dialogue.get(key);
	}
	public static Animation[] getEntityWalkingAnimation(String key){ return entityWalkAnims.get(key); }

	public static void init() throws FontFormatException, IOException{
		/*timesNewRoman = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/fonts/times new roman.ttf"));
		papyrus = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/fonts/papyrus.ttf"));
		comicSans = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/fonts/comic sans.ttf"));

		entityWalkAnims = new HashMap<>();
		loadWalkAnimations("src/resources");

		enemySprites = new HashMap<>();
		loadEnemySprites("src/resources");

		textInit();*/
	}

	private static void loadEnemySprites(String path){
		File dir = new File(path);
		if(dir.isDirectory()){
			for(File f : dir.listFiles()){
				if(f.isDirectory())
					loadEnemySprites(f.getPath());
				else if(f.getName().endsWith(".png") || f.getName().endsWith(".jpg")){
					if(Preferences.scale != 1.0) {
						BufferedImage before = loadImage(f.getPath().substring(3).replace('\\', '/'));
						AffineTransform at = new AffineTransform();
						at.scale(Preferences.scale, Preferences.scale);
						AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
						BufferedImage after = new BufferedImage((int) (before.getWidth() * Preferences.scale), (int) (before.getHeight() * Preferences.scale), BufferedImage.TYPE_INT_ARGB);
						enemySprites.put(f.getName().substring(0, f.getName().length() - 4), op.filter(before, after));
					}
					else
						enemySprites.put(f.getName().substring(0, f.getName().length() - 4), loadImage(f.getPath().substring(3).replace('\\', '/')));
				}
			}
		}
	}

	private static void loadWalkAnimations(String path) {
		File dir = new File(path);
		if(dir.isDirectory()){
			for(File f : dir.listFiles()){
				if(f.isDirectory())
					loadWalkAnimations(f.getPath());
				if(f.getName().endsWith(".png") || f.getName().endsWith(".jpg")){
					BufferedImage spriteSheet = loadImage(f.getPath().substring(3).replace('\\', '/'));
					if(spriteSheet.getWidth() >= playerSpriteWidth*4 && spriteSheet.getHeight() >= playerSpriteHeight*4) {
						BufferedImage[][] sprite = new BufferedImage[4][4];
						for (int i = 0; i < 4; i++) {
							for (int j = 0; j < 4; j++) {
								if (Preferences.scale != 1.0) {
									BufferedImage before = spriteSheet.getSubimage(playerSpriteWidth * j, playerSpriteHeight * i, playerSpriteWidth, playerSpriteHeight);
									AffineTransform at = new AffineTransform();
									at.scale(Preferences.scale, Preferences.scale);
									AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
									BufferedImage after = new BufferedImage((int) (playerSpriteWidth * Preferences.scale), (int) (playerSpriteHeight * Preferences.scale), BufferedImage.TYPE_INT_ARGB);
									sprite[i][j] = op.filter(before, after);
								} else
									sprite[i][j] = spriteSheet.getSubimage(playerSpriteWidth * j, playerSpriteHeight * i, playerSpriteWidth, playerSpriteHeight);
							}
						}
						entityWalkAnims.put(f.getName().substring(0, f.getName().length() - 4), Animation.createAnimations(sprite));
					}
				}
			}
		}
	}

	/*
	* Loads all the values from dialogue.xml into a hash map,
	* entities in rooms.xml have the hash key of their dialogue as an attribute
	 */
	private static void textInit(){
		dialogue = new HashMap<>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse("src/resources/data/dialogue.xml");
			document.normalize();

			NodeList text = document.getElementsByTagName("text");
			for(int i = 0; i < text.getLength(); i++){
				Element current = (Element)text.item(i);
				dialogue.put(current.getAttribute("id").toUpperCase(), current.getTextContent().split("/"));
			}
		}
		catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BufferedImage loadImage(String path){
		try{
			return ImageIO.read(Assets.class.getResource(path));
		} catch(IOException ex){
			Logger.getLogger(Assets.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
}