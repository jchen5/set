import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.net.URL;

public class DailyPuzzleArchivesPanel extends JPanel implements ActionListener
{
	SetGame parent;
	JFormattedTextField date;
	
	public DailyPuzzleArchivesPanel(SetGame parent)
	{
		this.parent = parent;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		MaskFormatter dateFormatter = null;
		try {
			dateFormatter = new MaskFormatter("####-##-##");
		}
		catch(java.text.ParseException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		
		date = new JFormattedTextField(dateFormatter);
		date.setFont(new Font("monospaced", Font.PLAIN, SetGame.DEFAULT_FONT_SIZE));
		date.setHorizontalAlignment(SwingConstants.CENTER);
		date.setColumns(11);
		date.setMaximumSize(date.getPreferredSize());

		MenuButton continueButton = new MenuButton("Play");

		date.addActionListener(this);
		continueButton.addActionListener(this);
				
		add(new GenericLabel("Enter date (YYYY-MM-DD)"));
		add(Box.createRigidArea(new Dimension(0, 20)));
		add(date);
		add(Box.createVerticalGlue());
		add(continueButton);
		add(Box.createRigidArea(SetGame.RIGID_DIM));
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(date.getValue() == null)
			return;
		
		SetPuzzlePanel game = new SetPuzzlePanel(parent, SetPuzzlePanel.ARCHIVES);
		game.setDailyPuzzleArchivesDate((String)date.getValue());
		parent.runGame(game);
	}

	public boolean requestFocusInWindow()
	{
		return date.requestFocusInWindow();
	}
}
