package entities;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import events.*;
import events.Event;
import graphics.Assets;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EntityManager {
	private ArrayList<Entity> entities;

	public EntityManager(int roomNum){
		entities = new ArrayList<>();
		initializeEntities(roomNum);
	}

	public void update(){
		for(Entity e : entities)
			e.update();
	}

	public void draw(Graphics2D graphics){
		for (Entity e : entities)
			e.draw(graphics);
	}

	public void addEntity(Entity ent){
		entities.add(ent);
	}

	public ArrayList<Entity> getEntities(){
		return entities;
	}

	/**
	 * Adds the room's entities to the EntityManager from the data in rooms.xml
	 * Called by the EntityManager contructor when a new EntityManager is made, which happens every time a player enters a room they have not entered before
	 * @param roomNum
	 */
	private void initializeEntities(int roomNum){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse("src/resources/data/rooms.xml");
			document.normalize();

			//Get correct room
			Element room = (Element)document.getElementsByTagName("room").item(roomNum-1);

			//Get all child elements of the room tag & iterate through them
			NodeList entities = room.getElementsByTagName("*");
			for(int i = 0; i < entities.getLength(); i++){
				Element entity = (Element) entities.item(i);

				//Construct list of events associated with this entity
				NodeList eventList = entity.getElementsByTagName("*");
				ArrayList<Event> events = new ArrayList<>();
				for(int j = 0; j < eventList.getLength(); j++){
					Element event = (Element)eventList.item(j);
					switch(event.getTagName()) {
						case "text":
							events.add(new DialogueEvent(Assets.getDialogue(event.getTextContent())));
							break;
						default:
							break;
					}
				}

				//Create entity
				switch(entity.getTagName()){
					case "npc":
						addEntity(new NPC(Integer.parseInt(entity.getAttribute("x")),
								Integer.parseInt(entity.getAttribute("y")),
								Assets.getEntityWalkingAnimation(entity.getAttribute("anim")),
								events));
						break;
				}
			}
		}
		catch (ParserConfigurationException e){
			e.printStackTrace();
		}catch (SAXException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}