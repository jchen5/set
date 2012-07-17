import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ProsetCardListener extends CardListener implements KeyListener, ActionListener {
	public static final char PAUSE = 'w', MORE_CARDS = 'v';
	
	public ProsetCardListener(SetPanel user){
		super(user);
		selected = ((ProsetPanel)user).selected;
	}
	
	public void actionPerformed(ActionEvent e) {
		SetCard card = (SetCard)e.getSource();
		selected.pick(card);
		user.requestFocusInWindow();
	}

	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == PAUSE){
			user.pause();
		}
		else if(e.getKeyChar() == MORE_CARDS && user.getClass().getSimpleName().equals("ProsetPanel")){
			((ProsetPanel)user).addCards.doClick();
		}
		else{
			int buttonIndex = e.getKeyChar() - 'a';
			if(buttonIndex < 0 || buttonIndex >= user.gp.getComponentCount()) return;
			((SetCard)(user.gp.getComponent(buttonIndex))).doClick();
		}
	}
}
