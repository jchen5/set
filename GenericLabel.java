import java.awt.Font;
import javax.swing.JLabel;
import java.awt.Dimension;
public class GenericLabel extends JLabel{
	public GenericLabel(String text){
		this(text, SetGame.DEFAULT_FONT_SIZE);
	}

	public GenericLabel(String text, int fontSize){
		super(text);
		setFont(new Font(SetGame.FONT, Font.PLAIN, fontSize));
		setAlignmentX(CENTER_ALIGNMENT);
	}
}
