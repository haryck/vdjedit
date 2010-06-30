package org.ajmm.vdj.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.ajmm.vdj.database.Song;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.06.30
 */
public class VDJTableHeader
{
	private static final String[] COLUMN_NAMES = new String[] {
		"Filename", "Artist", "Album", "Title", "Year", "Genre", "Comment",
		"Length", "BPM", "Key"
	};
	
	private static final char DESCENDING_CHAR = '\u25bc';
	private static final char ASCENDING_CHAR = '\u25b2';	
	private final Map<String, Boolean> sortOrder;
	private final JTable table;
	private Object lastColumnId;

	public VDJTableHeader(JTable table)
	{
		this.sortOrder = new TreeMap<String, Boolean>();
		this.table = table;
	}

	public void init()
	{
		if (table.getColumnCount() == 0)
		{
			JTableHeader header = table.getTableHeader();
			header.addMouseListener(getMouseAdapter());

			addColumns(COLUMN_NAMES);

			table.getColumn("Filename").setPreferredWidth(0);
			table.getColumn("Artist").setPreferredWidth(64);
			table.getColumn("Album").setPreferredWidth(128);
			table.getColumn("Title").setPreferredWidth(256);
			table.getColumn("Year").setMaxWidth(48);
			table.getColumn("Genre").setMaxWidth(96);
			table.getColumn("Comment").setPreferredWidth(160);
			table.getColumn("Length").setMaxWidth(56);
			table.getColumn("BPM").setMaxWidth(48);
			table.getColumn("Key").setMaxWidth(40);
		}
		else if (lastColumnId != null)
		{
			TableColumn column = table.getColumn(lastColumnId);
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
				/* only respond to a left mouse button event */
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
					TableColumn column = table.getColumn(lastColumnId);
					column.setHeaderValue((String)lastColumnId);
				}

				TableColumn column = header.getColumnModel().getColumn(col);
				String name = (String)column.getHeaderValue() + ' '
						+ (ascend ? ASCENDING_CHAR : DESCENDING_CHAR);
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
				if (identifier.equals("BPM")) order *= -1; /* bpm column must be inverted */

				return order;
			}

		};
	}

	private static Object getSongComparatorValue(Song song, String identifier)
	{
		Object value = null;

		if (identifier.equals("Filename"))  value = song.getFilePath();						else
		if (identifier.equals("Artist"))	value = song.display().getAuthor();				else
		if (identifier.equals("Album"))     value = song.display().getAlbum();				else
		if (identifier.equals("Title"))     value = song.display().getTitle();				else
		if (identifier.equals("Year"))      value = song.display().getYear();				else
		if (identifier.equals("Genre"))     value = song.display().getGenre();				else
		if (identifier.equals("Comment"))   value = song.comment().getNodeValue();			else
		if (identifier.equals("Length"))    value = song.infos().getInternalSongLength();	else
		if (identifier.equals("BPM"))       value = song.bpm().getInternalBpm();			else
		if (identifier.equals("Key"))       value = song.fame().getKey();

		return value;
	}

	/**
	 * Populate the table with the given column names. This method is
	 * preferred to {@link JTable#addColumn(TableColumn)} as column
	 * identifiers remain static even if the header value is changed
	 *
	 * @param headers array of column titles (also used as identifiers)
	 */
	private void addColumns(String[] headers)
	{
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setColumnIdentifiers(COLUMN_NAMES);

		/*
		 * explicitly set each column identifier so that it's not
		 * automatically changed if we later change the column header
		 */
		for (int i = 0; i < COLUMN_NAMES.length; i++)
		{
			TableColumn column = table.getColumn(COLUMN_NAMES[i]);
			column.setMinWidth(0);
			column.setIdentifier(COLUMN_NAMES[i]);
		}
	}

}
