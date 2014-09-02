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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.almworks.sqlite4java.SQLiteException;

public class Main {
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				initialSetup();
				
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
	
	private static void initialSetup() {
		SQLiteHandler sqlite;
		try {
			sqlite = new SQLiteHandler(Config.DATABASE);
		} catch (SQLiteException e) {
			e.printStackTrace();
			return;
		}
		
		// Create all the tables if they don't exist yet
		// This should only happen the first time the application is run on a computer
		try {
			sqlite.createTable(Config.DECK_TABLE, "ID,Name", "INTEGER PRIMARY KEY ASC,CHAR(50) UNIQUE");
			sqlite.createTable(Config.CARD_TABLE, "ID,DeckID,Weight", "INTEGER PRIMARY KEY ASC,INTEGER,INTEGER DEFAULT 0");
			sqlite.createTable(Config.SIDE_TABLE, "ID,CardID,Label,Text,Weight", "INTEGER PRIMARY KEY ASC,INTEGER,TEXT DEFAULT '',TEXT DEFAULT '',INTEGER DEFAULT 0");
			if (sqlite.select(Config.DECK_TABLE, "Name", "Name = ?", "Dictionary").isEmpty()) {

				sqlite.insert(Config.DECK_TABLE, "Name", "Dictionary");
				long id = sqlite.getLastInsertId();
				String deckid = String.valueOf(id);
				/*sqlite.insert(Config.CARD_TABLE, "DeckID,Weight", deckid + ",50");
				String cardid = String.valueOf(sqlite.getLastInsertId());
				sqlite.insert(Config.SIDE_TABLE, "CardID,Label,Text,Weight", cardid + ",,Word,50");
				sqlite.insert(Config.SIDE_TABLE, "CardID,Label,Text,Weight", cardid + ",,Definition,50");*/
				DictionaryIO dicOut = new DictionaryIO();
				Config.dictionary = dicOut.loadDictionary();
				int count = 0;
				for( Map.Entry<String, HashMap<String, ArrayList<String>>> word : ((Config.dictionary).getDictionary()).entrySet() )
				{
					count++;
					sqlite.insert(Config.CARD_TABLE, "DeckID,Weight", deckid + ",50");
					String cardid = String.valueOf(sqlite.getLastInsertId());
					for(Map.Entry<String, ArrayList<String>> type : word.getValue().entrySet())
					{
						String encodedkey = "";
						
						try {
							encodedkey = URLEncoder.encode(type.getKey(), "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						sqlite.insert(Config.SIDE_TABLE, "CardID,Label,Text,Weight", cardid + ",," + word.getKey()+"\n"+encodedkey + ",100");
						for(String definition : type.getValue())
						{
							try {
								encodedkey = URLEncoder.encode(definition, "UTF-8");
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							sqlite.insert(Config.SIDE_TABLE, "CardID,Label,Text,Weight", cardid + ",," + encodedkey +",50");
						}	
					}
					if(count%2000 == 0)
					{
						System.out.println(count);
					}
				}
				System.out.println(count);
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			sqlite.close();
		}

		/*try {
			sqlite = new SQLiteHandler(Config.DATABASE);
		} catch (SQLiteException e) {
			e.printStackTrace();
			return;
		}
		String alldefinition = "";
		int maxdefinitionlength = 0;
		int maxtypelength = 0;
		int maxwordlength = 0;
		System.out.println("Initializing dictionary table in database..");
		for( Map.Entry<String, HashMap<String, ArrayList<String>>> word : ((dictionary).getDictionary()).entrySet() )
		{
			for(Map.Entry<String, ArrayList<String>> type : word.getValue().entrySet())
			{
				alldefinition = "";
				for(String definition : type.getValue())
				{
					alldefinition += definition;
				}
				String toInsert = word.getKey() + "," + type.getKey() + "," + alldefinition;
				if (toInsert.length() > maxdefinitionlength)
				{
					maxdefinitionlength = toInsert.length();
				}
				if (type.getKey().length() > maxtypelength)
				{
					maxtypelength = type.getKey().length();
				}
				try {
					sqlite.insert(Config.DICTIONARY_TABLE, "Word,Type,Definition", toInsert);
				} catch (SQLiteException e) {
					e.printStackTrace();
				} finally {
					sqlite.close();
				}		
					
			}
			if (word.getKey().length() > maxwordlength)
			{
				maxwordlength = word.getKey().length();
			}
		}
		System.out.println("Dictionary initialized!");
		System.out.println("Max definition length " + maxdefinitionlength);
		System.out.println("Max type length " + maxtypelength);
		System.out.println("Max word length " + maxwordlength);*/
	}
	
	public Main() {
		/*
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