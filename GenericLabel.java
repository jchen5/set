import java.awt.Font;
import javax.swing.JLabel;
import java.awt.Dimension;
public class GenericLabel extends JLabel{
	public static final int DEFAULT_SIZE = 20;
	
	public GenericLabel(String text){
		super(text);
		setFont(new Font(SetGame.FONT, Font.PLAIN, DEFAULT_SIZE));
		setAlignmentX(CENTER_ALIGNMENT);
	}
}
