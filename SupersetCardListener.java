import java.awt.event.*;

public class SupersetCardListener extends CardListener implements KeyListener, ActionListener {
	public static final char PAUSE = 'w', MORE_CARDS = 'v';
	
	public SupersetCardListener(SetPanel user){
		super(user);
	}
	
	public void actionPerformed(ActionEvent e) {
		SetCard card = (SetCard)e.getSource();
		selected.pick(card);
		if(selected.size() == 4){
			((SupersetPanel)user).pickedFour(selected);
			selected.deselect();
			selected.clear();
		}
		user.requestFocusInWindow();
	}

	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == PAUSE){
			user.pause();
		}
		else if(e.getKeyChar() == MORE_CARDS && user.getClass().getSimpleName().equals("SupersetPanel")){
			((SupersetPanel)user).addCards.doClick();
		}
		else{
			int buttonIndex = e.getKeyChar() - 'a';
			if(buttonIndex < 0 || buttonIndex >= user.gp.getComponentCount()) return;
			((SetCard)(user.gp.getComponent(buttonIndex))).doClick();
		}
	}
}
