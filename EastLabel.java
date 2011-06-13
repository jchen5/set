import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class EastLabel extends JLabel {
	public static final int FONT_SIZE = 16;
	
	public EastLabel(String text){
		super(text, SwingConstants.CENTER);
		setFont(new Font(SetGame.FONT, Font.PLAIN, FONT_SIZE));
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setPreferredSize(new Dimension(SetGame.EAST_WIDTH, getPreferredSize().height));
	}
}
