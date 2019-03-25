package daoImpl;

import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;

import com.google.gson.Gson;

import interfaces.ICard;
import models.Card;

import org.xmldb.api.*;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import javax.xml.transform.OutputKeys;


public class ExistDBDAOImpl implements ICard { //Clase DAO que implementa la interfaz ICard

	//Atributos necesarios para conectar a la base de datos de ExistDB
	private ArrayList<Card> arraylist_cards;
	private Class cl;
	private String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db/"; 
	private String collectionName = "cards";
	private String xmlFile = "cards.xml";
	private Collection col = null;
	private XMLResource res = null;
	final String driver = "org.exist.xmldb.DatabaseImpl";

	/*
	* Desarollo del metodo getAllCards para conseguir todas las cartas de xml a objeto carta
	* Se conecta a la base de datos atraves de la ruta y encuentra la coleccion.
	* Almacena toda la informacion del xml en un arraylist formato JSON y lo pasamos a ArrayJSON t lo almacenamos como objeto una por una.
	*/
	public ArrayList<Card> getAllCards() {
		try {
			cl = Class.forName(driver);
			Database database = (Database) cl.newInstance();
			database.setProperty("create-database", "true");
			DatabaseManager.registerDatabase(database);
			col = DatabaseManager.getCollection(URI + collectionName);
			res = (XMLResource) col.getResource(xmlFile);
			if (res.equals(null)) {
				System.out.println("document not found!");
			} else {
				arraylist_cards = new ArrayList<Card>();
				res = (XMLResource) col.getResource(xmlFile);
				JSONObject xmlJSONObj = XML.toJSONObject((String) res.getContent());
				JSONArray arrayJSON = xmlJSONObj.getJSONObject("cards").getJSONArray("card");
				for (int i = 0; i < arrayJSON.length(); i++) {
					Card card = new Gson().fromJson(arrayJSON.get(i).toString(), Card.class);
					arraylist_cards.add(card);
				}

			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | XMLDBException
				| JSONException e) {
			// TODO Auto-generated catch block
		}
		return arraylist_cards;
	}

}