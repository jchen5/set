import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.*;

public class MenuPanel extends JPanel implements ActionListener {
	public static final int GAP = 20;

	private static final String NULL = "Pick A Mode";
	private static final String CLASSIC = "Classic Mode";
	private static final String PUZZLE = "Puzzle Mode";
	private static final String OFFICIAL_DAILY = "Official Set Daily Puzzle";
	private static final String SUPERSET = "Superset Mode";
	private static final String PROJECTIVE = "Projective Set Mode";
	private static final String PROSET = "Proset Mode";
	private static final String SPECIAL_DAILY = "Special Set Daily Puzzle";
	private static final String DAILY_ARCHIVE = "Set Daily Puzzle Archives";


	private static String[] names = new String[]
	                                           {NULL,
		CLASSIC,
		PUZZLE,
		OFFICIAL_DAILY,
		SUPERSET,
		PROJECTIVE,
		PROSET,
		SPECIAL_DAILY,
		DAILY_ARCHIVE};

	public MenuPanel(SetGame parent){
		comboBox = new JComboBox(names);
		comboBox.setSelectedIndex(0);
		comboBox.addActionListener(this);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.parent = parent;

		add(comboBox);

		add(Box.createVerticalGlue());
		add(new GenericLabel("Version " + SetGame.VERSION, 16));
	}

	public void actionPerformed(ActionEvent e) {
		JComboBox box = (JComboBox)e.getSource();
		String name = (String)box.getSelectedItem();
		if(name == CLASSIC){
			parent.runGame(new SetClassicPanel(parent));
		}
		else if(name == PUZZLE){
			parent.runGame(new SetPuzzlePanel(parent, SetPuzzlePanel.RANDOM));
		}
		else if(name == OFFICIAL_DAILY){
			parent.runGame(new SetPuzzlePanel(parent, SetPuzzlePanel.OFFICIAL));
		}
		else if(name == SPECIAL_DAILY){
			parent.runGame(new SetPuzzlePanel(parent, SetPuzzlePanel.MY_DAILY));
		}
		else if(name == DAILY_ARCHIVE){
			parent.setMainPanel(new DailyPuzzleArchivesPanel(parent));
		}
		else if(name == SUPERSET)	{
			parent.runGame(new SupersetPanel(parent));
		}
		else if(name == PROJECTIVE)	{
			parent.runGame(new ProjectiveSetPanel(parent));
		}
		else if(name == PROSET)	{
			parent.runGame(new ProsetPanel(parent));
		}
	}

	private JComboBox comboBox;
	SetGame parent;
}
