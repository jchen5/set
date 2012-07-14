import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class ProjectiveSetCard extends SetCard implements Comparable{
	
	public ProjectiveSetCard(int card, ActionListener l){
		super("/projectiveCards/", card-1, l);
	}
	
	public int compareTo(Object e) {
		return (modNumber) - (((ProjectiveSetCard)e).modNumber);
	}
	
	public void setNumber(int in)	{
		this.modNumber = in;
	}
	
}
