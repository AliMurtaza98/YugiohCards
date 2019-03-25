package models;

import java.util.ArrayList;

public class Deck {

	//Atributos de la clase
	private String deckName;
	private int deckValue;
	private ArrayList<Card> arraylist_Cards;

	public Deck() {

	}
	//Constructor del objeto
	public Deck(String deckName, int deckValue, ArrayList<Card> arraylist_Cards) {
		this.deckName = deckName;
		this.deckValue = deckValue;
		this.arraylist_Cards = arraylist_Cards;
	}
	//GETTERS y SETTERS
	public String getDeckName() {
		return deckName;
	}

	public void setDeckName(String deckName) {
		this.deckName = deckName;
	}

	public int getDeckValue() {
		return deckValue;
	}

	public void setDeckValue(int deckValue) {
		this.deckValue = deckValue;
	}

	public ArrayList<Card> getArraylist_Cards() {
		return arraylist_Cards;
	}

	public void setArraylist_Cards(ArrayList<Card> arraylist_Cards) {
		this.arraylist_Cards = arraylist_Cards;
	}

	@Override
	public String toString() {
		return "Deck [deckName=" + deckName + ", deckValue=" + deckValue + ", arraylist_Cards=" + arraylist_Cards + "]";
	}

	
}
