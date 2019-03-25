package interfaces;

import java.util.ArrayList;

import models.Card;
import models.Deck;

public interface IDeck {
	//Metodo para buscar la baraja recibe parametro el nombre de una baraja
	public Deck loadDeck(String deck_name);
	//Metodo para guardar una baraja que recibe como parametro
	public void saveDeck(Deck deck);
	public void updateDeck(Deck deck);
	
}
