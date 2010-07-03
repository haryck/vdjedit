package org.ajmm.vdj.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.ajmm.vdj.database.Song;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.03
 */
public class ColumnManager
{
	private static final Set<String> DEFAULT_COLUMNS = new TreeSet<String>();
	private static final String[] NEW_COLUMN_NAMES = new String[] {
		"Title", "Artist", "Album", "Genre", "BPM", "Key", "Length", "Bitrate",
		"Year", "Comment", "Play Count", "First Seen", "First Play",
		"Last Play", "Cues", "Filename", "Filetype", "Filesize", "LinkedVideo",
		"Composer", "Hidden"
	};

	private static final char ASC_CHAR = '\u25bc';
	private static final char DSC_CHAR = '\u25b2';
	private final Map<String, TableColumn> columns;
	private final Map<String, Boolean> sortOrder;
	private final JTable table;
	private Object lastColumnId;

	static
	{
		DEFAULT_COLUMNS.add("Filename");
		DEFAULT_COLUMNS.add("Artist");
		DEFAULT_COLUMNS.add("Album");
		DEFAULT_COLUMNS.add("Title");
		DEFAULT_COLUMNS.add("Year");
		DEFAULT_COLUMNS.add("Genre");
		DEFAULT_COLUMNS.add("Comment");
		DEFAULT_COLUMNS.add("Length");
		DEFAULT_COLUMNS.add("BPM");
		DEFAULT_COLUMNS.add("Key");
	}

	public ColumnManager(JTable table)
	{
		this.columns = new TreeMap<String, TableColumn>();
		this.sortOrder = new TreeMap<String, Boolean>();
		this.table = table;


		final JPopupMenu columnChooser = new JPopupMenu();
		for (int i = 0; i < NEW_COLUMN_NAMES.length; i++)
		{
			final JMenuItem menuItem = new JMenuItem(NEW_COLUMN_NAMES[i]);
			columnChooser.add(menuItem);
			menuItem.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mousePressed(MouseEvent e) {
					toggleColumn(menuItem.getText());
				}
			});
		}

		table.getTableHeader().addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				// only act if the right mouse button has been clicked
				if (e.getButton() != MouseEvent.BUTTON3) return;

				columnChooser.setInvoker(ColumnManager.this.table);
				columnChooser.setLocation(e.getXOnScreen(), e.getYOnScreen());
				columnChooser.setVisible(true);
			}
		});
	}

	private void toggleColumn(String name)
	{
		try
		{
			TableColumn column = table.getColumn(name);
			table.removeColumn(column);
		}
		catch (IllegalArgumentException e)
		{
			TableColumn column = columns.get(name);
			table.addColumn(column);
		}
	}

	public void init()
	{
		if (table.getColumnCount() == 0)
		{
			JTableHeader header = table.getTableHeader();
			header.addMouseListener(getMouseAdapter());

			createColumns(NEW_COLUMN_NAMES);

			columns.get("Title").setPreferredWidth(224);
			columns.get("Artist").setPreferredWidth(160);
			columns.get("Album").setPreferredWidth(160);
			columns.get("Genre").setPreferredWidth(80);
			columns.get("BPM").setPreferredWidth(50);
			columns.get("Key").setPreferredWidth(46);
			columns.get("Length").setPreferredWidth(64);
			columns.get("Bitrate").setPreferredWidth(60);
			columns.get("Year").setPreferredWidth(52);
			columns.get("Comment").setPreferredWidth(160);
			columns.get("Play Count").setPreferredWidth(84);
			columns.get("First Seen").setPreferredWidth(120);
			columns.get("First Play").setPreferredWidth(120);
			columns.get("Last Play").setPreferredWidth(120);
			columns.get("Cues").setPreferredWidth(60);
			columns.get("Filename").setPreferredWidth(20);
			columns.get("Filetype").setPreferredWidth(68);
			columns.get("Filesize").setPreferredWidth(68);
			columns.get("LinkedVideo").setPreferredWidth(256);
			columns.get("Composer").setPreferredWidth(160);
			columns.get("Hidden").setPreferredWidth(66);

		}
		else if (lastColumnId != null)
		{
			TableColumn column = columns.get(lastColumnId);
			column.setHeaderValue((String)lastColumnId);
		}
	}

	private MouseAdapter getMouseAdapter()
	{
		final DefaultTableModel model = (DefaultTableModel)table.getModel();
		final JTableHeader header = table.getTableHeader();

		return new MouseAdapter()
		{
			@SuppressWarnings("unchecked")
			@Override
			public void mouseClicked(MouseEvent e)
			{
				// only respond to a left mouse button event
				if (e.getButton() != MouseEvent.BUTTON1) return;

				int col = header.columnAtPoint(e.getPoint());
				String identifier = table.getColumnName(col);

				Boolean ascend = sortOrder.get(identifier);
				ascend = (ascend == null) ? true : ascend;
				Vector<Vector<Object>> data = model.getDataVector();
				Collections.sort(data, getRowComparator(identifier, !ascend));
				sortOrder.put(identifier, !ascend);

				if (lastColumnId != null)
				{
					TableColumn column = columns.get(lastColumnId);
					column.setHeaderValue((String)lastColumnId);
				}

				TableColumn column = header.getColumnModel().getColumn(col);
				String name = (String)column.getHeaderValue() + ' '
						+ (ascend ? DSC_CHAR : ASC_CHAR);
				column.setHeaderValue(name);
				lastColumnId = column.getIdentifier();

				model.fireTableDataChanged();
			}
		};
	}

	private Comparator<Vector<Object>> getRowComparator(
			final String identifier, final boolean descend)
	{
		return new Comparator<Vector<Object>>()
		{
			public int compare(Vector<Object> v1, Vector<Object> v2)
			{
				Object o1 = getSongComparatorValue((Song)v1.get(0), identifier);
				Object o2 = getSongComparatorValue((Song)v2.get(0), identifier);

				if (o1 == null) return 1;
				if (o2 == null) return -1;

				int order = (o1 instanceof String)
						? ((String)o1).compareToIgnoreCase((String)o2)
						: (Integer)o1 - (Integer)o2;

				if (descend) order *= -1;
				if (identifier.equals("BPM")) order *= -1; // bpm column must be inverted

				return order;
			}

		};
	}

	private static Object getSongComparatorValue(Song song, String identifier)
	{
		Object value = null;

		if (identifier.equals("Filename"))    value = song.getFilePath();				else
		if (identifier.equals("Artist"))	  value = song.display().getAuthor();		else
		if (identifier.equals("Album"))       value = song.display().getAlbum();		else
		if (identifier.equals("Title"))       value = song.display().getTitle();		else
		if (identifier.equals("Year"))        value = song.display().getYear();			else
		if (identifier.equals("Genre"))       value = song.display().getGenre();		else
		if (identifier.equals("Comment"))     value = song.comment().get();				else
		if (identifier.equals("Length"))      value = song.infos().getVdjSongLength();	else
		if (identifier.equals("BPM"))         value = song.bpm().getVdjBpm();			else
		if (identifier.equals("Key"))         value = song.fame().getKey();				else
		if (identifier.equals("Bitrate"))     value = song.infos().getBitrate();		else
		if (identifier.equals("Play Count"))  value = song.infos().getPlayCount();		else
		if (identifier.equals("First Seen"))  value = song.infos().getFirstSeen();		else
		if (identifier.equals("First Play"))  value = song.infos().getFirstPlay();		else
		if (identifier.equals("Last Play"))   value = song.infos().getLastPlay();		else
		if (identifier.equals("Cues"))		  value = song.cue().size();				else
		if (identifier.equals("Filetype"))    value = TableModel.formatFileType(song);	else
		if (identifier.equals("Filesize"))    value = song.getFileSize();				else
		if (identifier.equals("LinkedVideo")) value = song.link().getVideo();			else
		if (identifier.equals("Composer"))    value = song.display().getComposer();		else
		if (identifier.equals("Hidden"))  	  value = TableModel.formatHidden(song);

		return value;
	}

	/**
	 * Populate the table with the given column names. This method is
	 * preferred to {@link JTable#addColumn(TableColumn)} as column
	 * identifiers remain static even if the header value is changed
	 *
	 * @param columnNames array of column titles (also used as identifiers)
	 */
	private void createColumns(String[] columnNames)
	{
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setColumnIdentifiers(columnNames);

        // explicitly set each column identifier so that it's not
		// automatically changed if we later change the column header
		for (int i = 0; i < columnNames.length; i++)
		{
			TableColumn column = table.getColumn(columnNames[i]);
			column.setMinWidth(0);
			column.setIdentifier(columnNames[i]);
			columns.put(columnNames[i], column);

			if (!DEFAULT_COLUMNS.contains(columnNames[i])) table.removeColumn(column);
		}

		int index = table.getColumnModel().getColumnIndex("Filename");
		table.getColumnModel().moveColumn(index, 0);
	}

}
