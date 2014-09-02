/**
 * DeckReview.java: flip through a deck to study
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class DeckReview extends JPanel {
	private Deck deck;
	JPanel buttonsPanel;
	
	public DeckReview(Deck deck) {
		this.deck = deck;
		
		DeckOperations.loadDeck(deck);
		Vector<Flashcard> cards = deck.getCards();
		int numCards = cards.size();
		for (int i = 0; i < numCards; i++) {
			DeckOperations.loadCard(cards.get(i));
		}
		
		setLayout(new BorderLayout());
		
		buttonsPanel = new JPanel();
		//buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		buttonsPanel.setLayout(new BorderLayout());
		
		JButton checkButton = checkButton();
		buttonsPanel.add(checkButton, BorderLayout.CENTER);
		
		CardView cardView = new CardView();
		
		Flashcard card = getWeightedCard(deck.getCards());
		FlashcardSide side = getWeightedSide(card.getSides());
		
		cardView.setSide(new SideView(cardView, side));
		
		add(buttonsPanel, BorderLayout.EAST);
		add(cardView, BorderLayout.CENTER);
	}
	
	private Flashcard getWeightedCard(Vector<Flashcard> cards) {
		if (null == cards || cards.size() == 0) {
			System.out.println("no cards");
			return null;
		}
		
		// TODO: return random card based on weights
		return cards.get(0);
	}
	
	private FlashcardSide getWeightedSide(Vector<FlashcardSide> sides) {
		if (null == sides || sides.size() == 0) {
			System.out.println("no sides");
			return null;
		}
		
		// TODO: return random side based on weights
		return sides.get(0);
	}
	
	private JButton checkButton() {
		JButton checkButton = new JButton("Check");
		checkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				buttonsPanel.removeAll();
				
				JPanel p = new JPanel();
				p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
				
				JPanel correct = new JPanel();
				correct.setLayout(new BorderLayout());
				correct.add(correctButton(), BorderLayout.CENTER);
				
				JPanel incorrect = new JPanel();
				incorrect.setLayout(new BorderLayout());
				incorrect.add(incorrectButton(), BorderLayout.CENTER);
				
				p.add(correct);
				p.add(incorrect);
				
				buttonsPanel.add(p, BorderLayout.CENTER);
				validate();
			}
		});
		
		return checkButton;
	}
	
	private JButton correctButton() {
		JButton correctButton = new JButton("Correct");
		correctButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				buttonsPanel.removeAll();
				buttonsPanel.add(checkButton());
				validate();
			}
		});
		
		return correctButton;
	}
	
	private JButton incorrectButton() {
		JButton incorrectButton = new JButton("Incorrect");
		incorrectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				buttonsPanel.removeAll();
				buttonsPanel.add(checkButton());
				validate();
			}
		});
		
		return incorrectButton;
	}
}