package rooms;

import java.awt.*;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import entities.Player;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RoomManager {

	private static Room[] rooms = new Room[50];
	private static Room currentRoom;

	public static void init(){
		Tile.tileInit();
		try{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.parse("src/resources/data/rooms.xml");
			document.normalize();

			NodeList roomData = document.getElementsByTagName("room");

			rooms[0] = null;
			for(int i = 1; i <= roomData.getLength(); i++){
				rooms[i] = new Room();

				NodeList rows = ((Element)roomData.item(i-1)).getElementsByTagName("row");

				//Find room width
				int roomWidth = 0;
				for(int j = 0; j < rows.getLength(); j++){
					String row = ((Element)rows.item(j)).getTextContent().replaceAll("\\s+", "");

					if(row.length() > roomWidth)
						roomWidth = row.length();
				}

				for(int j = 0; j < rows.getLength(); j++){
					String row = ((Element)rows.item(j)).getTextContent().replaceAll("\\s+", "");

					if(rooms[i].tiles == null){
						rooms[i].height = rows.getLength();
						rooms[i].width = roomWidth;
						rooms[i].tiles = new byte[rooms[i].width][rooms[i].height];
					}

					for(int k = 0; k < row.length(); k++){
						rooms[i].tiles[k][j] = Byte.parseByte(row.substring(k, k+1));
					}
				}
			}
		}
		catch(ParserConfigurationException e){
			e.printStackTrace();
		}catch(SAXException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		setRoom(1, 0, -2);
	}

	public static void update(){
		currentRoom.update();
	}

	public static void draw(Graphics2D graphics){
		currentRoom.draw(graphics);
	}

	public static void setRoom(int roomNumber, int playerNewX, int playerNewY){
		if(!rooms[roomNumber].initialized){
			rooms[roomNumber].entityInit(roomNumber);
		}

		currentRoom = rooms[roomNumber];

		Player.getInstance().moveTo(playerNewX, playerNewY);
	}
	public static Room getRoom(){
		return currentRoom;
	}
}