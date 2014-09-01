/**
 * FlashcardSide.java: object for a side of a card
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

public class FlashcardSide {
	private int id;
	private int weight;
	private String text;
	private String label;
	private boolean isDirty = false;
	//private Image image;
	//private Audio audio;
	
	public FlashcardSide(int id) {
		this.id = id;
		this.weight = (Config.MAX_WEIGHT + Config.MIN_WEIGHT) / 2;
		this.text = "";
		this.label = "";
	}
	
	public FlashcardSide(int id, String label, String text) {
		this.id = id;
		this.weight = (Config.MAX_WEIGHT + Config.MIN_WEIGHT) / 2;
		this.text = text;
		this.label = label;
	}
	
	public FlashcardSide(int id, String label, String text, int weight) {
		this.id = id;
		this.weight = boundWeight(weight);
		this.text = text;
		this.label = label;
	}
	
	public int getID() {
		return this.id;
	}
	
	public int getWeight() {
		return this.weight;
	}
	
	public String getText() {
		return this.text;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public void setWeight(int weight) {
		this.weight = boundWeight(weight);
		this.isDirty = true;
	}
	
	public void setText(String text) {
		this.text = text;
		this.isDirty = true;
	}
	
	public void setLabel(String label) {
		this.label = label;
		this.isDirty = true;
	}
	
	public boolean isDirty() {
		return this.isDirty;
	}
	
	public void markDirty() {
		this.isDirty = true;
	}
	
	public void markClean() {
		this.isDirty = false;
	}
	
	private int boundWeight(int w) {
		if (w < Config.MIN_WEIGHT) {
			return Config.MIN_WEIGHT;
		} else if (w > Config.MAX_WEIGHT) {
			return Config.MAX_WEIGHT;
		} else {
			return w;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof FlashcardSide)) {
			return false;
		}
		FlashcardSide fs = (FlashcardSide)o;
		return (this.weight == fs.weight && this.label.equals(fs.label) && this.text.equals(fs.text));
	}
}
