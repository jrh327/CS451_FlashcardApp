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

import com.almworks.sqlite4java.SQLiteException;

public class Main {
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
			sqlite.dropTable("Decks");
			sqlite.dropTable("Cards");
			sqlite.dropTable("Sides");
			
			// Create all the tables
			sqlite.createTable("Decks", "ID,Name", "INTEGER PRIMARY KEY ASC,CHAR(50) UNIQUE");
			sqlite.createTable("Cards", "ID,DeckID,Weight", "INTEGER PRIMARY KEY ASC,INTEGER,INTEGER");
			sqlite.createTable("Sides", "ID,CardID,Label,Text,Weight", "INTEGER PRIMARY KEY ASC,INTEGER,CHAR(50),CHAR(100),INTEGER");
			
			// Insert some rows into table Decks
			sqlite.insert("Decks", "Name", "TestDeck");
			sqlite.insert("Decks", "Name", "TestDeck2");
			
			// Insert some rows into table Cards
			sqlite.insert("Cards", "DeckID,Weight", "2,60");
			sqlite.insert("Cards", "DeckID,Weight", "2,50");
			sqlite.insert("Cards", "DeckID,Weight", "1,10");
			
			// Insert some sides for the first card in the first deck
			sqlite.insert("Sides", "CardID,Label,Text,Weight", "1,Test Label,Test Text,100");
			sqlite.insert("Sides", "CardID,Label,Text,Weight", "1,TestLabel,TestText,40");
			
			// Insert some sides for the second card in the second deck
			sqlite.insert("Sides", "CardID,Label,Text,Weight", "2,Chemical Name,Water,50");
			sqlite.insert("Sides", "CardID,Label,Text,Weight", "2,Chemical Formula, H20,40");
			
			// Change the name of deck TestDeck2 to Chemistry
			sqlite.update("Decks", "Name", "Chemistry", "ID = ?", "2");
			
			// Delete deck TestDeck, all its cards, and the sides of those cards
			sqlite.delete("Sides", "CardId IN (SELECT ID FROM Cards WHERE DeckID = (SELECT ID FROM Decks WHERE Name = ?))", "TestDeck");
			sqlite.delete("Cards", "DeckID = (SELECT ID FROM Decks WHERE Name = ?)", "TestDeck");
			sqlite.delete("Decks", "Name = ?", "TestDeck");
			
			// Get all the cards, matched with their respective decks
			// Should only return two cards that are in deck Test2
			ArrayList<ArrayList<String>> rows = sqlite.select("Decks d, Cards c", "d.Name, c.Weight", "c.DeckID = d.ID", "");
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
	
}