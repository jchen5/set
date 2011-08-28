import java.awt.Dimension;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.TimeZone;

import javax.swing.*;

public class SetPuzzlePanel extends SetPanel {
	public static final int TOTAL_SETS = 6;
	public static final int TOTAL_CARDS = 12;
	public static final int RANDOM = 0, OFFICIAL = 1, MY_DAILY = 2, ARCHIVES = 3;
	public static final String flag = "initSetCards";
	
	public SetPuzzlePanel(SetGame parent, int type){
		super(parent);
		this.type = type;
	}

	public void setDailyPuzzleArchivesDate(String date) {
		dailyPuzzleArchivesDate = date;
	}

	private boolean grabDailyPuzzle(URL site){
		try {
			URLConnection conn = site.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while((line = br.readLine()) != null){
				cards = new ArrayList<SetCard>();
				if(line.contains(flag)){
					Scanner s = new Scanner(line);
					s.useDelimiter("[, (]++");
					s.skip(".*" + flag);
					for(int i = 0; i < 12; ++i){
						int nextCard = s.nextInt() - 1;
						cards.add(new SetCard(nextCard, cl));
					}
					return true;
				}
			}
		}
		catch (java.io.IOException ex) {
			System.err.println("Error downloading Daily Puzzle: " + ex);
		}
		return false;
	}

	private URL createURL(String spec) {
		try {
			return new URL(spec);
		}
		catch (java.net.MalformedURLException ex)
		{
			System.err.println("Unexpected error creating URL from input string: " + spec);
			ex.printStackTrace();
			System.exit(1);
			return null;
		}
	}
	
	private boolean grabDailyPuzzle(){
		return grabDailyPuzzle(createURL("http://www.setgame.com/puzzle/set.htm"));
	}
		
	private void generatePuzzle(Random rand){
		ArrayList<Integer> nums = new ArrayList<Integer>();
		for(int i = 0; i < 81; ++i){
			//note the zero-indexing for modular arithmetic's sake
			nums.add(i);
		}
		while(true){
			Collections.shuffle(nums, rand);
			int numSets = 0;
			HashSet<Integer> lookupTable = new HashSet<Integer>();
			for(int i = 0; i < TOTAL_CARDS; ++i){
				lookupTable.add(nums.get(i));
			}
			for(int i = 0; i < TOTAL_CARDS; ++i){
				for(int j = 0; j < i; ++j){
					if(lookupTable.contains(SetCardList.getThird(nums.get(i), nums.get(j)))) ++numSets;
				}
			}
			numSets /= 3; //overcounts by a factor of 3
			if(numSets == TOTAL_SETS) break;
		}
		cards = new ArrayList<SetCard>();
		for(int i = 0; i < TOTAL_CARDS; ++i){
			cards.add(new SetCard(nums.get(i), cl));
		}
	}
	
	private void generateRandom(){
		generatePuzzle(new Random());
	}
	
	private int makeDateSeed(){
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
		//System.out.println(now.getTime());
		int prime = 314159; //win
		return now.get(Calendar.DAY_OF_YEAR) * prime + now.get(Calendar.YEAR);
	}
	
	private void generateDaily(){
		generatePuzzle(new Random(makeDateSeed()));
	}
	
	private void layoutStartingCards(){
		for(int i = 0; i < cards.size(); ++i){
			gp.add(cards.get(i));
		}
		gp.updateSize();
	}
		
	public void specStart() {
		
		switch(type){
		case RANDOM:
			generateRandom();
			break;
		case OFFICIAL:
			if(!grabDailyPuzzle()){
				message.setText("Unable to acquire Daily Puzzle. Please check your internet connection.");
				return;
			}
			break;
		case MY_DAILY:
			generateDaily();
			break;
		case ARCHIVES:
			URL target = createURL("http://stanford.edu/~jachen2/setpuzzle/" + dailyPuzzleArchivesDate + ".htm");
			if(!grabDailyPuzzle(target)) {
				message.setText("Unable to acquire Daily Puzzle for " + dailyPuzzleArchivesDate + "."); 
				// "Possible causes: the requested puzzle is not in the archives, the date is invalid, or your internet connection is not working."
				return;
			}
			eastPanel.add(new EastLabel(dailyPuzzleArchivesDate));
			break;
		}

		eastPanel.add(pause);
		
		setsLeft = new EastLabel("Sets Left: " + TOTAL_SETS);
		eastPanel.add(setsLeft);
		
		eastPanel.add(timeLabel);
		eastPanel.add(new EastLabel("Found So Far:"));
		
		layoutStartingCards();
		timer.start();
	}

	private void showFound(SetCardList set){
		Collections.sort(set); //arranges in order of number, nothing else
		eastPanel.add(Box.createRigidArea(new Dimension(0, FoundPanel.GAP)));
		eastPanel.add(new FoundPanel(set));
		validate();
		repaint();
	}
	
	public void pickedThree(SetCardList selected) {
		if(selected.isSet()){
			if(foundSets.contains(selected)){
				message.setText("You already found that set!");
			}
			else{
				foundSets.add((SetCardList)selected.clone()); //clone method is ArrayList's, but does everything we need
				setsLeft.setText("Sets Left: " + (TOTAL_SETS - foundSets.size()));
				showFound(selected);
				if(foundSets.size() == TOTAL_SETS){
					timer.stop();
					parent.endGame(tl, null);
					//end game
				}
				else{
					message.setText("Set!");
				}
			}
		}
		else{
			message.setText("You fail. That is not a set.");
		}
	}
	
	private class FoundPanel extends JPanel{
		public static final double SCALE = .5;
		public static final int GAP = 5;
		
		public FoundPanel(SetCardList set){
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			for(SetCard card : set){
				Icon icon = card.getIcon();
				Image smaller = ((ImageIcon) icon).getImage().getScaledInstance((int) (icon.getIconWidth() * SCALE), -1, Image.SCALE_DEFAULT);
				add(new JLabel(new ImageIcon(smaller)));
				add(Box.createRigidArea(new Dimension(GAP, 0)));
			}
		}
	}
	
	ArrayList<SetCard> cards;
	ArrayList<SetCardList> foundSets = new ArrayList<SetCardList>();
	JLabel setsLeft;
	int type;
	String dailyPuzzleArchivesDate;
}
