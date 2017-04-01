package rooms;

import java.awt.*;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import entities.Player;
import events.ChangeRoomEvent;
import events.Event;
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
						rooms[i].tiles = new Tile[rooms[i].width][rooms[i].height];
					}

					for(int k = 0; k < rooms[i].width; k++){
						if(k > row.length()-1)
							rooms[i].tiles[k][j] = new Tile(Tile.tileData[0]);
						else
							rooms[i].tiles[k][j] = new Tile(Tile.tileData[Integer.parseInt(row.charAt(k)+"")]);//Byte.parseByte(row.substring(k, k+1));
					}
				}

				NodeList tileEvents = document.getElementsByTagName("*");
				for(int j = 0; j < tileEvents.getLength(); j++){
					Element event = (Element)tileEvents.item(j);

					boolean triggerOnButton = false;
					if("button".equals(event.getAttribute("trigger")))
						triggerOnButton = true;

					switch(event.getTagName()){
						case "changeroom":
							Tile t = rooms[i].getTile(Integer.parseInt(event.getAttribute("x")), Integer.parseInt(event.getAttribute("y")));
							Event e = new ChangeRoomEvent(Integer.parseInt(event.getAttribute("destRoom")), Integer.parseInt(event.getAttribute("destx")), Integer.parseInt(event.getAttribute("desty")));
							if(triggerOnButton)
								t.addKeyPressEvent(e);
							else
								t.addWalkOverEvent(e);
							break;
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
		setRoom(1, 0, 0);
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