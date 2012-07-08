import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Date;
import java.io.*;
import javax.swing.*;

public class SetGame extends JApplet {
	public static final int CARD_MARGIN = 7;
	public static final int EAST_WIDTH = 200, NORTH_HEIGHT = 75, SOUTH_HEIGHT = 50;
	public static final String FONT = "Comic Sans";
	public static final int DEFAULT_FONT_SIZE = 20;
	public static final String VERSION = "5.0.0-alpha";
	public static final Color BACKGROUND = new Color(200, 100, 200);
	public static final Dimension RIGID_DIM = new Dimension(0, 30);
	public static final String LOG_FILE = "SetGame.log";
	public boolean logToFile = false;
	
	public static void main(String[] args){
		SetGame s = new SetGame();
        JFrame window = new JFrame("Productivity Destroyer " + VERSION);
        s.init();
		s.logToFile = true;				// enable logging when not run as applet
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
		
		JLabel title = new JLabel("SET", SwingConstants.CENTER);
		title.setFont(new Font(FONT, Font.BOLD, 42));
		title.setPreferredSize(new Dimension(getWidth(), NORTH_HEIGHT));
		add(title, BorderLayout.NORTH);
		
		menu = new MenuPanel(this);

		backToMenu = new MenuButton("Back to Main Menu");
		continueListener = new ContinueListener(this);
		backToMenu.addActionListener(continueListener);

		bottombar = Box.createVerticalBox();
		
		Box horBox = Box.createHorizontalBox();
		horBox.add(Box.createHorizontalGlue());
		horBox.add(backToMenu);
		horBox.add(Box.createHorizontalGlue());

		bottombar.add(horBox);
		bottombar.add(Box.createRigidArea(new Dimension(0, 10)));
		
		add(bottombar, BorderLayout.SOUTH);
		
		setMainPanel(menu);
	}
	
	public void runGame(SetPanel game){
		setMainPanel(game);
		game.start();
	}
	
	public void endGame(TimerListener tl, ArrayList<Component> comps){
		JPanel congrats = new JPanel();
		congrats.setLayout(new BoxLayout(congrats, BoxLayout.Y_AXIS));
		
		
		congrats.add(new CongratsLabel("Congratulations!"));
		congrats.add(new CongratsLabel("You finished in " + tl.getTimeString()));
		congrats.add(new CongratsLabel("(" + tl.getSecondsString() + ")."));
		
		congrats.add(Box.createRigidArea(RIGID_DIM));
		
		if(comps != null){
			for(Component c : comps){
				congrats.add(c);
			}
			congrats.add(Box.createRigidArea(RIGID_DIM));
		}
		
		congrats.add(new CongratsLabel("Press any key or click below to continue."));
		
		congrats.addKeyListener(continueListener);

		setMainPanel(congrats);
		
		continueListener.setConfirm(false);
	}

	public void start(){
		menu.setFocusable(true);
		menu.requestFocusInWindow();
	}
	
	MenuPanel menu;
	MenuButton backToMenu;
	JComponent bottombar;
	ContinueListener continueListener;

	JComponent mainPanel;

	public void setMainPanel(JComponent newPanel)
	{
		if(mainPanel != null)
			remove(mainPanel);
		add(newPanel);
		backToMenu.setVisible(newPanel != menu);
		continueListener.setConfirm(true);
		if(mainPanel != null)
		{
			validate();
			repaint();
		}
		newPanel.requestFocusInWindow();
		mainPanel = newPanel;
	}

	public void logGame(String logEntry)
	{
		Date date = new Date();
		String logLine = date + ": " + logEntry;
		System.out.println(logLine);
		if(logToFile)
		{
			try {
				PrintWriter file = new PrintWriter(new BufferedWriter(
						new FileWriter(LOG_FILE, true))); // append to file
				file.println(logLine);
				file.close();
			}
			catch (IOException ex) {
				System.err.println("Error writing set game log entry: " + ex);
				System.err.println("Log entry: " + logEntry);
			}
			catch (java.security.AccessControlException ex) {
				System.err.println("Error writing set game log entry: " + ex);
				System.err.println("Log entry: " + logEntry);
			}
		}
	}

	private class ContinueListener implements ActionListener, KeyListener{
		SetGame parent;
		boolean confirm;

		public ContinueListener(SetGame parent) {
			super();
			this.parent = parent;
			confirm = true;
		}

		public void setConfirm(boolean confirm) {
			this.confirm = confirm;
		}
		
		public void actionPerformed(ActionEvent e){
			if(confirm)
			{
				String msg = "Return to the main menu?";
				int choice = JOptionPane.showConfirmDialog(parent, msg, msg, JOptionPane.OK_CANCEL_OPTION);
				if(choice != JOptionPane.OK_OPTION)
					return;
			}
			setMainPanel(menu);
		}

		public void keyPressed(KeyEvent arg0) {}
		public void keyReleased(KeyEvent arg0) {}
		
		public void keyTyped(KeyEvent arg0) {
			actionPerformed(null);
		}
	}

	
}
