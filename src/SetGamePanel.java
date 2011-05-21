import java.awt.*;
import javax.swing.JPanel;

public class SetGamePanel extends JPanel {
	
	public void updateSize(){
		int numRows = getComponentCount()/3;
		setPreferredSize(new Dimension(3 * (SetCard.PIC_WIDTH + 2 * SetCard.BORDER_WIDTH) + 2 * SetGame.CARD_MARGIN,
				numRows * (SetCard.PIC_HEIGHT + 2 * SetCard.BORDER_WIDTH + SetGame.CARD_MARGIN) - SetGame.CARD_MARGIN));
		setLayout(new GridLayout(numRows, 3, SetGame.CARD_MARGIN, SetGame.CARD_MARGIN));
	}
	
	public boolean hasSet(){
		int size = getComponentCount();
		for(int i = 0; i < size; ++i){
			for(int j = 0; j < i; ++j){
				for(int k = 0; k < j; ++k){
					if(SetCardList.isSet((SetCard)getComponent(i), (SetCard)getComponent(j), (SetCard)getComponent(k))) return true;
				}
			}
		}
		return false;
	}
}
