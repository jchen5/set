import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

public class SetGame extends JApplet {
	public static final int CARD_MARGIN = 7;
	public static final int EAST_WIDTH = 200, NORTH_HEIGHT = 75, SOUTH_HEIGHT = 50;
	public static final String FONT = "Comic Sans";
	public static final String VERSION = "3.0";
	public static final Color BACKGROUND = new Color(200, 100, 200);
	
	public static void main(String[] args){
		SetGame s = new SetGame();
        JFrame window = new JFrame("Productivity Destroyer " + VERSION);
        s.init();
        window.setContentPane(s);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();              // Arrange the components.
        window.setVisible(true);    // Make the window visible
        s.start(); //call start after setVisible(true) so that GUI is setup by the time start is called
	}
	
	
	public void init(){
		setPreferredSize(new Dimension(3 * (SetCard.PIC_WIDTH + 2 * SetCard.BORDER_WIDTH) + 4 * CARD_MARGIN + EAST_WIDTH,
				7 * (SetCard.PIC_HEIGHT + 2 * SetCard.BORDER_WIDTH) + 8 * CARD_MARGIN + NORTH_HEIGHT + SOUTH_HEIGHT));
		setLayout(new BorderLayout());
		setFocusable(false);
		
		JLabel title = new JLabel("Set v. " + VERSION, SwingConstants.CENTER);
		title.setFont(new Font(FONT, Font.BOLD, 42));
		title.setPreferredSize(new Dimension(getWidth(), NORTH_HEIGHT));
		add(title, BorderLayout.NORTH);
		menu = new MenuPanel(this);
		add(menu);
	}
	
	public void runGame(SetPanel g){
		game = g;
		remove(menu);
		add(game);
		validate();
		repaint();
		game.start();
	}
	
	public void endGame(TimerListener tl){
		remove(game);
		
		congrats = new JPanel();
		congrats.setLayout(new BoxLayout(congrats, BoxLayout.Y_AXIS));
		
		congrats.add(new CongratsLabel("Congratulations!"));
		congrats.add(new CongratsLabel("You finished in " + tl.getTimeString()));
		congrats.add(new CongratsLabel("(" + tl.getSeconds() + " seconds)."));
		
		congrats.add(Box.createRigidArea(new Dimension(0, 30)));
		
		congrats.add(new CongratsLabel("Press any key or click below to continue."));
		
		congrats.add(Box.createVerticalGlue());
		
		ContinueListener col = new ContinueListener();
		MenuButton continueOn = new MenuButton("Back to Menu");
		continueOn.addActionListener(col);
		congrats.add(continueOn);
		
		congrats.add(Box.createRigidArea(new Dimension(0, 30)));
		
		congrats.addKeyListener(col);
		add(congrats);		
		validate();
		repaint();
		congrats.requestFocusInWindow();
	}
	
	public void start(){
		menu.setFocusable(true);
		menu.addKeyListener(menu);
		menu.requestFocusInWindow();
	}
	
	MenuPanel menu;
	JPanel congrats;
	SetPanel game;
	
	private class ContinueListener implements ActionListener, KeyListener{
		public void actionPerformed(ActionEvent e){
			remove(congrats);
			add(menu);
			validate();
			repaint();
			menu.requestFocusInWindow();
		}

		public void keyPressed(KeyEvent arg0) {}
		public void keyReleased(KeyEvent arg0) {}
		
		public void keyTyped(KeyEvent arg0) {
			actionPerformed(null);
		}
	}
	
	private class CongratsLabel extends JLabel{
		public static final int DEFAULT_SIZE = 24;
		
		public CongratsLabel(String text){
			super(text);
			setFont(new Font(FONT, Font.PLAIN, DEFAULT_SIZE));
			setAlignmentX(CENTER_ALIGNMENT);
		}
	}
}
