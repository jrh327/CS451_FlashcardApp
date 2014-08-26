package group8.cs451.drexel;

public class FlashcardSide {
	private int weight;
	private final int MIN_WEIGHT = 0;
	private final int MAX_WEIGHT = 100;
	private String text;
	private String label;
	//private Image image;
	//private Audio audio;
	
	public FlashcardSide() {
		this.weight = (MAX_WEIGHT + MIN_WEIGHT) / 2;
		this.text = "";
		this.label = "";
	}
	
	public FlashcardSide(String label, String text) {
		this.weight = (MAX_WEIGHT + MIN_WEIGHT) / 2;
		this.text = text;
		this.label = label;
	}
	
	public FlashcardSide(String label, String text, int weight) {
		this.weight = boundWeight(weight);
		this.text = text;
		this.label = label;
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
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setLabel(String label) {
		this.label = label;
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
}
