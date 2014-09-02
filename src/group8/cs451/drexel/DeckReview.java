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
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DeckReview extends JPanel {
	private Deck deck;
	private Flashcard displayedCard;
	private FlashcardSide displayedSide;
	private FlashcardSide guessedSide;
	private JPanel buttonsPanel;
	
	public DeckReview(Deck deck) throws NumberFormatException, UnsupportedEncodingException {
		this.deck = deck;
		
		DeckOperations.loadDeck(deck);
		Vector<Flashcard> cards = deck.getCards();
		int numCards = cards.size();
		for (int i = 0; i < numCards; i++) {
			DeckOperations.loadCard(cards.get(i));
		}
		
		setLayout(new BorderLayout());
		
		setupDeckReview();
	}
	
	private void setupDeckReview() {
		DeckOperations.saveDeck(deck);
		
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BorderLayout());
		
		JButton checkButton = checkButton();
		buttonsPanel.add(checkButton, BorderLayout.CENTER);
		
		CardView cardView = new CardView();
		
		displayedCard = getWeightedCard(deck.getCards());
		displayedSide = null;
		displayedSide = getWeightedSide(displayedCard.getSides());
		guessedSide = getWeightedSide(displayedCard.getSides());
		
		cardView.setSide(new SideView(cardView, displayedSide));
		
		JLabel question = new JLabel("What is the " + guessedSide.getLabel() + "?");
		question.setHorizontalAlignment(SwingConstants.CENTER);
		
		BorderLayout layout = (BorderLayout)getLayout();
		Component c = layout.getLayoutComponent(BorderLayout.SOUTH);
		if (null != c) {
			remove(c);
		}
		c = layout.getLayoutComponent(BorderLayout.EAST);
		if (null != c) {
			remove(c);
		}
		c = layout.getLayoutComponent(BorderLayout.CENTER);
		if (null != c) {
			remove(c);
		}
		
		add(buttonsPanel, BorderLayout.EAST);
		add(question, BorderLayout.SOUTH);
		add(cardView, BorderLayout.CENTER);
	}
	
	private Flashcard getWeightedCard(Vector<Flashcard> cards) {
		if (null == cards || cards.size() == 0) {
			return null;
		}
		
		// get selection threshold
		Random rand = new Random();
		double threshold = rand.nextDouble();
		
		// Shuffle deck without modifying original
		Vector<Flashcard> dup = new Vector<Flashcard>(cards);
		Collections.shuffle(dup);
		
		// Select first card in our shuffled deck that passes the threshold
		Flashcard ret = null;
		for (int i = 0; i < dup.size() && ret == null; i++) {
			// Cards with high weights are difficult
			// Cards with low weights are easy
			// The threshold is a minimum difficulty to select
			// If we're at the end of the deck and no card has passed the test, select the last card
			// The last card then effectively becomes a random draw
			if (dup.get(i).getWeight() >= threshold || i == dup.size()-1)
				ret = dup.get(i);
		}
		
		return ret;
	}
	
	private FlashcardSide getWeightedSide(Vector<FlashcardSide> sides) {
		if (null == sides || sides.size() == 0) {
			return null;
		}
		if (sides.size() == 1) {
			return sides.get(0);
		}
		
		FlashcardSide side = displayedSide;
		Random rand = new Random();
		
		// keep trying until we get a side that isn't the side being displayed
		while (side == displayedSide) {
			// TODO: return a random side based on weights
			int randomNum = rand.nextInt(sides.size());
			side = sides.get(randomNum);
		}
		
		return side;
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
				
				CardView cardView = new CardView();
				cardView.setSide(new SideView(cardView, guessedSide));
				
				BorderLayout layout = (BorderLayout)getLayout();
				remove(layout.getLayoutComponent(BorderLayout.SOUTH));
				remove(layout.getLayoutComponent(BorderLayout.CENTER));
				
				add(cardView, BorderLayout.CENTER);
				
				validate();
			}
		});
		
		return checkButton;
	}
	
	private JButton correctButton() {
		JButton correctButton = new JButton("Correct");
		correctButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// decrement weight for selected card
				System.out.println("[" + guessedSide.getLabel() + "]" + " " + guessedSide.getWeight() + " -> " + (guessedSide.getWeight() - 1));
				guessedSide.setWeight(guessedSide.getWeight() - 1);

				displayedCard.updateWeight();
				
				setupDeckReview();
				validate();
			}
		});
		
		return correctButton;
	}
	
	private JButton incorrectButton() {
		JButton incorrectButton = new JButton("Incorrect");
		incorrectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// increment weight for selected card
				System.out.println("[" + guessedSide.getLabel() + "]" + " " + guessedSide.getWeight() + " -> " + (guessedSide.getWeight() + 1));
				guessedSide.setWeight(guessedSide.getWeight() + 1);

				displayedCard.updateWeight();
				
				setupDeckReview();
				validate();
			}
		});
		
		return incorrectButton;
	}
}
