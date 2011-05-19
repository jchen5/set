import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SetGame extends JApplet implements ActionListener{
	
	public static final int CARD_MARGIN = 7;
	public static final int EAST_WIDTH = 150;
	
	public static void main(String[] args) throws Exception{
		SetGame s = new SetGame();
        JFrame window = new JFrame("Productivity Destroyer 1.0");
        s.init();
        s.start();
        window.setContentPane(s);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();              // Arrange the components.
        window.setVisible(true);    // Make the window visible
	}
	
	public void init(){
		setPreferredSize(new Dimension(3 * (SetCard.PIC_WIDTH + 2 * SetCard.BORDER_WIDTH) + 4 * CARD_MARGIN + EAST_WIDTH,
				7 * (SetCard.PIC_HEIGHT + 2 * SetCard.BORDER_WIDTH) + 8 * CARD_MARGIN));
	}
	
	public void start(){
		setLayout(new BorderLayout());
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
		/*for(int i = 0; i < 15; ++i){
			deck.add(new SetCard(0,0,0,0, this));
		}*/
		Collections.shuffle(deck);
		
		gp = new SetGamePanel();
		for(int i = 0; i < 12; ++i){
			gp.add(deck.pop());
		}
		gp.updateSize();
		JPanel superPanel = new JPanel();
		superPanel.setLayout(new FlowLayout());
		superPanel.add(gp);
		add(superPanel);
		
		addCards = new JButton("Can I Haz Cards?");
		addCards.addActionListener(this);
		addCards.setPreferredSize(new Dimension(EAST_WIDTH, addCards.getPreferredSize().height));
		eastPanel = new JPanel();
		eastPanel.setLayout(new FlowLayout());
		eastPanel.add(addCards);
		add(eastPanel, BorderLayout.EAST);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == addCards){
			if(deck.empty()) return;
			if(gp.hasSet()) return;
			for(int i = 0; i < 3; ++i){
				gp.add(deck.pop());
			}
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
							else gp.add(deck.pop(), i);
						}
					}
					if(sizeChanged){
						gp.updateSize();
					}
				}
				selected.deselect();
				selected.clear();
				validate();
				repaint();
			}
		}
	}
	Stack<SetCard> deck = new Stack<SetCard>();
	SetCardList selected = new SetCardList();
	SetGamePanel gp;
	JButton addCards;
	JPanel eastPanel;
}
