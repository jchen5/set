import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class MessageLabel extends JLabel{
	public static final int FONT_SIZE = 20;
	
	public MessageLabel(int width){
		super("", SwingConstants.CENTER);
		setFont(new Font(SetGame.FONT, Font.PLAIN, 20));
		setPreferredSize(new Dimension(width, SetGame.SOUTH_HEIGHT));
	}
}
