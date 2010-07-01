package org.ajmm.vdj.gui;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.ajmm.vdj.database.Database;
import org.ajmm.vdj.database.Song;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.06.30
 */
public class Table extends JTable
{
	private static final long serialVersionUID = 8358734115640035667L;

	private final TableModel  model;
	private final ColumnManager cm;
	private final JPopupMenu bpmContextMenu;

	private boolean showAudio = true;
	private boolean showVideo = true;
	private boolean showKaraoke = true;
	private boolean showHidden = false;

	private Set<Song> songs;

	public Table()
	{
		model = new TableModel();
		cm = new ColumnManager(this);
		bpmContextMenu = new JPopupMenu();

		setModel(model);
		addMouseListener(getMouseAdapter());
	}

	private MouseAdapter getMouseAdapter()
	{
		JMenuItem menuItem = new JMenuItem("Half BPM");
		menuItem.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				// only act if the left mouse button has been pressed
				if (e.getButton() != MouseEvent.BUTTON1) return;

				for (int i : getSelectedRows())
				{
					Song song = model.getSong(i);
					int bpm = song.bpm().getVdjBpm();
					song.bpm().setVdjBpm(bpm*2);
					int[] selectedRows = getSelectedRows();
					model.fireTableDataChanged();
					for (int j : selectedRows) {
						changeSelection(j, 0, true, false);
					}
				}
			}
		});
		bpmContextMenu.add(menuItem);

		menuItem = new JMenuItem("Double BPM");
		menuItem.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				// only act if the left mouse button has been pressed
				if (e.getButton() != MouseEvent.BUTTON1) return;

				for (int i : getSelectedRows())
				{
					Song song = model.getSong(i);
					int bpm = song.bpm().getVdjBpm();
					song.bpm().setVdjBpm((int)Math.round(bpm/2.0));
					int[] selectedRows = getSelectedRows();
					model.fireTableDataChanged();
					for (int j : selectedRows) {
						changeSelection(j, 0, true, false);
					}
				}
			}
		});
		bpmContextMenu.add(menuItem);

		return new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				// only act if the right mouse button has been clicked
				if (e.getButton() != MouseEvent.BUTTON3) return;

				if (!isCursorOverSelection(e.getPoint())) {
					int row = rowAtPoint(e.getPoint());
					getSelectionModel().setSelectionInterval(row, row);
				}

				bpmContextMenu.setInvoker(Table.this);
				bpmContextMenu.setLocation(e.getXOnScreen(), e.getYOnScreen());
				bpmContextMenu.setVisible(true);
			}
		};
	}

	private boolean isCursorOverSelection(Point p)
	{
		boolean match = false;
		int row = rowAtPoint(p);
		for (int i : getSelectedRows()) {
			if (row == i) {
				match = true; break;
			}
		}
		return match;
	}

	public void setTableData(Database database)
	{
		songs = database.getSongs();
		cm.init();
		setTableData(songs);
	}

	private void setTableData(Collection<Song> songs, String filter)
	{
		// no action if a database has not yet been loaded
		if (songs == null) return;

		Vector<Vector<Song>> dataVector = new Vector<Vector<Song>>();
		for (Song song : songs)
		{
			int flag = (song.getFlag() > 0) ? song.getFlag() : 0;
			int type = flag - (flag & Song.VALUE_FLAG_HIDDEN);
			if (!showHidden && flag != type
					|| !showVideo && type == Song.VALUE_FLAG_VIDEO
					|| !showKaraoke && type == Song.VALUE_FLAG_KARAOKE
					|| !showAudio && type == 0)
				continue;

			String[] filters = filter.trim().replaceAll("  +", " ").toLowerCase().split(" ");
			String searchString =
					song.display().getTitle() + " " +
					song.display().getAuthor() + " " +
					song.display().getAlbum() + " " +
					song.display().getGenre() + " " +
					song.display().getYear();

			searchString = searchString.toLowerCase();

			for (int i = 0; i < filters.length; i++)
			{
				// exit the loop if the search term was not found
				if (!searchString.contains(filters[i])) break;

				if (i == filters.length-1)
				{
					Vector<Song> v = new Vector<Song>();
					v.add(song);
					dataVector.add(v);
				}
			}
		}

		model.setDataVector(dataVector);
		model.fireTableDataChanged();
	}

	private void setTableData(Collection<Song> songs)
	{
		// no action if a database has not yet been loaded
		if (songs == null) return;

		Vector<Vector<Song>> dataVector = new Vector<Vector<Song>>();
		for (Song song : songs)
		{
			int flag = (song.getFlag() > 0) ? song.getFlag() : 0;
			int type = flag - (flag & Song.VALUE_FLAG_HIDDEN);
			if (!showHidden && flag != type
					|| !showVideo && type == Song.VALUE_FLAG_VIDEO
					|| !showKaraoke && type == Song.VALUE_FLAG_KARAOKE
					|| !showAudio && type == 0)
				continue;

			Vector<Song> v = new Vector<Song>();
			v.add(song);
			dataVector.add(v);
		}

		model.setDataVector(dataVector);
		model.fireTableDataChanged();
	}

	public void filter(String filter)
	{
		if (filter == null || filter.length() == 0)
			setTableData(songs);
		else setTableData(songs, filter);
	}

	public void toggleShowAudio()
	{
		showAudio = !showAudio;
		setTableData(songs);
	}

	public void toggleShowVideo()
	{
		showVideo = !showVideo;
		setTableData(songs);
	}

	public void toggleShowKaraoke()
	{
		showKaraoke = !showKaraoke;
		setTableData(songs);
	}

	public void toggleShowHidden()
	{
		showHidden = !showHidden;
		setTableData(songs);
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
	{
		Component c = super.prepareRenderer(renderer, row, column);
		if (c instanceof JComponent)
		{
			JComponent component = (JComponent)c;
			String identifier = getColumnName(column);
			String text = (identifier.equals("Filename"))
					? (String)getValueAt(row, column) : null;
			component.setToolTipText(text);
		}

		return c;
	}

}
