import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;

public class SetClassicPanel extends SetPanel{
	
	/*
	 * Things to be added
	 *  - Last Card
	 *  - Keyboard Selection
	 *  - Set Daily Puzzle
	 *  - Automatic Score Reporting
	 */
	
	public SetClassicPanel(SetGame parent){
		super(parent);
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
		lastCard = deck.get(0);
		lastCard.setFlipped(true);
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
		if(Arrays.asList(gp.getComponents()).contains(lastCard)){
			ArrayList<Component> comps = new ArrayList<Component>();
			comps.add(new CongratsLabel("The Turned-Over Card Was:"));
			comps.add(Box.createRigidArea(new Dimension(0, 10)));
			JLabel lastCardLabel = new JLabel(lastCard.icon);
			lastCardLabel.setAlignmentX(CENTER_ALIGNMENT);
			comps.add(lastCardLabel);
			parent.endGame(tl, comps);
		}
		else{
			parent.endGame(tl, null);
		}
	}
	
	private void updateNumCards(){
		cardsLeft.setText("Cards Left: " + deck.size());
		if(deck.size() == 0){
			addCards.setText("No more Sets (" + (char)(CardListener.MORE_CARDS - 'a' + 'A') + ")");
		}
	}
	
	public void pickedThree(SetCardList selected){
		if(selected.isSet()){
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
			if(gp.getComponentCount() > 0) message.setText("Set!");
			//else restartGame();
			else endGame();
		}
		else{
			message.setText("You fail.  That's not a set.");
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
				if(gp.hasSet()){
					message.setText("You fail. There's still a set here.");
				}
				else{
					endGame();
				}
				return;
			}
			if(gp.hasSet()){
				message.setText("You fail.  There's still a set here.");
				return;
			}
			for(int i = 0; i < 3; ++i){
				SetCard sc = deck.pop();
				gp.add(sc);
				//savedCards.push(sc);
			}
			message.setText("Three Cards Added.");
			updateNumCards();
			gp.updateSize();
			validate();
		}
	}
	
	Stack<SetCard> deck = new Stack<SetCard>();
	//Stack<SetCard> savedCards = new Stack<SetCard>();
	SetCard lastCard;
	SetCardList selected = new SetCardList();
	JButton addCards;
	JLabel cardsLeft;
}
