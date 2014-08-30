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
			sqlite.createTable("Decks", "ID,Name", "INTEGER PRIMARY KEY ASC,CHAR(50) UNIQUE");
			sqlite.createTable("Cards", "ID,DeckID,Weight", "INTEGER PRIMARY KEY ASC,INTEGER,INTEGER");
			sqlite.createTable("Sides", "ID,CardID,Label,Text,Weight", "INTEGER PRIMARY KEY ASC,INTEGER,CHAR(50),CHAR(100),INTEGER");
			
			sqlite.insert("Decks", "Name", "TestDeck");
			sqlite.insert("Decks", "Name", "TestDeck2");
			sqlite.insert("Decks", "Name", "TestDeck3");
			
			sqlite.update("Decks", "Name", "Test2", "ID = ?", "2");
			
			ArrayList<String> arr = sqlite.selectColumn("Decks", "Name", "", "");
			for (int i = 0; i < arr.size(); i++) {
				System.out.println("Got from Table Decks: " + arr.get(i));
			}
			
			sqlite.delete("Decks", "Name = ?", "TestDeck2");
			
			ArrayList<ArrayList<String>> rows = sqlite.select("Decks", "*", "", "");
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