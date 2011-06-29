import java.awt.Font;
import javax.swing.JLabel;

public class CongratsLabel extends JLabel{
	public static final int DEFAULT_SIZE = 24;
	
	public CongratsLabel(String text){
		super(text);
		setFont(new Font(SetGame.FONT, Font.PLAIN, DEFAULT_SIZE));
		setAlignmentX(CENTER_ALIGNMENT);
	}
}