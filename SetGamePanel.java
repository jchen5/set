import java.awt.*;
import javax.swing.JPanel;

public class SetGamePanel extends JPanel {

	public void drawLetters(){
		Component[] comps = getComponents();
		for(int i = 0; i < comps.length; ++i){
			((SetCard) comps[i]).setLetter((char)('A' + i));
		}
		repaint();
	}

	public void updateSize(){
		drawLetters();
		int numRows = (getComponentCount()+2)/3;
		setPreferredSize(new Dimension(3 * (SetCard.PIC_WIDTH + 2 * SetCard.BORDER_WIDTH) + 2 * SetGame.CARD_MARGIN,
				numRows * (SetCard.PIC_HEIGHT + 2 * SetCard.BORDER_WIDTH + SetGame.CARD_MARGIN) - SetGame.CARD_MARGIN));
		setLayout(new GridLayout(numRows, 3, SetGame.CARD_MARGIN, SetGame.CARD_MARGIN));
	}

	public boolean hasSet(){
		return numSets() != 0;
	}

	public boolean hasSuperSet(){
		return numSuperSets() != 0;
	}
	
	public boolean hasProjectiveSet() {
		return numProjectiveSets() != 0;
	}
	
	public int numSuperSets()	{
		int size = getComponentCount();
		int numSets = 0;
		for(int i = 0; i < size; ++i){
			for(int j = 0; j < i; ++j){
				for(int k = 0; k < j; ++k){
					for(int l = 0; l < k; ++l)	{
						if(SetCardList.isSuperSet((SetCard)getComponent(i), (SetCard)getComponent(j), (SetCard)getComponent(k), (SetCard)getComponent(l)))	{ 
							++numSets;
						}
					}
				}
			}
		}
		return numSets;
	}

	public int numProjectiveSets(){
		int size = getComponentCount();
		int numSets = 0;
		for(int i = 0; i < size; ++i){
			for(int j = 0; j < i; ++j){
				for(int k = 0; k < j; ++k){
					if(SetCardList.isProjectiveSet((SetCard)getComponent(i), (SetCard)getComponent(j), (SetCard)getComponent(k))) {
						++numSets;
					}
				}
			}
		}
		return numSets;
	}
	
	public int numSets(){
		int size = getComponentCount();
		int numSets = 0;
		for(int i = 0; i < size; ++i){
			for(int j = 0; j < i; ++j){
				for(int k = 0; k < j; ++k){
					if(SetCardList.isSet((SetCard)getComponent(i), (SetCard)getComponent(j), (SetCard)getComponent(k))) ++numSets;
				}
			}
		}
		return numSets;
	}
}
