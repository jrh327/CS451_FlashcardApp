/**
 * Deck.java: object for a deck of cards
 * 
 * This file is part of FlashcardApp
 * 
 * Contributors:
 * Jon Hopkins
 * Jesse Kuehn
 * Rishir Patel
 * Sanjana Raj
 */

package group8.cs451.drexel;

import java.util.Vector;

public class Deck {
	private int id;
	private String name;
	private Vector<Flashcard> cards;
	private boolean isDirty = false;
	
	public Deck(int id) {
		this.id = id;
		this.name = "";
		this.cards = new Vector<Flashcard>();
	}
	
	public Deck(int id, String name) {
		this.id = id;
		this.name = name;
		this.cards = new Vector<Flashcard>();
	}
	
	public Deck(int id, String name, Vector<Flashcard> cards) {
		this.id = id;
		this.name = name;
		this.cards = cards;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Vector<Flashcard> getCards() {
		return this.cards;
	}
	
	public void setName(String name) {
		this.name = name;
		this.isDirty = true;
	}
	
	public void setCards(Vector<Flashcard> cards) {
		this.cards = cards;
	}
	
	public void addCard(Flashcard card) {
		if (this.cards.contains(card)) {
			return;
		}
		this.cards.add(card);
	}
	
	public void removeCard(Flashcard card) {
		this.cards.remove(card);
	}
	
	public boolean isDirty() {
		return this.isDirty;
	}
	
	public void markDirty() {
		this.isDirty = true;
	}
	
	public void markClean() {
		this.isDirty = false;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Deck)) {
			return false;
		}
		
		// assume Object o is equal to this if the names
		// are the same, because the database requires each
		// deck's name to be unique, so there should never
		// be a case where a deck with the same name would
		// not be the deck we're looking for
		// check the ids too just in case
		Deck d = (Deck)o;
		return (d.getID() == this.id && d.name.equals(this.name));
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
