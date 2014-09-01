/**
 * MyDictionary.java: 
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

import java.util.HashMap;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Rishir
 */
public class MyDictionary implements Serializable{
	
	/**
	 * @param args the command line arguments
	 */
	private static final long serialVersionUID = 1001L;
	
	/*
            My dictionary structure.
             Map       Map       Array
            | WORD |->| TYPE |->| DEFINITION |
            ///////////////////////////////
            Outer HashMap->Stores word/type key/value pair
                Inner HashMap->Stores type/definition key/value pair
                    Innermost Arraylist->Stores definitions for a specific type
            ///////////////////////////////
            type refers to different uses of a word such as noun, adjective, prefic, etc.
            
	 */
	private HashMap<String, HashMap<String, ArrayList<String>>> dictionary;
	
	public MyDictionary() {
		dictionary = new HashMap<>();
	}
	
	//Can be used to copy dictionaries
	//im using this so that it can deserialize the saved .ser file
	public MyDictionary(HashMap<String, HashMap<String, ArrayList<String>>> dictionary) {
		for(Map.Entry<String, HashMap<String, ArrayList<String>>> word : dictionary.entrySet())
		{
			for(Map.Entry<String, ArrayList<String>> type : word.getValue().entrySet())
			{
				for(String definition : type.getValue())
				{
					insertWord(word.getKey(), type.getKey(), definition);
				}
			}
		}
	}
	
	//Inserts words into dictionary
	public final void insertWord(String word, String type, String definition) {
		word = word.toUpperCase();
		if( dictionary.get(word) != null )
		{
			if( (dictionary.get(word)).get(type) != null ) 
			{
				((dictionary.get(word)).get(type)).add(definition);
			} else 
			{
				ArrayList<String> alDefinition = new ArrayList<>();
				(dictionary.get(word)).put(type, alDefinition);
				((dictionary.get(word)).get(type)).add(definition);
			}
		} else
		{
			ArrayList<String> alDefinition = new ArrayList<>();
			alDefinition.add(definition);
			HashMap<String, ArrayList<String>> hmType = new HashMap<>();
			hmType.put(type, alDefinition);
			
			dictionary.put(word, hmType);
		}
	}
	
	//returns a hashmap with all uses and definitions of a word
	public HashMap<String, ArrayList<String>> getWord(String word) {
		word = word.toUpperCase();
		return dictionary.get(word);
	}
	
	//returns a specific definition
	public ArrayList<String> getDefinition(String word, String type) {
		word = word.toUpperCase();
		return (dictionary.get(word)).get(type);
	}
	
	//returns size of dictionary -> number of words in dictionary
	public int getSize() {
		return dictionary.size();
	}
}