/**
 * MainScreen.java: displays the main view of the application
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
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;



import com.almworks.sqlite4java.SQLiteException;

public class MainScreen extends JPanel implements TreeSelectionListener {
	private JPanel sidesList;
	private CardView cardView;
	private Vector<Deck> decks;
	
	public MainScreen() {
		decks = DeckOperations.loadDecks();
		
		setLayout(new BorderLayout());
		
		cardView = new CardView();
		
		
		sidesList = new JPanel();
		sidesList.setLayout(new BoxLayout(sidesList, BoxLayout.Y_AXIS));
		
		JPanel pDecks = setupDecksTree();
		
		add(new JScrollPane(pDecks), BorderLayout.WEST);
		add(new JScrollPane(sidesList), BorderLayout.EAST);
		add(cardView, BorderLayout.CENTER);
	}
	
	public void sideViewClicked(SideView sideView) {
		cardView.setSide(new EditSideView(sideView));
		revalidate();
	}
	
	public JPanel setupDecksTree() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Decks");
		createDecksNodes(top);
		
		final JTree decksTree = new JTree(top);
		decksTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		//Listen for when the selection changes.
		decksTree.addTreeSelectionListener(this);
		JPanel pDecks = new JPanel();
		JButton addDeck = new JButton("Add");
		addDeck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Component source = (Component)event.getSource();
				String response = JOptionPane.showInputDialog(source, "Enter name of new deck:", "Add a new deck", JOptionPane.QUESTION_MESSAGE);
				if (null != response && !response.isEmpty()) {
					DeckOperations.addDeck(decks, response);
					JPanel pDecks = setupDecksTree();
					
					BorderLayout layout = (BorderLayout)getLayout();
					remove(layout.getLayoutComponent(BorderLayout.WEST));
					
					add(new JScrollPane(pDecks), BorderLayout.WEST);
					validate();
				}
			}
		});
		JButton removeDeck = new JButton("Remove");
		removeDeck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Component source = (Component)event.getSource();
				Deck deck;
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)decksTree.getLastSelectedPathComponent();
				
				// Nothing is selected
				if (node == null) {
					return;
				}
				
				Object nodeInfo = node.getUserObject();
				if (nodeInfo instanceof Deck) {
					deck = (Deck)nodeInfo;
				} else if (node.isLeaf()) {
					if (nodeInfo instanceof Flashcard) {
						node = (DefaultMutableTreeNode)node.getParent();
						nodeInfo = node.getUserObject();
						if (nodeInfo instanceof Deck) {
							deck = (Deck)nodeInfo;
						} else {
							return;
						}
					} else {
						return;
					}
				} else {
					return;
				}
				
				int dialogResult = JOptionPane.showConfirmDialog (source, "Remove deck " + deck.getName() + "?", "Remove deck", JOptionPane.YES_NO_OPTION);
				if (dialogResult == JOptionPane.YES_OPTION){
					DeckOperations.removeDeck(decks, deck);
					JPanel pDecks = setupDecksTree();
					
					BorderLayout layout = (BorderLayout)getLayout();
					remove(layout.getLayoutComponent(BorderLayout.WEST));
					
					add(new JScrollPane(pDecks), BorderLayout.WEST);
					validate();
				}
			}
		});
		
		JButton addCard = new JButton("Add");
		addCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Component source = (Component)event.getSource();
				Deck deck;
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)decksTree.getLastSelectedPathComponent();
				
				// Nothing is selected
				if (node == null) {
					return;
				}
				
				Object nodeInfo = node.getUserObject();
				if (nodeInfo instanceof Deck) {
					deck = (Deck)nodeInfo;
				} else if (node.isLeaf()) {
					if (nodeInfo instanceof Flashcard) {
						node = (DefaultMutableTreeNode)node.getParent();
						nodeInfo = node.getUserObject();
						if (nodeInfo instanceof Deck) {
							deck = (Deck)nodeInfo;
						} else {
							return;
						}
					} else {
						return;
					}
				} else {
					return;
				}
				
				int dialogResult = JOptionPane.showConfirmDialog (source, "Add a card to " + deck.getName() + "?", "Add card", JOptionPane.YES_NO_OPTION);
				if (dialogResult == JOptionPane.YES_OPTION){
					DeckOperations.addNewCardToDeck(deck);
					JPanel pDecks = setupDecksTree();
					
					BorderLayout layout = (BorderLayout)getLayout();
					remove(layout.getLayoutComponent(BorderLayout.WEST));
					
					add(new JScrollPane(pDecks), BorderLayout.WEST);
					validate();
				}
			}
		});
		JButton removeCard = new JButton("Remove");
		removeCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Component source = (Component)event.getSource();
				Flashcard card;
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)decksTree.getLastSelectedPathComponent();
				
				// Nothing is selected
				if (node == null) {
					return;
				}
				
				Object nodeInfo = node.getUserObject();
				if (nodeInfo instanceof Flashcard) {
					card = (Flashcard)nodeInfo;
				} else {
					return;
				}
				
				Deck deck = (Deck)((DefaultMutableTreeNode)node.getParent()).getUserObject();
				
				int dialogResult = JOptionPane.showConfirmDialog (source, "Remove Card " + (node.getParent().getIndex(node) + 1)
						+ " from " + deck.getName() + "?", "Remove card", JOptionPane.YES_NO_OPTION);
				if(dialogResult == JOptionPane.YES_OPTION){
					DeckOperations.removeCardFromDeck(deck, card);
					JPanel pDecks = setupDecksTree();
					
					BorderLayout layout = (BorderLayout)getLayout();
					remove(layout.getLayoutComponent(BorderLayout.WEST));
					
					add(new JScrollPane(pDecks), BorderLayout.WEST);
					validate();
					
					createSidesList(null);
				}
			}
		});
		
		JPanel westButtons = new JPanel();
		westButtons.setLayout(new BoxLayout(westButtons, BoxLayout.Y_AXIS));
		
		JPanel deckButtons = new JPanel();
		deckButtons.setLayout(new BoxLayout(deckButtons, BoxLayout.X_AXIS));
		deckButtons.add(addDeck);
		deckButtons.add(removeDeck);
		
		westButtons.add(new JLabel("Deck"));
		westButtons.add(deckButtons);
		
		JPanel cardButtons = new JPanel();
		cardButtons.setLayout(new BoxLayout(cardButtons, BoxLayout.X_AXIS));
		cardButtons.add(addCard);
		cardButtons.add(removeCard);
		
		westButtons.add(new JLabel("Card"));
		westButtons.add(cardButtons);
		
		pDecks.setLayout(new BorderLayout());
		pDecks.add(decksTree, BorderLayout.CENTER);
		pDecks.add(westButtons, BorderLayout.SOUTH);
		
		return pDecks;
	}
	
	/**
	 * Creates a tree-list of Decks and their Flashcards
	 * 
	 * @param top The root node of the tree
	 */
	private void createDecksNodes(DefaultMutableTreeNode top) {
		SQLiteHandler sqlite;
		try {
			sqlite = new SQLiteHandler(Config.DATABASE);
		} catch (SQLiteException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			// Get all the decks' names and ids
			ArrayList<ArrayList<String>> decks = sqlite.select(Config.DECK_TABLE, "ID,Name", "", "");
			int numDecks = decks.size();
			for (int i = 0; i < numDecks; i++) {
				ArrayList<String> deckRow = decks.get(i);
				String deckID = deckRow.get(0);
				String deckName = deckRow.get(1);
				Deck deck = new Deck(Integer.parseInt(deckID), deckName);
				
				DefaultMutableTreeNode deckNode = new DefaultMutableTreeNode(deck); 
				
				// Get all the cards in this deck
				ArrayList<ArrayList<String>> cards = sqlite.select(Config.CARD_TABLE, "ID,Weight", "DeckID = ?", deckID);
				int numCards = cards.size();
				for (int j = 0; j < numCards; j++) {
					ArrayList<String> cardRow = cards.get(j);
					String cardID = cardRow.get(0);
					int cardWeight = Integer.parseInt(cardRow.get(1));
					Flashcard card = new Flashcard(Integer.parseInt(cardID), null, cardWeight);
					card.toString("Card " + (j + 1));
					
					deck.addCard(card);
					deckNode.add(new DefaultMutableTreeNode(card));
				}
				
				top.add(deckNode);
			}
			
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			sqlite.close();
		}
	}
	
	/**
	 * Builds a scrollable list of previews of the currently selected card's sides
	 * 
	 * @param sides The sides of the currently selected card
	 */
	private void createSidesList(Vector<FlashcardSide> sides) {
		sidesList.removeAll();
		cardView.setSide(null);
		
		if (null == sides) {
			revalidate();
			return;
		}
		
		JLabel listLabel = new JLabel("Sides");
		listLabel.setHorizontalAlignment(SwingConstants.CENTER);
		sidesList.add(listLabel);
		
		int numSides = sides.size();
		for (int i = 0; i < numSides; i++) {
			SideView sideView = new SideView(this, sides.get(i));
			sideView.listenForMouseClicks(true);
			sidesList.add(sideView);
		}
		
		revalidate();
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent event) {
		JTree source = (JTree)event.getSource();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)source.getLastSelectedPathComponent();
		
		// Nothing is selected
		if (node == null) {
			return;
		}
		
		Object nodeInfo = node.getUserObject();
		if (node.isLeaf()) {
			if (nodeInfo instanceof Flashcard) {
				Flashcard card = (Flashcard)nodeInfo;
				if (null == card.getSides()) {
					SQLiteHandler sqlite;
					try {
						sqlite = new SQLiteHandler(Config.DATABASE);
					} catch (SQLiteException e) {
						e.printStackTrace();
						return;
					}
					
					try {
						ArrayList<ArrayList<String>> rows = sqlite.select(Config.SIDE_TABLE, "ID,Label,Text,Weight", "CardID = ?", String.valueOf(card.getID()));
						int numSides = rows.size();
						Vector<FlashcardSide> sides = new Vector<FlashcardSide>();
						for (int i = 0; i < numSides; i++) {
							ArrayList<String> row = rows.get(i);
							sides.add(new FlashcardSide(Integer.parseInt(row.get(0)), row.get(1), row.get(2), Integer.parseInt(row.get(3))));
						}
						card.setSides(sides);
					} catch (SQLiteException e) {
						e.printStackTrace();
					} finally {
						sqlite.close();
					}
				}
				
				createSidesList(card.getSides());
			}
		}
	}
}