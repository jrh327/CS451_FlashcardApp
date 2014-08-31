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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class SideView extends JPanel implements MouseListener {
	private FlashcardSide side;
	private boolean listeningForClick = false;
	private JPanel parent;
	
	public SideView(JPanel parent, FlashcardSide side) {
		this.side = side;
		this.parent = parent;
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new EtchedBorder()));
		
		JPanel info = new JPanel();
		info.setBackground(Color.WHITE);
		info.setLayout(new BorderLayout());
		
		JLabel label = new JLabel(side.getLabel());
		JLabel text = new JLabel(side.getText());
		label.setHorizontalAlignment(SwingConstants.CENTER);
		text.setHorizontalAlignment(SwingConstants.CENTER);
		
		info.add(label, BorderLayout.NORTH);
		info.add(text, BorderLayout.CENTER);
		
		add(info, BorderLayout.CENTER);
		
		addMouseListener(this);
	}
	
	/**
	 * Force the view to update its values
	 */
	public void refresh() {
		removeAll();
		
		JPanel info = new JPanel();
		info.setBackground(Color.WHITE);
		info.setLayout(new BorderLayout());
		
		JLabel label = new JLabel(side.getLabel());
		JLabel text = new JLabel(side.getText());
		label.setHorizontalAlignment(SwingConstants.CENTER);
		text.setHorizontalAlignment(SwingConstants.CENTER);
		
		info.add(label, BorderLayout.NORTH);
		info.add(text, BorderLayout.CENTER);
		
		add(info, BorderLayout.CENTER);
		
		revalidate();
	}
	
	/**
	 * Begin or stop listening for mouse clicks
	 * 
	 * @param b Whether or not to listen for clicks
	 */
	public void listenForMouseClicks(boolean b) {
		listeningForClick = b;
	}
	
	/**
	 * Returns the FlashcardSide object being represented by this panel
	 * 
	 * @return The FlashcardSide being represented
	 */
	public FlashcardSide getSide() {
		return side;
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
		if (!listeningForClick) {
			return;
		}
		if (parent instanceof MainScreen) {
			((MainScreen)parent).sideViewClicked(this);
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent event) {
		
	}
	
	@Override
	public void mouseExited(MouseEvent event) {
		
	}
	
	@Override
	public void mousePressed(MouseEvent event) {
		
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}
}