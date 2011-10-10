import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
	
	public class TimerListener implements ActionListener{
		public static final int TIMER_DELAY = 100;
		
		public TimerListener(JLabel label){
			this.label = label;
			setLabelText();
		}
		
		public void reset(){
			time = -1;
		}
		
		public int getTicks(){
			return time;
		}
		
		public int getSeconds(){
			return time * TIMER_DELAY / 1000;
		}

		public double getTime(){
			return time * TIMER_DELAY / 1000.0;
		}
		
		public static String makeTimeString(int secs){
			return secs/60 + ":" + String.format("%02d", (secs % 60));
		}
		
		public String getTimeString(){
			return makeTimeString(getSeconds());
		}
		
		public String getSecondsString(){
			if(getTime() < 20)
				return getTime() + " seconds";
			else
				return getSeconds() + " seconds";
		}
		
		public void actionPerformed(ActionEvent e){
			++time;
			setLabelText();
		}

		public void setLabelText(){
			label.setText("Time Elapsed: " + getTimeString());
		}
		
		JLabel label;
		int time = 0;
	}
