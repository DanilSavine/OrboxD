
import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Enables to retrieve levels from xml and to update entities in the game
 * @author Danil
 *
 */

public class Level {
	public Game game;
	public String file;
	public ArrayList<Entity> entities;
	public Player player;
	public Level(Game game, String file, ArrayList<Entity> entities, Player player) {
		this.game = game;
		this.file = file;
		this.entities = entities;
		this.player = player;
	}
	public void initEntities(){
		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (new File(file));

			doc.getDocumentElement ().normalize ();

			NodeList listOfEntities = doc.getElementsByTagName("entity");

			for(int s=0; s<listOfEntities.getLength() ; s++){


				Node firstPersonNode = listOfEntities.item(s);
				if(firstPersonNode.getNodeType() == Node.ELEMENT_NODE){

					Element firstPersonElement = (Element)firstPersonNode;

					NodeList firstNameList = firstPersonElement.getElementsByTagName("x");
					Element firstNameElement = (Element)firstNameList.item(0);

					NodeList textFNList = firstNameElement.getChildNodes();
					Integer x = Integer.parseInt(((Node)textFNList.item(0)).getNodeValue().trim());


					NodeList lastNameList = firstPersonElement.getElementsByTagName("y");
					Element lastNameElement = (Element)lastNameList.item(0);

					NodeList textLNList = lastNameElement.getChildNodes();
					Integer y = Integer.parseInt(((Node)textLNList.item(0)).getNodeValue().trim());


					NodeList ageList = firstPersonElement.getElementsByTagName("type");
					Element ageElement = (Element)ageList.item(0);

					NodeList textAgeList = ageElement.getChildNodes();
					String type = ((Node)textAgeList.item(0)).getNodeValue().trim();


					if(type.equals("obstacle")){
						Entity alien = new Block(x*40,y*40);
						entities.add(alien);

					} else if (type.equals("player")){
						player = new Player(game,"Ressources/player.gif",x*40,y*40);

					} else if (type.equals("win")){
						Entity win = new Win(game,"Ressources/win.gif",x*40,y*40);
						entities.add(win);
					}
				}
			}
		}catch (SAXParseException err) {
			System.out.println ("** Parsing error" + ", line " 
					+ err.getLineNumber () + ", uri " + err.getSystemId ());
			System.out.println(" " + err.getMessage ());

		}catch (SAXException e) {
			Exception x = e.getException ();
			((x == null) ? e : x).printStackTrace ();

		}catch (Throwable t) {
			t.printStackTrace ();
		}
	}

	public ArrayList<Entity> getEntities(){
		return this.entities;
	}public Player getShip(){
		return this.player;
	}
}

