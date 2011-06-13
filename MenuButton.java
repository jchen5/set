import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
	
public class MenuButton extends JButton{
	public static final int DEFAULT_SIZE = 28;
	public static final int DEFAULT_WIDTH = 300;
	public static final int MARGIN = 5;
	
	public MenuButton(String text){
		super(text);
		setFont(new Font(SetGame.FONT, Font.PLAIN, DEFAULT_SIZE));
		setPreferredSize(new Dimension(DEFAULT_WIDTH, getPreferredSize().height));
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setAlignmentY(Component.CENTER_ALIGNMENT);
	}
}