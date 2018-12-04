package socsim.ui;

import java.util.Comparator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import socsim.Table.Row;
import socsim.io.Fussball_IO;

public class HistoryDialog extends Dialog {
	Object result;
	private Table table;
	Comparator<Row> comp = socsim.Table.WM_2018;
	
	public HistoryDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	public HistoryDialog(Shell parent) {
		this(parent, 0); // your default style bits go here (not the Shell's style bits)
	}
	
	public Object open() {
		Shell parent = getParent();
		Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("History");
		// Your code goes here (widget creation, set result, etc).
		createTable(shell);
		shell.setSize(600, 800);
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return result;
	}
	
	private Table createTable(Composite parent) {
		
		var tbl = socsim.Table.buildTable(Fussball_IO.HISTORY, comp);
		tbl.print(System.out);
		table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		
		createCol("Pl.");
		TableColumn t_col_team = new TableColumn(table, SWT.FILL);
		t_col_team.setWidth(180);
		t_col_team.setText("Team");
		createCol("Sp.");
		createCol("gew.");
		createCol("un.");
		createCol("verl.");
		createCol("Tore", socsim.Table.GOALS_SCORED);
		createCol("Diff.", socsim.Table.GOAL_DIFFERENCE);
		createCol("Punkte", socsim.Table.POINTS);
		
		int i = 0;
		for (Row r : tbl.getRows()) {
			i++;
			TableItem t_item_team = new TableItem(table, SWT.NONE);
			t_item_team.setText(new String[] { Integer.toString(i), //
					r.getTeam().getName(), //
					Integer.toString(r.getMatchesPlayed()), //
					Integer.toString(r.getMatchesWon()), //
					Integer.toString(r.getMatchesDrawn()), //
					Integer.toString(r.getMatchesLost()), //
					r.getGoalsFor() + ":" + r.getGoalsAgainst(), //
					Integer.toString(r.getGoalsDifference()), //
					Integer.toString(r.getPoints()) });
			t_item_team.setImage(1, r.getTeam().getFlag());
		}
		
		table.pack(true);
		return table;
	}
	
	private TableColumn createCol(String name, Comparator<Row> c) {
		var t_col = createCol(name);
		t_col.setData(c.reversed());
		t_col.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				table.setSortColumn(t_col);
				table.setSortDirection(SWT.UP);
				var tbl = socsim.Table.buildTable(Fussball_IO.HISTORY, comp);
				var rows = tbl.getRows();
				rows.sort((Comparator<Row>) t_col.getData());
				table.removeAll();
				int i = 0;
				for (Row r : rows) {
					i++;
					TableItem t_item_team = new TableItem(table, SWT.NONE);
					t_item_team.setText(new String[] { Integer.toString(i), //
							r.getTeam().getName(), //
							Integer.toString(r.getMatchesPlayed()), //
							Integer.toString(r.getMatchesWon()), //
							Integer.toString(r.getMatchesDrawn()), //
							Integer.toString(r.getMatchesLost()), //
							r.getGoalsFor() + ":" + r.getGoalsAgainst(), //
							Integer.toString(r.getGoalsDifference()), //
							Integer.toString(r.getPoints()) });
					t_item_team.setImage(1, r.getTeam().getFlag());
				}
			}
		});
		
		return t_col;
	}
	
	private TableColumn createCol(String name) {
		TableColumn t_col = new TableColumn(table, SWT.NONE);
		t_col.setWidth(50);
		t_col.setText(name);
		
		return t_col;
	}
}
