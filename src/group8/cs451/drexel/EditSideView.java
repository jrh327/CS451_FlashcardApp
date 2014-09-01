/**
 * EditSideView.java: displays a form for editing cards
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import com.almworks.sqlite4java.SQLiteException;

public class EditSideView extends JPanel {
	private FlashcardSide side;
	
	public EditSideView(final SideView sideView) {
		side = sideView.getSide();
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new EtchedBorder()));
		
		JPanel info = new JPanel();
		info.setBackground(Color.WHITE);
		info.setLayout(new BorderLayout());
		
		final JTextField label = new JTextField(side.getLabel());
		final JTextField text = new JTextField(side.getText());
		final FlashcardSide s = this.side;
		
		JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				boolean refresh = false;
				if (!s.getLabel().equals(label.getText())) {
					// change the side's label if the value in the textbox has changed
					s.setLabel(label.getText());
					refresh = true;
				}
				if (!s.getText().equals(text.getText())) {
					// change the side's text if the value in the textbox has changed
					s.setText(text.getText());
					refresh = true;
				}
				if (refresh) {
					// force the view to update
					sideView.refresh();
					
					SQLiteHandler sqlite;
					try {
						sqlite = new SQLiteHandler(Config.DATABASE);
					} catch (SQLiteException e) {
						e.printStackTrace();
						return;
					}
					
					try {
						sqlite.update(Config.SIDE_TABLE, "Label,Text", s.getLabel() + "," + s.getText(), "ID = ?", String.valueOf(s.getID()));
						s.markClean();
					} catch (SQLiteException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		info.add(label, BorderLayout.NORTH);
		info.add(save, BorderLayout.SOUTH);
		info.add(text, BorderLayout.CENTER);
		
		add(info, BorderLayout.CENTER);
	}
	
	public FlashcardSide getSide() {
		return side;
	}
}