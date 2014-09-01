/**
 * DeckOperations.java: contains common methods for working with decks of flashcards
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

import java.util.ArrayList;
import java.util.Vector;

import com.almworks.sqlite4java.SQLiteException;

public class DeckOperations {

	/**
	 * Load the existing decks<br>
	 * For performance reasons, empty Deck objects with only the names are loaded
	 * 
	 * @return A vector containing the Deck objects that were successfully loaded
	 */
	public static Vector<Deck> loadDecks() {
		SQLiteHandler sqlite;
		Vector<Deck> decks = new Vector<Deck>();
		
		try {
			sqlite = new SQLiteHandler(Config.DATABASE);
		} catch (SQLiteException e) {
			e.printStackTrace();
			return decks;
		}
		
		try {
			ArrayList<ArrayList<String>> arr = sqlite.select(Config.DECK_TABLE, "ID,Name", "", "");
			
			for (int i = 0; i < arr.size(); i++) {
				ArrayList<String> row = arr.get(i);
				decks.add(new Deck(Integer.parseInt(row.get(0)), row.get(1)));
			}
			
			return decks;
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			sqlite.close();
		}
		
		return decks;
	}
	
	/**
	 * Loads the cards owned by the deck<br>
	 * For performance reasons, the deck's cards get the id and weight only
	 * 
	 * @param deck The Deck object to be filled out
	 */
	public static void loadDeck(Deck deck) {
		SQLiteHandler sqlite;
		try {
			sqlite = new SQLiteHandler(Config.DATABASE);
		} catch (SQLiteException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			String deckName = deck.getName();
			ArrayList<ArrayList<String>> arr = sqlite.select(Config.CARD_TABLE, "ID,Weight",
						"DeckID = (SELECT ID FROM " + Config.DECK_TABLE + " WHERE Name = ?", deckName);
			
			for (int i = 0; i < arr.size(); i++) {
				ArrayList<String> row = arr.get(i);
				Flashcard card = new Flashcard(Integer.parseInt(row.get(0)));
				card.setWeight(Integer.parseInt(row.get(1)));
				
				deck.addCard(card);
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			sqlite.close();
		}
	}
	
	/**
	 * Saves the deck, its cards, and the cards' sides to the database
	 * 
	 * @param deck The deck to save
	 */
	public static void saveDeck(Deck deck) {
		SQLiteHandler sqlite;
		try {
			sqlite = new SQLiteHandler(Config.DATABASE);
		} catch (SQLiteException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			if (deck.isDirty()) {
				sqlite.update(Config.DECK_TABLE, "Name", deck.getName(), "ID = ?", String.valueOf(deck.getID()));
				deck.markClean();
			}
			Vector<Flashcard> cards = deck.getCards();
			for (int i = 0; i < cards.size(); i++) {
				Flashcard card = cards.get(i);
				if (card.isDirty()) {
					sqlite.update(Config.CARD_TABLE, "Weight", String.valueOf(card.getWeight()), "ID = ?", String.valueOf(card.getID()));
					card.markClean();
				}
				Vector<FlashcardSide> sides = card.getSides();
				for (int j = 0; j < sides.size(); j++) {
					FlashcardSide side = sides.get(j);
					if (side.isDirty()) {
						sqlite.update(Config.SIDE_TABLE, "Label,Text,Weight",
								side.getLabel() + "," + side.getText() + "," + side.getWeight(),
								"CardID = ?", String.valueOf(card.getID()));
						side.markClean();
					}
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			sqlite.close();
		}
	}
	
	/**
	 * Adds a new deck to the list
	 * 
	 * @param decks The list of decks to add to
	 * @param deckName The name of the new deck
	 */
	public static void addDeck(Vector<Deck> decks, String deckName) {
		SQLiteHandler sqlite;
		try {
			sqlite = new SQLiteHandler(Config.DATABASE);
		} catch (SQLiteException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			sqlite.insert(Config.DECK_TABLE, "Name", deckName);
			int id = (int)sqlite.getLastInsertId();
			Deck deck = new Deck(id, deckName);
			deck.markDirty();
			decks.add(deck);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			sqlite.close();
		}
	}
	
	/**
	 * Removed the specified deck from the list<br>
	 * Also removes all the deck's cards and their sides from the database<br>
	 * There is no recovering a deck once this method is called
	 * 
	 * @param decks The list of decks to remove from
	 * @param deck The deck to remove
	 */
	public static void removeDeck(Vector<Deck> decks, Deck deck) {
		SQLiteHandler sqlite;
		try {
			sqlite = new SQLiteHandler(Config.DATABASE);
		} catch (SQLiteException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			String deckID = String.valueOf(deck.getID());
			sqlite.delete(Config.SIDE_TABLE, "CardId IN (SELECT ID FROM " + Config.CARD_TABLE + " WHERE DeckID = ?)", deckID);
			sqlite.delete(Config.CARD_TABLE, "DeckID = ?", deckID);
			sqlite.delete(Config.DECK_TABLE, "ID = ?", deckID);
			decks.remove(new Deck(deck.getID(), deck.getName()));
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a new card to the specified deck
	 * 
	 * @param deck The deck to add the card to
	 */
	public static void addNewCardToDeck(Deck deck) {
		SQLiteHandler sqlite;
		try {
			sqlite = new SQLiteHandler(Config.DATABASE);
		} catch (SQLiteException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			sqlite.insert(Config.CARD_TABLE, "DeckID", String.valueOf(deck.getID()));
			int cardID = (int)sqlite.getLastInsertId();
			Flashcard card = new Flashcard(cardID);
			card.markDirty();
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			sqlite.close();
		}
	}
	
	/**
	 * Adds an existing card to the specified deck
	 * 
	 * @param deck The deck to add the card to
	 * @param card The card to add
	 */
	public static void addCardToDeck(Deck deck, Flashcard card) {
		SQLiteHandler sqlite;
		try {
			sqlite = new SQLiteHandler(Config.DATABASE);
		} catch (SQLiteException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			sqlite.insert(Config.CARD_TABLE, "DeckID,Weight", deck.getID() + "," + card.getWeight());
			card.markDirty();
			deck.addCard(card);
			
			if (card.getSides().size() > 0) {
				Vector<FlashcardSide> sides = card.getSides();
				int cardID = card.getID();
				for (int i = 0; i < sides.size(); i++) {
					FlashcardSide side = sides.get(i);
					sqlite.insert(Config.SIDE_TABLE, "CardID,Label,Text,Weight",
							cardID + "," + side.getLabel() + "," + side.getText() + "," + side.getWeight());
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			sqlite.close();
		}
	}
	
	/**
	 * Removes the specified card from the specified deck<br>
	 * Also removes the card and its sides from the database<br>
	 * There is no recovering a card once this method is called
	 * 
	 * @param deck The deck to remove the card from
	 * @param card The card to be removed
	 */
	public static void removeCardFromDeck(Deck deck, Flashcard card) {
		SQLiteHandler sqlite;
		try {
			sqlite = new SQLiteHandler(Config.DATABASE);
		} catch (SQLiteException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			sqlite.delete(Config.SIDE_TABLE, "CardId = ?", String.valueOf(card.getID()));
			sqlite.delete(Config.CARD_TABLE, "ID = ?", String.valueOf(card.getID()));
			deck.removeCard(card);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			sqlite.close();
		}
	}
	
	/**
	 * Adds a new side to the specified card
	 * 
	 * @param card The card to add the side to
	 */
	public static void addNewSideToCard(Flashcard card) {
		SQLiteHandler sqlite;
		try {
			sqlite = new SQLiteHandler(Config.DATABASE);
		} catch (SQLiteException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			sqlite.insert(Config.SIDE_TABLE, "CardID,Text,Label", card.getID() + ",,");
			int sideID = (int)sqlite.getLastInsertId();
			FlashcardSide side = new FlashcardSide(sideID);
			side.markDirty();
			card.addSide(side);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			sqlite.close();
		}
	}
	
	/**
	 * Adds an existing side to the specified card
	 * 
	 * @param card The card to add the side to
	 * @param side The side to be added
	 */
	public static void addSideToCard(Flashcard card, FlashcardSide side) {
		SQLiteHandler sqlite;
		try {
			sqlite = new SQLiteHandler(Config.DATABASE);
		} catch (SQLiteException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			sqlite.insert(Config.SIDE_TABLE, "CardID,Label,Text,Weight",
					card.getID() + "," + side.getLabel() + ","  + side.getText() + "," + side.getWeight());
			side.markDirty();
			card.addSide(side);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			sqlite.close();
		}
	}
	
	/**
	 * Removed the specified side from the specified card<br>
	 * Also removes the side from database<br>
	 * There is no recovering a side once this method is called
	 * 
	 * @param card The card to remove the side from
	 * @param side The side to be remove
	 */
	public static void removeSideFromCard(Flashcard card, FlashcardSide side) {
		SQLiteHandler sqlite;
		try {
			sqlite = new SQLiteHandler(Config.DATABASE);
		} catch (SQLiteException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			sqlite.delete(Config.SIDE_TABLE, "ID = ?", String.valueOf(side.getID()));
			card.removeSide(side);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			sqlite.close();
		}
	}
}
