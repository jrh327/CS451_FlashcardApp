/**
 * Flashcard.java: object for a flashcard
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

public class Flashcard {
	private int weight;
	private final int MIN_WEIGHT = 0;
	private final int MAX_WEIGHT = 100;
	private Vector<FlashcardSide> sides;
	
	public Flashcard() {
		this.weight = (MAX_WEIGHT + MIN_WEIGHT) / 2;
		this.sides = new Vector<FlashcardSide>();
	}
	
	public Flashcard(Vector<FlashcardSide> sides) {
		this.weight = (MAX_WEIGHT + MIN_WEIGHT) / 2;
		this.sides = copySides(sides);
	}
	
	public Flashcard(Vector<FlashcardSide> sides, int weight) {
		this.weight = boundWeight(weight);
		this.sides = copySides(sides);
	}
	
	public int getWeight() {
		return this.weight;
	}
	
	public Vector<FlashcardSide> getSides() {
		return copySides(this.sides);
	}
	
	public FlashcardSide getRandomSide() {
		if (null == this.sides || this.sides.size() == 0) {
			return null;
		}
		
		return this.sides.get(0);
	}
	
	public FlashcardSide getWeightedSide() {
		if (null == this.sides || this.sides.size() == 0) {
			return null;
		}
		
		// TODO: return random side based on weights
		return this.sides.get(0);
	}
	
	private int boundWeight(int w) {
		if (w < MIN_WEIGHT) {
			return MIN_WEIGHT;
		} else if (w > MAX_WEIGHT) {
			return MAX_WEIGHT;
		} else {
			return w;
		}
	}
	
	private Vector<FlashcardSide> copySides(Vector<FlashcardSide> sides) {
		if (null == sides || sides.size() == 0) {
			return new Vector<FlashcardSide>();
		}
		
		Vector<FlashcardSide> copy = new Vector<FlashcardSide>();
		int numSides = sides.size();
		
		for (int i = 0; i < numSides; i++) {
			FlashcardSide curSide = sides.get(i);
			FlashcardSide newSide = new FlashcardSide(curSide.getLabel(), curSide.getText(), curSide.getWeight());
			copy.add(newSide);
		}
		
		return copy;
	}
}
