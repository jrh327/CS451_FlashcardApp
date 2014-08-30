/**
 * Main.java: main routine
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

public class Main {
	private Vector<Deck> decks;
	SQLiteHandler sql;
	
	private static final String DECK_TABLE = "Decks";
	private static final String CARD_TABLE = "Cards";
	private static final String SIDE_TABLE = "Sides";
	
	public static void main(String[] args) {
		SQLiteHandler sqlite;
		try {
			sqlite = new SQLiteHandler("/tmp/flashcarddb");
		} catch (SQLiteException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			// Drop all the tables in case they already exist
			sqlite.dropTable(DECK_TABLE);
			sqlite.dropTable(CARD_TABLE);
			sqlite.dropTable(SIDE_TABLE);
			
			// Create all the tables
			sqlite.createTable(DECK_TABLE, "ID,Name", "INTEGER PRIMARY KEY ASC,CHAR(50) UNIQUE");
			sqlite.createTable(CARD_TABLE, "ID,DeckID,Weight", "INTEGER PRIMARY KEY ASC,INTEGER,INTEGER");
			sqlite.createTable(SIDE_TABLE, "ID,CardID,Label,Text,Weight", "INTEGER PRIMARY KEY ASC,INTEGER,CHAR(50),CHAR(100),INTEGER");
			
			// Insert some rows into table Decks
			sqlite.insert(DECK_TABLE, "Name", "TestDeck");
			sqlite.insert(DECK_TABLE, "Name", "TestDeck2");
			
			// Insert some rows into table Cards
			sqlite.insert(CARD_TABLE, "DeckID,Weight", "2,60");
			sqlite.insert(CARD_TABLE, "DeckID,Weight", "2,50");
			sqlite.insert(CARD_TABLE, "DeckID,Weight", "1,10");
			
			// Insert some sides for the first card in the first deck
			sqlite.insert(SIDE_TABLE, "CardID,Label,Text,Weight", "1,Test Label,Test Text,100");
			sqlite.insert(SIDE_TABLE, "CardID,Label,Text,Weight", "1,TestLabel,TestText,40");
			
			// Insert some sides for the second card in the second deck
			sqlite.insert(SIDE_TABLE, "CardID,Label,Text,Weight", "2,Chemical Name,Water,50");
			sqlite.insert(SIDE_TABLE, "CardID,Label,Text,Weight", "2,Chemical Formula, H20,40");
			
			// Change the name of deck TestDeck2 to Chemistry
			sqlite.update(DECK_TABLE, "Name", "Chemistry", "ID = ?", "2");
			
			// Delete deck TestDeck, all its cards, and the sides of those cards
			sqlite.delete(SIDE_TABLE,
				"CardId IN (SELECT ID FROM " + CARD_TABLE + " WHERE DeckID = (SELECT ID FROM " + DECK_TABLE + " WHERE Name = ?))",
				"TestDeck");
			sqlite.delete(CARD_TABLE, "DeckID = (SELECT ID FROM " + DECK_TABLE + " WHERE Name = ?)", "TestDeck");
			sqlite.delete(DECK_TABLE, "Name = ?", "TestDeck");
			
			// Get all the cards, matched with their respective decks
			// Should only return two cards that are in deck Test2
			ArrayList<ArrayList<String>> rows = sqlite.select(DECK_TABLE + " d, " + CARD_TABLE + " c", "d.Name, c.Weight", "c.DeckID = d.ID", "");
			for (int i = 0; i < rows.size(); i++) {
				ArrayList<String> row = rows.get(i);
				System.out.print("Row " + i + ": ");
				for (int j = 0; j < row.size(); j++) {
					if (j > 0) {
						System.out.print('\t');
					}
					System.out.print(row.get(j));
				}
				System.out.println();
			}
			
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			sqlite.close();
		}
	}
	
	/**
	 * Load the existing decks<br>
	 * For performance reasons, empty Deck objects with only the names are loaded
	 */
	public void loadDecks() {
		try {
			ArrayList<ArrayList<String>> arr = sql.select(DECK_TABLE, "ID,Name", "", "");
			
			for (int i = 0; i < arr.size(); i++) {
				ArrayList<String> row = arr.get(i);
				decks.add(new Deck(Integer.parseInt(row.get(0)), row.get(1)));
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
			sql.close();
			System.exit(0);
		}
	}
	
	/**
	 * Loads the cards owned by the deck<br>
	 * For performance reasons, the deck's cards get the id and weight only
	 * 
	 * @param deck The Deck object to be filled out
	 */
	public void loadDeck(Deck deck) {
		try {
			String deckName = deck.getName();
			ArrayList<ArrayList<String>> arr = sql.select(CARD_TABLE, "ID,Weight",
						"DeckID = (SELECT ID FROM " + DECK_TABLE + " WHERE Name = ?", deckName);
			
			for (int i = 0; i < arr.size(); i++) {
				ArrayList<String> row = arr.get(i);
				Flashcard card = new Flashcard(Integer.parseInt(row.get(0)));
				card.setWeight(Integer.parseInt(row.get(1)));
				
				deck.addCard(card);
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
			sql.close();
			System.exit(0);
		}
	}
	
	/**
	 * Saves the deck, its cards, and the cards' sides to the database
	 * 
	 * @param deck The deck to save
	 */
	public void saveDeck(Deck deck) {
		try {
			if (deck.isDirty()) {
				sql.update(DECK_TABLE, "Name", deck.getName(), "ID = ?", String.valueOf(deck.getID()));
			}
			Vector<Flashcard> cards = deck.getCards();
			for (int i = 0; i < cards.size(); i++) {
				Flashcard card = cards.get(i);
				if (card.isDirty()) {
					sql.update(CARD_TABLE, "Weight", String.valueOf(card.getWeight()), "ID = ?", String.valueOf(card.getID()));
					
					Vector<FlashcardSide> sides = card.getSides();
					for (int j = 0; j < sides.size(); j++) {
						FlashcardSide side = sides.get(j);
						if (side.isDirty()) {
							sql.update(SIDE_TABLE, "Label,Text,Weight",
									side.getLabel() + "," + side.getText() + "," + side.getWeight(),
									"CardID = ?", String.valueOf(card.getID()));
						}
					}
				}
			}
			
		} catch (SQLiteException e) {
			e.printStackTrace();
			return;
		}
		
		
	}
	
	/**
	 * Adds a new deck to the list
	 * 
	 * @param deckName The name of the new deck
	 */
	public void addDeck(String deckName) {
		try {
			sql.insert(DECK_TABLE, "Name", deckName);
			int id = Integer.parseInt(sql.selectSingle("SELECT MAX(ID) FROM " + DECK_TABLE, ""));
			Deck deck = new Deck(id, deckName);
			deck.markDirty();
			decks.add(deck);
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Removed the specified deck from the list<br>
	 * Also removes all the deck's cards and their sides from the database<br>
	 * There is no recovering a deck once this method is called
	 * 
	 * @param deck The deck to remove
	 */
	public void removeDeck(Deck deck) {
		try {
			String deckID = String.valueOf(deck.getID());
			sql.delete(SIDE_TABLE, "CardId IN (SELECT ID FROM " + CARD_TABLE + " WHERE DeckID = ?)", deckID);
			sql.delete(CARD_TABLE, "DeckID = ?", deckID);
			sql.delete(DECK_TABLE, "ID = ?", deckID);
			decks.remove(new Deck(deck.getID(), deck.getName()));
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a new card to the specified deck
	 * 
	 * @param deck The deck to add the card to
	 * @param card The card to add
	 */
	public void addCardToDeck(Deck deck, Flashcard card) {
		try {
			sql.insert(CARD_TABLE, "DeckID,Weight", card.getID() + "," + card.getWeight());
			card.markDirty();
			deck.addCard(card);
			
			if (card.getSides().size() > 0) {
				Vector<FlashcardSide> sides = card.getSides();
				int cardID = card.getID();
				for (int i = 0; i < sides.size(); i++) {
					FlashcardSide side = sides.get(i);
					sql.insert(SIDE_TABLE, "CardID,Label,Text,Weight",
							cardID + "," + side.getLabel() + "," + side.getText() + "," + side.getWeight());
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
			return;
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
	public void removeCardFromDeck(Deck deck, Flashcard card) {
		try {
			sql.delete(SIDE_TABLE, "CardId = ?", String.valueOf(card.getID()));
			sql.delete(CARD_TABLE, "ID = ?", String.valueOf(card.getID()));
			deck.removeCard(card);
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a new side to the specified card
	 * 
	 * @param card The card to add the side to
	 * @param side The side to be added
	 */
	public void addSideToCard(Flashcard card, FlashcardSide side) {
		try {
			sql.insert(SIDE_TABLE, "CardID,Label,Text,Weight",
					card.getID() + "," + side.getLabel() + ","  + side.getText() + "," + side.getWeight());
			side.markDirty();
			card.addSide(side);
		} catch (SQLiteException e) {
			e.printStackTrace();
			return;
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
	public void removeSideFromCard(Flashcard card, FlashcardSide side) {
		try {
			sql.delete(SIDE_TABLE, "ID = ?", String.valueOf(side.getID()));
			card.removeSide(side);
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
	}
}