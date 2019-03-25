package daoImpl;

import java.util.ArrayList;

import org.bson.Document;

import com.google.gson.Gson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;

import interfaces.IDeck;
import models.Deck;

public class MongoDBDAOImpl implements IDeck { // Clase que implementa la interfaz de Deck

	//Atributos necesarios para trabajar con MongoDB
	private MongoClient mongoClient;
	private MongoDatabase database;
	private MongoCollection<Document> collection;
	private Document document;
	private UpdateResult updateResult;

	/*
	 * Metodo para cargar la baraja desde la base de datos de MongoDB. Establecemos
	 * la conexion pasando el nombre de la base de datos y el nombre de la
	 * coleccion, y si estos dos no existen, los creará. Hace una busqueda en los
	 * objetos de la base de datos igualando el nombre de la baraja introducida,
	 * mirando todos los nombres de las barajas. Pasamos los objetos de MongoDB de
	 * formato JSON a un arrayList de objetos de cartas.
	 */
	public Deck loadDeck(String deck_name) {
		mongoClient = new MongoClient("localhost", 27017);
		database = mongoClient.getDatabase("CardsDB");
		collection = database.getCollection("Decks");
		document = new Document();
		Deck deckToLoad = null;

		try {
			MongoCursor<Document> cursor = collection.find(Filters.eq("deckName", deck_name)).iterator();
			document = cursor.next();
			deckToLoad = new Gson().fromJson(document.toJson(), Deck.class);
		} catch (Exception e) {
			deckToLoad = null;
		}
		mongoClient.close();
		mongoClient = null;
		database = null;
		collection = null;
		return deckToLoad;
	}

	/*
	 * Metodo para cargar la baraja desde la base de datos de MongoDB. Establecemos
	 * la conexion pasando el nombre de la base de datos y el nombre de la
	 * coleccion, y si estos dos no existen, los creará. La baraj es convierte en
	 * json y lo parseamos a documento para alacenarlo en mongoDB.
	 */
	public void saveDeck(Deck deck) {
		mongoClient = new MongoClient("localhost", 27017);
		database = mongoClient.getDatabase("CardsDB");
		collection = database.getCollection("Decks");
		document = new Document();
		String json = new Gson().toJson(deck);
		document = Document.parse(json);
		collection.insertOne(document);
		mongoClient.close();
		mongoClient = null;
		database = null;
		collection = null;

	}

	/*
	 * Este metodo modificará la baraja que se ha cargado anteriormente gracias una
	 * variable booleana. Recibe como paremtro una baraja, busca esa baraja con su
	 * nombre y e el documento parseado con JSON para almacenarlo en MongoDB, y lo
	 * modifica.
	 */
	public void updateDeck(Deck deck) {
		mongoClient = new MongoClient("localhost", 27017);
		database = mongoClient.getDatabase("CardsDB");
		collection = database.getCollection("Decks");
		String json = new Gson().toJson(deck);
		document = Document.parse(json);
		updateResult = collection.replaceOne(Filters.eq("deckName", deck.getDeckName()), document);
		mongoClient.close();
		mongoClient = null;
		database = null;
		collection = null;
	}
}
