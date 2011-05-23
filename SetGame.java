import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;

public class SetGame extends JApplet implements ActionListener{
	
	public static final int CARD_MARGIN = 7;
	public static final int EAST_WIDTH = 200, NORTH_HEIGHT = 75, SOUTH_HEIGHT = 50;
	public static final String font = "Comic Sans";
	public static final String version = "2.1";
	
	public static void main(String[] args){
		SetGame s = new SetGame();
        JFrame window = new JFrame("Productivity Destroyer " + version);
        s.init();
        s.start();
        window.setContentPane(s);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();              // Arrange the components.
        window.setVisible(true);    // Make the window visible
	}
	
	public void init(){
		setPreferredSize(new Dimension(3 * (SetCard.PIC_WIDTH + 2 * SetCard.BORDER_WIDTH) + 4 * CARD_MARGIN + EAST_WIDTH,
				7 * (SetCard.PIC_HEIGHT + 2 * SetCard.BORDER_WIDTH) + 8 * CARD_MARGIN + NORTH_HEIGHT + SOUTH_HEIGHT));
		setLayout(new BorderLayout());
	}
	
	private void layStartingCards(){
		for(int i = 0; i < 12; ++i){
			SetCard sc = deck.pop();
			gp.add(sc);
			savedCards.add(sc);
		}
		gp.updateSize();
	}
	
	public void start(){		
		for(int col = 0; col < 3; ++col){
			//deck.push(new SetCard(col, 0, 0, 0, this));
			for(int shp = 0; shp < 3; ++shp){
				for(int shd = 0; shd < 3; ++shd){
					for(int num = 0; num < 3; ++num){
						deck.add(new SetCard(col, shp, shd, num, this));
					}
				}
			}
		}
		/*for(int i = 0; i < 14; ++i){
			deck.add(new SetCard(0,0,0,0, this));
		}
		deck.add(new SetCard(0,0,0,1, this));*/
		Collections.shuffle(deck);
		
		gp = new SetGamePanel();
		layStartingCards();
		JPanel outerPanel = new JPanel();
		outerPanel.setLayout(new FlowLayout());
		outerPanel.add(gp);
		add(outerPanel);
		
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
		add(eastPanel, BorderLayout.EAST);
		
		addCards = new JButton("Can I Haz Cards?");
		addCards.addActionListener(this);
		addCards.setPreferredSize(new Dimension(EAST_WIDTH, addCards.getPreferredSize().height));
		addCards.setAlignmentX(Component.CENTER_ALIGNMENT);
		eastPanel.add(addCards);

		cardsLeft = new JLabel("Cards Left: " + deck.size(), SwingConstants.CENTER);
		cardsLeft.setFont(new Font(font, Font.PLAIN, 16));
		cardsLeft.setAlignmentX(Component.CENTER_ALIGNMENT);
		cardsLeft.setPreferredSize(new Dimension(EAST_WIDTH, cardsLeft.getPreferredSize().height));
		eastPanel.add(cardsLeft);
		
		timeLabel = new JLabel("", SwingConstants.CENTER);
		timeLabel.setFont(new Font(font, Font.PLAIN, 16));
		timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		timeLabel.setPreferredSize(new Dimension(EAST_WIDTH, timeLabel.getPreferredSize().height));
		eastPanel.add(timeLabel);
		
		JLabel title = new JLabel("Set v. " + version, SwingConstants.CENTER);
		title.setFont(new Font(font, Font.BOLD, 36));
		title.setPreferredSize(new Dimension(getWidth(), NORTH_HEIGHT));
		add(title, BorderLayout.NORTH);
		
		message = new JLabel("", SwingConstants.CENTER);
		message.setFont(new Font(font, Font.PLAIN, 20));
		message.setPreferredSize(new Dimension(getWidth(), SOUTH_HEIGHT));
		add(message, BorderLayout.SOUTH);
		
		tl = new TimerListener(timeLabel);
		timer = new Timer(1000, tl);
		timer.setInitialDelay(0);
		timer.start();
	}
	
	private void restartGame(){
		message.setText("Congratulations!  You finished in " + tl.getTime() + " seconds!");
		
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
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == addCards){
			if(deck.empty()){
				//Assume text was reset to "No more Sets"
				if(gp.hasSet()){
					message.setText("You fail. There's still a set here.");
				}
				else{
					restartGame();
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
				savedCards.push(sc);
			}
			message.setText("Three Cards Added.");
			gp.updateSize();
			validate();
		}
		else{
			SetCard card = (SetCard)e.getSource();
			selected.pick(card);
			if(selected.size() == 3){
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
								savedCards.push(sc);
							}
						}
					}
					if(sizeChanged){
						gp.updateSize();
					}
					if(gp.getComponentCount() > 0) message.setText("Set!");
					else restartGame();
				}
				else{
					message.setText("You fail.  That's not a set.");
				}
				selected.deselect();
				selected.clear();
				validate();
				repaint();
			}
		}
		cardsLeft.setText("Cards Left: " + deck.size());
		if(deck.size() == 0){
			addCards.setText("No more Sets");
		}
	}
	
	private class TimerListener implements ActionListener{
		public TimerListener(JLabel lab){
			this.lab = lab;
		}
		
		public void reset(){
			time = -1;
		}
		
		public int getTime(){
			return time;
		}
		
		public void actionPerformed(ActionEvent e){
			++time;
			lab.setText("Time Elapsed: " + time/60 + ":" + String.format("%02d", time % 60));
		}
		
		JLabel lab;
		int time = -1;
	}
	
	Stack<SetCard> deck = new Stack<SetCard>();
	Stack<SetCard> savedCards = new Stack<SetCard>();
	SetCardList selected = new SetCardList();
	SetGamePanel gp;
	JButton addCards;
	JLabel cardsLeft, message, timeLabel;
	Timer timer;
	TimerListener tl;
}
