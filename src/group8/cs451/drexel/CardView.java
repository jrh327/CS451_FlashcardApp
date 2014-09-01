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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CardView extends JPanel {
	public CardView() {
		setLayout(new BorderLayout());
	}
	
	/**
	 * Sets the panel to use to display the card side
	 * Should be either a SideView or an EditSideView
	 * 
	 * @param side The panel to display the card side
	 *             If null, a default message is displayed
	 */
	public void setSide(JPanel side) {
		if (null == side) {
			removeAll();
			JLabel msg = new JLabel("Click a side of the card to view it");
			msg.setHorizontalAlignment(SwingConstants.CENTER);
			add(msg, BorderLayout.CENTER);
			return;
		}
		
		add(side, BorderLayout.CENTER);
	}
}