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
	private String name;
	private Vector<Flashcard> cards;
	
	public Deck() {
		this.name = "";
		this.cards = new Vector<Flashcard>();
	}
	
	public Deck(String name) {
		this.name = name;
		this.cards = new Vector<Flashcard>();
	}
	
	public Deck(String name, Vector<Flashcard> cards) {
		this.name = name;
		this.cards = copyCards(cards);
	}
	
	public String getName() {
		return this.name;
	}
	
	public Vector<Flashcard> getCards() {
		return this.cards;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setCards(Vector<Flashcard> cards) {
		this.cards = copyCards(cards);
	}
	
	private Vector<Flashcard> copyCards(Vector<Flashcard> cards) {
		if (null == cards || cards.size() == 0) {
			return new Vector<Flashcard>();
		}
		
		Vector<Flashcard> copy = new Vector<Flashcard>();
		int numCards = cards.size();
		
		for (int i = 0; i < numCards; i++) {
			Flashcard curCard = cards.get(i);
			Flashcard newCard = new Flashcard(curCard.getSides(), curCard.getWeight());
			copy.add(newCard);
		}
		
		return copy;
	}
}
