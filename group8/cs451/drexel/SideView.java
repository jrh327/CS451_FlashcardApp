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
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class SideView extends JPanel {
	FlashcardSide side;
	
	public SideView(FlashcardSide side) {
		this.side = side;
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new EtchedBorder()));
		
		JPanel info = new JPanel();
		info.setBackground(Color.WHITE);
		info.setLayout(new BorderLayout());
		
		JLabel label = new JLabel("This is the " + side.getLabel());
		JLabel text = new JLabel(side.getText());
		label.setHorizontalAlignment(SwingConstants.CENTER);
		text.setHorizontalAlignment(SwingConstants.CENTER);
		
		info.add(label, BorderLayout.NORTH);
		info.add(text, BorderLayout.CENTER);
		
		add(info, BorderLayout.CENTER);
	}
}