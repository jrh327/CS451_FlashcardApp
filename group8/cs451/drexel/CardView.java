/**
 * CardView.java: displays a card's sides
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

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class CardView extends JPanel {
	private SideView sideView;
	
	public CardView() {
		setLayout(new BorderLayout());
		
		//sideView = new SideView(null);
	}
}