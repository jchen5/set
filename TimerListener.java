import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
	
	public class TimerListener implements ActionListener{
		public static final int TIMER_DELAY = 100;
		
		public TimerListener(JLabel lab){
			this.lab = lab;
		}
		
		public void reset(){
			time = -1;
		}
		
		public int getTime(){
			return time;
		}
		
		public int getSeconds(){
			return time * TIMER_DELAY / 1000;
		}
		
		public String getTimeString(){
			return time * TIMER_DELAY /(60 * 1000) + ":" + String.format("%02d", (time * TIMER_DELAY/1000) % 60);
		}
		
		public String congrats(){
			return ("Congratulations!\nYou finished in " + getTimeString() + "\n(" + getSeconds() + " seconds)!");
		}
		
		public void actionPerformed(ActionEvent e){
			++time;
			lab.setText("Time Elapsed: " + getTimeString());
		}
		
		JLabel lab;
		int time = -1;
	}