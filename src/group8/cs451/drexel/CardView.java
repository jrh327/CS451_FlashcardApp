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
	private FlashcardSide side = null;
	
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
			this.side = null;
			return;
		}
		
		BorderLayout layout = (BorderLayout)getLayout();
		remove(layout.getLayoutComponent(BorderLayout.CENTER));
		
		add(side, BorderLayout.CENTER);
		if (side instanceof SideView) {
			this.side = ((SideView)side).getSide();
		} else if (side instanceof EditSideView) {
			this.side = ((EditSideView)side).getSide();
		}
		validate();
	}
	
	public FlashcardSide getSide() {
		return side;
	}
}