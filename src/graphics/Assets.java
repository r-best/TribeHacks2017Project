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

	public static Font timesNewRoman;

	private static HashMap<String, String[]> dialogue;
	private static HashMap<String, Animation[]> entityAnims;

	public static String[] getDialogue(String key){
		return dialogue.get(key);
	}
	public static Animation[] getEntityAnimation(String key){ return entityAnims.get(key); }

	public static void init() throws FontFormatException, IOException{
		timesNewRoman = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/fonts/times new roman.ttf"));

		entityAnims = new HashMap<>();
		loadAnimations("src/resources");

		textInit();
	}

	private static void loadAnimations(String path) {
		/*File dir = new File(path);
		if(dir.isDirectory()){
			for(File f : dir.listFiles()){
				if(f.isDirectory())
					loadAnimations(f.getPath());
				if(f.getName().endsWith(".png") || f.getName().endsWith(".jpg")){
					BufferedImage spriteSheet = loadImage(f.getPath().substring(3).replace('\\', '/'));
					if(spriteSheet.getWidth() >= spriteWidth *4 && spriteSheet.getHeight() >= spriteHeight *2) {
						BufferedImage[][] sprite = new BufferedImage[2][4];
						for (int i = 0; i < 2; i++) {
							for (int j = 0; j < 4; j++) {
								if (Preferences.scale != 1.0) {
									BufferedImage before = spriteSheet.getSubimage(spriteWidth * j, spriteHeight * i, spriteWidth, spriteHeight);
									AffineTransform at = new AffineTransform();
									at.scale(Preferences.scale, Preferences.scale);
									AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
									BufferedImage after = new BufferedImage((int) (spriteWidth * Preferences.scale), (int) (spriteHeight * Preferences.scale), BufferedImage.TYPE_INT_ARGB);
									sprite[i][j] = op.filter(before, after);
								} else
									sprite[i][j] = spriteSheet.getSubimage(spriteWidth * j, spriteHeight * i, spriteWidth, spriteHeight);
							}
						}
						entityAnims.put(f.getName().substring(0, f.getName().length() - 4), Animation.createAnimations(sprite));
					}
				}
			}
		}*/
		createAnimation(path, "player.png", 2, 4, 50, 50);
		createAnimation(path, "playerAttack.png", 2, 4, 50, 50);
		createAnimation(path, "toddhoward.png", 2, 1, 100, 100);
		createAnimation(path, "bullet.png", 2, 1, 10, 10);
	}

	public static void createAnimation(String path, String itemName, int rows, int columns, int width, int height){
		File f = new File(path + "/" + itemName);
		System.out.println(f.getPath());
		BufferedImage spriteSheet = loadImage(f.getPath().substring(3).replace('\\', '/'));
		BufferedImage[][] sprite = new BufferedImage[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (Preferences.scale != 1.0) {
					BufferedImage before = spriteSheet.getSubimage(width * j, height * i, width, height);
					AffineTransform at = new AffineTransform();
					at.scale(Preferences.scale, Preferences.scale);
					AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
					BufferedImage after = new BufferedImage((int) (width * Preferences.scale), (int) (height * Preferences.scale), BufferedImage.TYPE_INT_ARGB);
					sprite[i][j] = op.filter(before, after);
				} else
					sprite[i][j] = spriteSheet.getSubimage(width * j, height * i, width, height);
			}
		}
		entityAnims.put(f.getName().substring(0, f.getName().length() - 4), Animation.createAnimations(sprite));
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