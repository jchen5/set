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

public class ProsetPanel extends SetPanel {
	public static final int TOTAL_SETS = 6;
	public static final int TOTAL_CARDS = 12;


	public ProsetPanel(SetGame parent){
		super(parent);
		removeKeyListener(cl);
		cl = new ProsetCardListener(this);
		addKeyListener(cl);
	}

	private void layStartingCards(){
		for(int i = 0; i < 7; ++i){
			SetCard sc = deck.pop();
			gp.add(sc);
			//savedCards.add(sc);
		}
		gp.updateSize();
	}

	private void loadStandardDeck(){
		for(int i = 1; i <= 63; ++i){
			deck.add(new ProjectiveSetCard(i, cl));
			deck.get(i-1).setNumber(i);
		}
	}

	private void loadTestingDeck(){
		for(int i = 0; i < 14; ++i){
			deck.add(new ProjectiveSetCard(0, cl));
		}
		deck.add(new ProjectiveSetCard(1, cl));
	}

	public void specStart(){
		loadStandardDeck();
		//loadTestingDeck();

		Collections.shuffle(deck);

		originalDeck = new ArrayList<ProjectiveSetCard>();
		for(int i = deck.size() - 1; i >= 0; --i)
			originalDeck.add(deck.get(i));

		layStartingCards();

		addCards = new JButton("Validate Proset! (" + (char)(CardListener.MORE_CARDS - 'a' + 'A') + ")");
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
		s.append("Proset Game");

		s.append("; time: " + tl.getTime());

		s.append("; failed prosets: " + numFailedAddCards);

		s.append("; cards: ");
		for(int i = 0; i < originalDeck.size(); ++i) {
			if(i > 0)
				s.append(", ");
			s.append((originalDeck.get(i).getMod() + 1));
		}

		parent.logGame(s.toString());

		ArrayList<Component> comps = new ArrayList<Component>();
		comps.add(new CongratsLabel("You incorrectly claimed a proset"));
		comps.add(new CongratsLabel(numFailedAddCards + " " + (numFailedAddCards == 1 ? "time" : "times") + "."));

		parent.endGame(tl, comps);
	}

	private void updateNumCards(){
		cardsLeft.setText("Cards Left: " + deck.size());
	}

	public void pickedThree(SetCardList selected){
		// no-op
		return;
	}

	public void otherActionPerformed(ActionEvent e) {
		if(e.getSource() == addCards && !isPaused){
			if(selected.isProset())	{
				for(int i = 0; i < gp.getComponentCount(); i++){
					if(selected.contains(gp.getComponent(i)))	{
						gp.remove(i--);
					}
				}
				selected.clear();
				while(!deck.isEmpty() && gp.getComponentCount() < 7){
					SetCard sc = deck.pop();
					gp.add(sc);
				}
				if(gp.getComponentCount() == 0)	{
					endGame();
				}
				message.setText("Proset Found!");
				updateNumCards();
				gp.updateSize();
				validate();
			}
			else	{
				numFailedAddCards++;
				message.setText("You fail. That's not a proset.");
			}
		}
	}

	Stack<ProjectiveSetCard> deck = new Stack<ProjectiveSetCard>();
	ArrayList<ProjectiveSetCard> originalDeck;
	//Stack<SetCard> savedCards = new Stack<SetCard>();
	SetCardList selected = new SetCardList();
	ProjectiveSetCard lastCard;
	JButton addCards;
	JLabel cardsLeft;
	int numFailedAddCards = 0;
}
