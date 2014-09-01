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
	private int id;
	private int weight;
	private final int MIN_WEIGHT = 0;
	private final int MAX_WEIGHT = 100;
	private Vector<FlashcardSide> sides;
	private boolean isDirty = false;
	
	public Flashcard(int id) {
		this.id = id;
		this.weight = (MAX_WEIGHT + MIN_WEIGHT) / 2;
		this.sides = new Vector<FlashcardSide>();
	}
	
	public Flashcard(int id, Vector<FlashcardSide> sides) {
		this.id = id;
		this.weight = (MAX_WEIGHT + MIN_WEIGHT) / 2;
		this.sides = sides;
	}
	
	public Flashcard(int id, Vector<FlashcardSide> sides, int weight) {
		this.id = id;
		this.weight = boundWeight(weight);
		this.sides = sides;
	}
	
	public int getID() {
		return this.id;
	}
	
	public int getWeight() {
		return this.weight;
	}
	
	public Vector<FlashcardSide> getSides() {
		return this.sides;
	}
	
	public FlashcardSide getRandomSide() {
		if (null == this.sides || this.sides.size() == 0) {
			return null;
		}
		
		// TODO: return an actually random side
		return this.sides.get(0);
	}
	
	public FlashcardSide getWeightedSide() {
		if (null == this.sides || this.sides.size() == 0) {
			return null;
		}
		
		// TODO: return random side based on weights
		return this.sides.get(0);
	}
	
	public void setWeight(int weight) {
		this.weight = boundWeight(weight);
		this.isDirty = true;
	}
	
	public void addSide(FlashcardSide side) {
		if (null == this.sides) {
			this.sides = new Vector<FlashcardSide>();
		}
		if (this.sides.contains(side)) {
			return;
		}
		this.sides.add(side);
		this.isDirty = true;
	}
	
	public void setSides(Vector<FlashcardSide> sides) {
		this.sides = sides;
	}
	
	public void removeSide(FlashcardSide side) {
		this.sides.remove(side);
	}
	
	public boolean isDirty() {
		return this.isDirty;
	}
	
	public void markDirty() {
		this.isDirty = true;
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
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Flashcard)) {
			return false;
		}
		
		Flashcard f = (Flashcard)o;
		if (this.weight != f.weight) {
			return false;
		}
		if (this.sides.size() != f.sides.size()) {
			return false;
		}
		for (int i = 0; i < this.sides.size(); i++) {
			if (!this.sides.get(i).equals(f.sides.get(i))) {
				return false;
			}
		}
		
		return true;
	}
	
	
	private String toString = "";
	public void toString(String string) {
		this.toString = string;
	}
	@Override
	public String toString() {
		return this.toString;
	}
}