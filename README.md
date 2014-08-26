CS451_FlashcardApp
==================

Final project for CS451

Jon Hopkins  
Jesse Kuehn  
Rishir Patel  
Sanjana Raj

Flashcard decks are composed of flashcards, which are composed of a weight and any number of sides. Each side has a label, text, and a weight.

The weight of the card is used to draw incorrect cards more often. The weight on the sides is so a side that is gotten incorrect more often can be displayed more.

The label on each side is for display purposes, and is important for cards with more than two sides. Any of the sides can be displayed and ask the user to guess any of the other sides. Using the labels, the user knows which side is being displayed and which side they should be guessing.

An example deck for a chemistry class would have a card like:
- Side 1
  - Label: Chemical Formula
  - Text: H20
  - Weight: 50
- Side 2
  - Label: Chemical Name
  - Text: water
  - Weight: 50

This could be displayed like:  
Given the **Chemical Formula**:  
H20  
what is the **Chemical Name**?

The SQLite database will be as follows:
- Table `Deck`
  - `ID`, `Name`
- Table `Cards`
  - `ID`, `DeckID`, `Weight`
- Table `Sides`
  - `ID`, `CardID`, `Label`, `Text`, `Weight`

Support for images and audio may be added later
