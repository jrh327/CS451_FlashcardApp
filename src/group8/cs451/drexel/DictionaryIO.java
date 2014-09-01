/**
 * DictionaryIO.java: 
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author patelrn
 */

//Used to serialize and deserialize dictionary
public class DictionaryIO {
	private String filename = "flashcard_dictionary.ser";
	
	public void saveDictionary(MyDictionary dictionary) {
		
		File file = new File(filename);
		FileOutputStream fileOut;
		
		try {
			file.createNewFile();
			
			try {
				fileOut = new FileOutputStream(file);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				System.out.println("attempting to save dictionary...");
				out.writeObject(dictionary);
				out.close();
				fileOut.close();
				System.out.println("Dictionary saved to " + filename + ".");
			} catch (FileNotFoundException ex) {
				Logger.getLogger(DictionaryIO.class.getName()).log(Level.SEVERE, null, ex);
			}
		} catch (IOException ex) {
			Logger.getLogger(DictionaryIO.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public MyDictionary loadDictionary() {
		MyDictionary inDict = null;
		
		try {
			try (FileInputStream fileIn = new FileInputStream(filename)) {
				System.out.println("attempting to load dictionary...");
				try (ObjectInputStream in = new ObjectInputStream(fileIn)) {
					inDict = (MyDictionary)in.readObject();
					System.out.println("Dictionary successfully loaded!");
				}
			}
			
		} catch (FileNotFoundException ex) {
			Logger.getLogger(DictionaryIO.class.getName()).log(Level.SEVERE, null, ex);
			
		} catch (IOException | ClassNotFoundException ex) {
			Logger.getLogger(DictionaryIO.class.getName()).log(Level.SEVERE, null, ex);
			
		}
		
		return inDict;
	}
}