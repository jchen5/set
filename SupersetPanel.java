import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.TimeZone;
import java.util.regex.*;
import javax.swing.*;

import java.text.*;

public class SupersetPanel extends SetPanel {
	public static final int TOTAL_SETS = 6;
	public static final int TOTAL_CARDS = 12;

	/*
	 * Things to be added
	 *  - Last Card
	 *  - Keyboard Selection
	 *  - Set Daily Puzzle
	 *  - Automatic Score Reporting
	 */

	public SupersetPanel(SetGame parent){
		super(parent);
		removeKeyListener(cl);
		cl = new SupersetCardListener(this);
		addKeyListener(cl);
	}

	private void layStartingCards(){
		for(int i = 0; i < 12; ++i){
			SetCard sc = deck.pop();
			gp.add(sc);
			//savedCards.add(sc);
		}
		gp.updateSize();
	}

	private void loadStandardDeck(){
		for(int i = 0; i < 81; ++i){
			deck.add(new SetCard(i, cl));
		}
	}

	private void loadTestingDeck(){
		for(int i = 0; i < 14; ++i){
			deck.add(new SetCard(0, cl));
		}
		deck.add(new SetCard(1, cl));
	}

	public void specStart(){
		loadStandardDeck();
		//loadTestingDeck();

		Collections.shuffle(deck);

		originalDeck = new ArrayList<SetCard>();
		for(int i = deck.size() - 1; i >= 0; --i)
			originalDeck.add(deck.get(i));

		layStartingCards();

		addCards = new JButton("Can I Haz Cards? (" + (char)(CardListener.MORE_CARDS - 'a' + 'A') + ")");
		addCards.addActionListener(this);
		addCards.setPreferredSize(new Dimension(SetGame.EAST_WIDTH, addCards.getPreferredSize().height));
		addCards.setAlignmentX(Component.CENTER_ALIGNMENT);
		eastPanel.add(addCards);

		eastPanel.add(pause);

		cardsLeft = new EastLabel("Cards Left: " + deck.size());
		eastPanel.add(cardsLeft);

		eastPanel.add(timeLabel);

		timer.start();
	}

	/*private void restartGame(){
		message.setText(tl.congrats());

		while(!deck.empty()){
			savedCards.push(deck.pop());
		}
		gp.removeAll();
		deck = savedCards;
		savedCards = new Stack<SetCard>();
		Collections.shuffle(deck);
		layStartingCards();

		addCards.setText("Can I Haz Cards?");

		tl.reset();
		timer.restart();
	}*/



	private void endGame(){		
		timer.stop();

		StringBuilder s = new StringBuilder();
		s.append("Superset Game");

		s.append("; time: " + tl.getTime());

		s.append("; failed add cards: " + numFailedAddCards);

		s.append("; cards: ");
		for(int i = 0; i < originalDeck.size(); ++i) {
			if(i > 0)
				s.append(", ");
			s.append((originalDeck.get(i).getMod() + 1));
		}

		parent.logGame(s.toString());

		ArrayList<Component> comps = new ArrayList<Component>();
		comps.add(new CongratsLabel("You incorrectly claimed there"));
		comps.add(new CongratsLabel("were no supersets " + numFailedAddCards + " " + (numFailedAddCards == 1 ? "time" : "times") + "."));

		parent.endGame(tl, comps);
	}

	private void updateNumCards(){
		cardsLeft.setText("Cards Left: " + deck.size());
		if(deck.size() == 0){
			addCards.setText("No more Sets (" + (char)(CardListener.MORE_CARDS - 'a' + 'A') + ")");
		}
	}

	public void pickedFour(SetCardList selected){
		if(selected.isSuperSet()){
			boolean sizeChanged = false;
			for(int i = 0; i < gp.getComponentCount(); ++i){
				if(selected.contains(gp.getComponent(i))){
					gp.remove(i);
					if(deck.empty() || gp.getComponentCount() >= 12){
						--i;
						sizeChanged = true;
					}
					else{
						SetCard sc = deck.pop();
						gp.add(sc, i);
						//savedCards.push(sc);
					}
				}
			}
			if(sizeChanged){
				gp.updateSize();
			}
			if(gp.getComponentCount() > 0) message.setText("Superset!");
			//else restartGame();
			else endGame();
		}
		else{
			message.setText("You fail. That's not a super set.");
		}
		updateNumCards();
		gp.updateSize();
		validate();
		repaint();
	}

	public void otherActionPerformed(ActionEvent e) {
		if(e.getSource() == addCards && !isPaused){
			if(deck.empty()){
				//Assume text was reset to "No more Sets"
				if(gp.hasSuperSet()){
					numFailedAddCards++;
					message.setText("You fail. There's still a superset here.");
				}
				else{
					endGame();
				}
				return;
			}
			if(gp.hasSuperSet()){
				numFailedAddCards++;
				message.setText("You fail.  There's still a superset here.");
				return;
			}
			boolean all = true;
			for(int i = 0; i < 4; ++i){
				if(deck.isEmpty())	{
					all = false;
				}
				SetCard sc = deck.pop();
				gp.add(sc);
				//savedCards.push(sc);
			}
			if(all)
				message.setText("Four Cards Added.");
			else
				message.setText("Last Card Added.");
			updateNumCards();
			gp.updateSize();
			validate();
		}
	}

	// dummy
	public void pickedThree(SetCardList selected){
		return;
	}

	Stack<SetCard> deck = new Stack<SetCard>();
	ArrayList<SetCard> originalDeck;
	//Stack<SetCard> savedCards = new Stack<SetCard>();
	SetCardList selected = new SetCardList();
	JButton addCards;
	JLabel cardsLeft;
	int numFailedAddCards = 0;
}
