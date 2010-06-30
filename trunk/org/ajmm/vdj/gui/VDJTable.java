package org.ajmm.vdj.gui;
import java.awt.Component;
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
public class VDJTable extends JTable
{
	private static final long serialVersionUID = 8358734115640035667L;

	private final VDJTableModel  model;
	private final VDJTableHeader header;
	private final JPopupMenu bpmContextMenu;

	private boolean showAudio = true;
	private boolean showVideo = true;
	private boolean showKaraoke = true;

	private Set<Song> songs;

	public VDJTable()
	{
		model = new VDJTableModel();
		header = new VDJTableHeader(this);
		bpmContextMenu = new JPopupMenu();

		setModel(model);
		addMouseListener(getMouseAdapter());
	}

	private MouseAdapter getMouseAdapter()
	{
		JMenuItem menuItem = new JMenuItem("Half BPM");
		menuItem.addMouseListener(new MouseAdapter()
		{
			@SuppressWarnings("unchecked")
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (e.getButton() == MouseEvent.BUTTON1)
				{
					for (int i : getSelectedRows())
					{
						Vector<Song> vector =
							(Vector<Song>)model.getDataVector().get(i);
						Song song = vector.get(0);
						int bpm = song.bpm().getInternalBpm();
						song.bpm().setInternalBpm(bpm*2);
						int[] selectedRows = getSelectedRows();
						model.fireTableDataChanged();
						for (int j : selectedRows) {
							changeSelection(j, 0, true, false);
						}
					}
				}
			}
		});
		bpmContextMenu.add(menuItem);

		menuItem = new JMenuItem("Double BPM");
		menuItem.addMouseListener(new MouseAdapter()
		{
			@SuppressWarnings("unchecked")
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (e.getButton() == MouseEvent.BUTTON1)
				{
					for (int i : getSelectedRows())
					{
						Vector<Song> vector =
							(Vector<Song>)model.getDataVector().get(i);
						Song song = vector.get(0);
						int bpm = song.bpm().getInternalBpm();
						song.bpm().setInternalBpm((int)Math.round(bpm/2.0));
						int[] selectedRows = getSelectedRows();
						model.fireTableDataChanged();
						for (int j : selectedRows) {
							changeSelection(j, 0, true, false);
						}
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
				/* only act on a right mouse click */
				if (e.getButton() == MouseEvent.BUTTON3)
				{
					boolean match = false;
					int rowAtPoint = rowAtPoint(e.getPoint());

					for (int selectedRow : getSelectedRows()) {
						if (rowAtPoint == selectedRow) {
							match = true; break;
						}
					}

					if (!match) {
						getSelectionModel()
							.setSelectionInterval(rowAtPoint, rowAtPoint);
					}

					bpmContextMenu.setInvoker(VDJTable.this);
					bpmContextMenu.setLocation(e.getXOnScreen(), e.getYOnScreen());
					bpmContextMenu.setVisible(true);
				}
			}
		};
	}

	public void setTableData(Database database)
	{
		header.init();

		songs = database.getSongs();
		setTableData(songs);
	}

	private void setTableData(Collection<Song> songs, String filter)
	{
		/* no action if a database has not yet been loaded */
		if (songs == null) return;

		Vector<Vector<Song>> dataVector = new Vector<Vector<Song>>();
		for (Song song : songs)
		{
			if (song.getFlag() == Song.VALUE_FLAG_HIDDEN ||
					!showVideo && song.getFlag() == Song.VALUE_FLAG_VIDEO ||
					!showKaraoke && song.getFlag() == Song.VALUE_FLAG_KARAOKE ||
					!showAudio && song.getFlag() == -1)
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
				/* exit the loop if the search term was not found */
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
		/* no action if a database has not yet been loaded */
		if (songs == null) return;

		Vector<Vector<Song>> dataVector = new Vector<Vector<Song>>();
		for (Song song : songs)
		{
			if (song.getFlag() == Song.VALUE_FLAG_HIDDEN ||
					!showVideo && song.getFlag() == Song.VALUE_FLAG_VIDEO ||
					!showKaraoke && song.getFlag() == Song.VALUE_FLAG_KARAOKE ||
					!showAudio && song.getFlag() == -1)
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
