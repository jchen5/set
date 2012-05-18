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
		s.append("Classic Game");
					
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
		comps.add(new CongratsLabel("Number of incorrect attempts to add cards: " + numFailedAddCards));
		
		if(Arrays.asList(gp.getComponents()).contains(lastCard)){
			comps.add(Box.createRigidArea(SetGame.RIGID_DIM));
			comps.add(new CongratsLabel("The turned-over card was:"));
			comps.add(Box.createRigidArea(new Dimension(0, 10)));
			JLabel lastCardLabel = new JLabel(lastCard.icon);
			lastCardLabel.setAlignmentX(CENTER_ALIGNMENT);
			comps.add(lastCardLabel);
		}
		parent.endGame(tl, comps);
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
					numFailedAddCards++;
					message.setText("You fail. There's still a set here.");
				}
				else{
					endGame();
				}
				return;
			}
			if(gp.hasSet()){
				numFailedAddCards++;
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
	ArrayList<SetCard> originalDeck;
	//Stack<SetCard> savedCards = new Stack<SetCard>();
	SetCard lastCard;
	SetCardList selected = new SetCardList();
	JButton addCards;
	JLabel cardsLeft;
	int numFailedAddCards = 0;
}
