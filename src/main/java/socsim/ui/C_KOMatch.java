package socsim.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import socsim.stable.Match;

public class C_KOMatch extends Composite {
	
	private Match match;
	private Label label_team1;
	private Label label_score1;
	private Label label_team2;
	private Label label_score2;
	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public C_KOMatch(Composite parent, int style, boolean reversed) {
		super(parent, style);

		setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 2));
		setLayout(new GridLayout(2, false));
		
		label_team1 = new Label(this, SWT.NONE);
		label_team1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		label_team1.setText(match != null ? match.getHomeTeam().toString() : "Team 1");
		
		label_score1 = new Label(this, SWT.NONE);
		label_score1.setAlignment(SWT.RIGHT);
		label_score1.setText("3");
		
		label_team2 = new Label(this, SWT.NONE);
		
		label_team2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		label_team2.setText(match != null ? match.getGuestTeam().toString() : "Team 2");
		
		label_score2 = new Label(this, SWT.NONE);
		label_score2.setText("1");
		
		GridData gd_composite_komatch = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_komatch.heightHint = 50;
		gd_composite_komatch.widthHint = 100;
		setLayoutData(gd_composite_komatch);
		
		if (reversed) {
			label_score1.moveAbove(label_team1);
			label_team1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
			label_score2.moveAbove(label_team2);
			label_team2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		}
		
	}
	
	/**
	 * @wbp.factory
	 */
	public static C_KOMatch createCompositeKoMatch(Composite parent, boolean reversed) {
		return new C_KOMatch(parent, SWT.BORDER, reversed);
	}

	public void updateMatch(Match m) {
		label_team1.setText(m.getHomeTeam().toString());
		label_team2.setText(m.getGuestTeam().toString());
		label_score1.setText(Integer.toString(m.getHomeScore()));
		label_score2.setText(Integer.toString(m.getGuestScore()));

		layout();
	}
}
