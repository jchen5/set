import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class MenuPanel extends JPanel implements ActionListener, KeyListener {
	public static final int GAP = 20;
	
	public MenuPanel(SetGame parent){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		addKeyListener(this);
		
		this.parent = parent;
		
		classic = new MenuButton("Classic Mode", buttons.size());
		buttons.add(classic);

		genPuzzle = new MenuButton("Puzzle Mode", buttons.size());
		buttons.add(genPuzzle);
		
		dailyPuzzle = new MenuButton("Play Set Daily Puzzle", buttons.size());
		buttons.add(dailyPuzzle);
		
		for(MenuButton mb : buttons){
			add(Box.createRigidArea(new Dimension(0, GAP)));
			mb.addActionListener(this);
			add(mb);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == classic){
			parent.runGame(new SetClassicPanel(parent));
		}
		else if(e.getSource() == dailyPuzzle){
			parent.runGame(new SetPuzzlePanel(parent, true));
		}
		else if(e.getSource() == genPuzzle){
			parent.runGame(new SetPuzzlePanel(parent, false));
		}
	}

	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	
	public void keyTyped(KeyEvent e) {
		int buttonIndex = e.getKeyChar() - 'a';
		if(buttonIndex < 0 || buttonIndex >= buttons.size()) return;
		buttons.get(buttonIndex).doClick();
	}
	
	MenuButton classic, dailyPuzzle, genPuzzle;
	ArrayList<MenuButton> buttons = new ArrayList<MenuButton>();
	SetGame parent;
}