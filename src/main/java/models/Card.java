package models;

public class Card {

	//Atributos de la clase
	private int id;
	private String name;
	private int summonCost;
	private int attack;
	private int defense;
	private int value;

	public Card() {

	}

	//Constructor del objeto 
	public Card(int id, String name, int summonCost, int attack, int defense, int value) {
		this.id = id;
		this.name = name;
		this.summonCost = summonCost;
		this.attack = attack;
		this.defense = defense;
		this.value = value;
	}

	// GETTERS y SETTERS
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSummonCost() {
		return summonCost;
	}

	public void setSummonCost(int summonCost) {
		this.summonCost = summonCost;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return name+" - "+summonCost+" - "+ attack + " - "+ defense + " - "+ value;
	}
	
	

}