import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;

public abstract class SetPanel extends JPanel implements ActionListener{
	
	public SetPanel(SetGame parent){
		this.parent = parent;
		
		setLayout(new BorderLayout());
		addKeyListener(cl);
		
		gp = new SetGamePanel();
		outerPanel = new JPanel();
		outerPanel.setLayout(new FlowLayout());
		outerPanel.add(gp);
		add(outerPanel);
		
		eastPanel = new JPanel();
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
		add(eastPanel, BorderLayout.EAST);
		
		pause = new JButton("Pause (" + (char)(CardListener.PAUSE - 'a' + 'A') + ")");
		pause.addActionListener(this);
		pause.setPreferredSize(new Dimension(SetGame.EAST_WIDTH, pause.getPreferredSize().height));
		pause.setAlignmentX(Component.CENTER_ALIGNMENT);
		//button is not added: individual SetPanel types decide where it should go
		
		timeLabel = new EastLabel("");
		//timeLabel is not added: individual SetPanel types decide where it should go
		
		message = new MessageLabel(getWidth());
		add(message, BorderLayout.SOUTH);
		
		tl = new TimerListener(timeLabel);
		timer = new Timer(TimerListener.TIMER_DELAY, tl);
		//timer not started: wait until game fully ready to begin
	}
	
	public abstract void specStart();
	public abstract void pickedThree(SetCardList selected);
	
	public void start(){
		specStart();
		requestFocusInWindow();
	}
	
	public void pause(){
		if(isPaused){
			isPaused = false;
			outerPanel.add(gp);
			message.setText("Game Resumed.");
			pause.setText("Pause (" + (char)(CardListener.PAUSE - 'a' + 'A') + ")");
			timer.start();
		}
		else{
			isPaused = true;
			outerPanel.remove(gp);
			message.setText("Game Paused.");
			pause.setText("Resume (" + (char)(CardListener.PAUSE - 'a' + 'A') + ")");
			timer.stop();
		}
		repaint();
	}
	
	public void otherActionPerformed(ActionEvent e){}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == pause){
			pause();
		}
		else{
			otherActionPerformed(e);
		}
		requestFocusInWindow();
	}
	
	SetGame parent;
	SetGamePanel gp;
	JButton pause;
	JPanel outerPanel, eastPanel;
	JLabel message, timeLabel;
	Timer timer;
	TimerListener tl;
	CardListener cl = new CardListener(this);
	boolean isPaused = false;
}
