/**
 * DictionaryMain.java: 
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author patelrn
 */
public class DictionaryMain {
	public static void main(String[] args) throws IOException {
		FlashDictionary dictionary = new FlashDictionary();
		ArrayList<String> def;
		HashMap<String, ArrayList<String>> mydefinition;
		/*
                Insert word example
                
                dictionary.insertWord("sdf", "Sdf","k");
                dictionary.insertWord("sdf", "Sdf","lmfao");
                dictionary.insertWord("sdf", "khf","lmfao");
                dictionary.insertWord("Rishir", "noun","creator");*/

                
		/*
                get definition example
                
                def = dictionary.getDefinition("Rishir", "noun");
                
                for( String definition : def) 
                {
                    System.out.println(definition);
                }
		 */
		
		//importDictionary(dictionary);
		DictionaryIO dicOut = new DictionaryIO();
		//dicOut.saveDictionary(dictionary);
		dictionary = dicOut.loadDictionary();
		System.out.println("Dicitonary word count = " + dictionary.getSize());
		
		
		//This retrives all definitions for the word A
		//remember getWord returns a HASHMAP of format HashMap<String, ArrayList<String>>
		mydefinition = dictionary.getWord("A");
		System.out.println("A");
		for(Map.Entry<String, ArrayList<String>> entry : mydefinition.entrySet())
		{
			System.out.println(entry.getKey());
			for(String definition : entry.getValue())
			{
				System.out.println(definition);
			}
			System.out.printf("\n\n");
		}              
		
	}
	
	
	/*
            importDictionary
            Imports the specified dictionary into MyDictionary class object
	 */
	public static void importDictionary(FlashDictionary dictionary) throws IOException {
		File dictFile = new File("gutenbergDictionary.txt");
		BufferedReader in = null;
		try {
			try {
				in = new BufferedReader(
						new InputStreamReader(new FileInputStream(dictFile), "UTF-8"));
			} catch (FileNotFoundException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
			}
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		String word = "";
		String type = "";
		String definition = "";
		
		String str;
		String prvStr = " ";
		String numDefMarkRE = "^[\\pN]+.*";
		String letDefMarkRE = "^(Defn:)+.*";
		String defMark = "";
		boolean defMarkFound = false;
		boolean printReg = false;
		boolean wordFound = false;
		boolean lookForDefinition = false;
		
		int linecount = 0;
		
		if(in != null) {
			while ((str = in.readLine()) != null) {
				linecount++;
				if(linecount%5000 == 0) {
					System.out.println("now on line " + linecount + "...");
				}
				if(wordFound) 
				{
					//System.out.println(str + "  <--- ETYM/TYPE FOUND");
					type = str;
					wordFound = false;
					lookForDefinition = true;
				} else 
				{
					if( isAllUpperCase(str) && !(str.equals(prvStr)) )
					{
						if(!(word.isEmpty()))
						{
							dictionary.insertWord(word, type, definition);
						}  
						wordFound = true;
						lookForDefinition = false;
						defMarkFound = false;
						//System.out.println(str + "  <--- WORD FOUND");
						word = str;
						printReg = false;
					}else
					{
						printReg = true;
					}
					if(lookForDefinition) 
					{
						String checktype;
						if(str.length() > 5)
						{
							checktype = str.substring(0, 6);
						}else
						{
							checktype = str;
						}
						
						if(!defMarkFound)
						{
							if(checktype.matches(numDefMarkRE))
							{
								defMarkFound = true;
								defMark = numDefMarkRE;
								//System.out.println(str + "  <--- DEFN FOUND");
								definition = str;
								printReg = false;
							}else if(checktype.matches(letDefMarkRE))
							{
								defMarkFound = true;
								defMark = letDefMarkRE;
								//System.out.println(str + "  <--- DEFN FOUND");
								definition = str;
								printReg = false;
							}else
							{
								printReg = true;
							}
						} else
						{
							if(checktype.matches(defMark))
							{
								//System.out.println(str + "  <--- DEFN FOUND");
								printReg = false;
								dictionary.insertWord(word, type, definition);
								definition = str;
							}else
							{
								definition += " " + str;
								printReg = true;
							}                   
						}
					}
					if(printReg) 
					{
						//System.out.println(str);
					}
					prvStr = str;                        
				}
			}
		}
	}
	
	/*
         Used by importDictionary to check if a line is all uppercase letters
         This is how a word is defined in the dictionary txt file
	 */
	public static boolean isAllUpperCase(String str) {
		str = str.replaceAll("[\\s+';-]", "");
		if(str == null || str.isEmpty() || !(str.matches("^[\\pL\\pN]+-?$")))
		{
			return false;
		}
		int size = str.length();
		for (int i = 0; i < size; i++)
		{
			if (Character.isLetter(str.charAt(i)) && (str.charAt(i)) != ' ')
			{
				if (!(Character.isUpperCase(str.charAt(i))))
				{
					return false;
				}
			}
		}
		return true;
	}
}