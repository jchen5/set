import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class SetCard extends JButton{
	public static final int RED = 0, PURPLE = 1, GREEN = 2,
		SQUIGGLE = 0, DIAMOND = 1, OVAL = 2,
		SOLID = 0, SHADED = 1, EMPTY = 2;
	public static final int BORDER_WIDTH = 4;
	public static final int PIC_WIDTH = 95, PIC_HEIGHT = 62;
	public static final int LABEL_X_POS = SetCard.PIC_WIDTH - 6, LABEL_Y_POS = SetCard.PIC_HEIGHT + 1;
	public static final int BOX_X_MARGIN = 18, BOX_Y_MARGIN = 18;
	public static final int LETTER_SIZE = 12;
	
	public SetCard(int card, ActionListener l){
		String file = "/cards/" + String.format("%02d", card) + ".gif";
		try {
			//System.out.println(SetCard.class.getResourceAsStream(file));
			//System.out.println(SetCard.class.getResourceAsStream("SetCard.class"));
			setIcon(new ImageIcon(ImageIO.read(SetCard.class.getResourceAsStream(file))));
		} catch (IOException e) {
			setText("Oops");
		}
		
		card--; //look what you've done, 1-indexing
		features = new int[4];
		features[0] = card % 3;
		features[1] = (card / 3) % 3;
		features[2] = (card / 9) % 3;
		features[3] = (card / 27) % 3;
		addActionListener(l);
		chosen = false;
		//setBorder(new BasicBorders.ButtonBorder(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
		setBorder(BorderFactory.createLineBorder(Color.WHITE, BORDER_WIDTH));
	}
	
	public boolean getChosen(){
		return chosen;
	}
	
	public void setLetter(char l){
		letter = l;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(getWidth() - BOX_X_MARGIN, getHeight() - BOX_Y_MARGIN, BOX_X_MARGIN, BOX_Y_MARGIN);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Trebuchet MS", Font.BOLD, LETTER_SIZE));
		char[] str = {letter};
		g.drawString(new String(str), LABEL_X_POS, LABEL_Y_POS);
	}
	
	public void setChosen(boolean cho){
		chosen = cho;
		if(chosen){
			setBorder(new LineBorder(Color.BLACK, BORDER_WIDTH, false));
		}
		else{
			setBorder(BorderFactory.createLineBorder(Color.WHITE, BORDER_WIDTH));
		}
	}
	
	public int getFeature(int which){
		return features[which];
	}
	
	int[] features;
	boolean chosen = false;
	char letter;
}
