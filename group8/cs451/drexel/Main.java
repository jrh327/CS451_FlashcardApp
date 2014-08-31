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

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.almworks.sqlite4java.SQLiteException;

public class Main {
	private Vector<Deck> decks;
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					//System.err.println("Couldn't use system look and feel.");
				}
				
				// Create and set up the window
				JFrame frame = new JFrame("FlipIt!");
				
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setPreferredSize(new Dimension(640, 480));
				//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				
				// Add content to the window
				frame.add(new MainScreen());
				
				//Display the window
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
	
	public Main() {
		//*
		SQLiteHandler sqlite;
		try {
			sqlite = new SQLiteHandler(Config.DATABASE);
		} catch (SQLiteException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			// Drop all the tables in case they already exist
			sqlite.dropTable(Config.DECK_TABLE);
			sqlite.dropTable(Config.CARD_TABLE);
			sqlite.dropTable(Config.SIDE_TABLE);
			
			// Create all the tables
			sqlite.createTable(Config.DECK_TABLE, "ID,Name", "INTEGER PRIMARY KEY ASC,CHAR(50) UNIQUE");
			sqlite.createTable(Config.CARD_TABLE, "ID,DeckID,Weight", "INTEGER PRIMARY KEY ASC,INTEGER,INTEGER");
			sqlite.createTable(Config.SIDE_TABLE, "ID,CardID,Label,Text,Weight", "INTEGER PRIMARY KEY ASC,INTEGER,CHAR(50),CHAR(100),INTEGER");
			
			// Insert some rows into table Decks
			sqlite.insert(Config.DECK_TABLE, "Name", "TestDeck");
			sqlite.insert(Config.DECK_TABLE, "Name", "TestDeck2");
			
			// Insert some rows into table Cards
			sqlite.insert(Config.CARD_TABLE, "DeckID,Weight", "2,60");
			sqlite.insert(Config.CARD_TABLE, "DeckID,Weight", "2,50");
			sqlite.insert(Config.CARD_TABLE, "DeckID,Weight", "1,10");
			
			// Insert some sides for the first card in the first deck
			sqlite.insert(Config.SIDE_TABLE, "CardID,Label,Text,Weight", "1,Test Label,Test Text,100");
			sqlite.insert(Config.SIDE_TABLE, "CardID,Label,Text,Weight", "1,TestLabel,TestText,40");
			
			// Insert some sides for the second card in the second deck
			sqlite.insert(Config.SIDE_TABLE, "CardID,Label,Text,Weight", "2,Chemical Name,Water,50");
			sqlite.insert(Config.SIDE_TABLE, "CardID,Label,Text,Weight", "2,Chemical Formula, H20,40");
			
			// Change the name of deck TestDeck2 to Chemistry
			sqlite.update(Config.DECK_TABLE, "Name", "Chemistry", "ID = ?", "2");
			
			// Delete deck TestDeck, all its cards, and the sides of those cards
			sqlite.delete(Config.SIDE_TABLE,
				"CardId IN (SELECT ID FROM " + Config.CARD_TABLE + " WHERE DeckID = (SELECT ID FROM " + Config.DECK_TABLE + " WHERE Name = ?))",
				"TestDeck");
			sqlite.delete(Config.CARD_TABLE, "DeckID = (SELECT ID FROM " + Config.DECK_TABLE + " WHERE Name = ?)", "TestDeck");
			sqlite.delete(Config.DECK_TABLE, "Name = ?", "TestDeck");
			
			// Get all the cards, matched with their respective decks
			// Should only return two cards that are in deck Test2
			ArrayList<ArrayList<String>> rows = sqlite.select(Config.DECK_TABLE + " d, " + Config.CARD_TABLE + " c", "d.Name, c.Weight", "c.DeckID = d.ID", "");
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
		//*/
	}
}