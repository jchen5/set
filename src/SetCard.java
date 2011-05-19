import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class SetCard extends JButton{
	public static final int RED = 0, PURPLE = 1, GREEN = 2,
		SQUIGGLE = 0, DIAMOND = 1, OVAL = 2,
		SOLID = 0, SHADED = 1, EMPTY = 2;
	public static final int BORDER_WIDTH = 4;
	public static final int PIC_WIDTH = 95, PIC_HEIGHT = 62;
	
	public SetCard(int color, int shape, int shade, int number, ActionListener l){
		int cardNumber = number + 3 * color + 9 * shape + 27 * shade + 1;
		String file = "cards/" + String.format("%02d", cardNumber) + ".gif";
		setIcon(new ImageIcon(file));
		
		features = new int[4];
		features[0] = color;
		features[1] = shape;
		features[2] = shade;
		features[3] = number;
		addActionListener(l);
		chosen = false;
		//setBorder(new BasicBorders.ButtonBorder(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
		setBorder(BorderFactory.createLineBorder(Color.WHITE, BORDER_WIDTH));
	}
	
	public boolean getChosen(){
		return chosen;
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
}
