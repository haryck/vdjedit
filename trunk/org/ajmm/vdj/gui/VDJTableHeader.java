package org.ajmm.vdj.gui;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.ajmm.vdj.database.Song;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.06.28
 */
public class VDJTableHeader extends JTableHeader
{
	private static final long serialVersionUID = -1816760537419500898L;

	public static final String[] COLUMN_NAMES = new String[] {
		"Filename", "Artist", "Album", "Title", "Year", "Genre", "Comment",
		"Length", "BPM", "Key" };
	private static final char DESCENDING_CHAR = '\u25bc';
	private static final char ASCENDING_CHAR = '\u25b2';
	private final boolean[] isDescendingOrder;
	private int lastSortedColumn = -1;

	public VDJTableHeader()
	{
		this.isDescendingOrder = new boolean[COLUMN_NAMES.length];
		setReorderingAllowed(false);
		addMouseListener(getMouseAdapter());
	}

	public void reset()
	{
		if (lastSortedColumn >= 0)
		{
			TableColumn tableColumn = getColumnModel().getColumn(lastSortedColumn);
			String columnHeader = (String)tableColumn.getHeaderValue();
			columnHeader = columnHeader.substring(0, columnHeader.length()-2);
			tableColumn.setHeaderValue(columnHeader);

			for (int i = 0; i < isDescendingOrder.length; i++) {
				isDescendingOrder[i] = false;
			}
			lastSortedColumn = -1;
		}

		repaint();
	}

	private MouseAdapter getMouseAdapter()
	{
		return new MouseAdapter()
		{
			@SuppressWarnings("unchecked")
			@Override
			public void mouseClicked(MouseEvent e)
			{
				/* only respond to a left mouse button event */
				if (e.getButton() != MouseEvent.BUTTON1) return;

				DefaultTableModel model =
					(DefaultTableModel)getTable().getModel();

				int column = columnAtPoint(e.getPoint());
				Vector<Vector<Object>> data = model.getDataVector();
				Collections.sort(data, getRowComparator(column));

				isDescendingOrder[column] = !isDescendingOrder[column];


				TableColumn tableColumn;
				String columnHeader;

				if (lastSortedColumn >= 0)
				{
					tableColumn = getColumnModel().getColumn(lastSortedColumn);
					columnHeader = (String)tableColumn.getHeaderValue();
					columnHeader = columnHeader.substring(0, columnHeader.length()-2);
					tableColumn.setHeaderValue(columnHeader);
				}

				tableColumn = getColumnModel().getColumn(column);
				columnHeader = (String)tableColumn.getHeaderValue() + ' ' +
					(isDescendingOrder[column] ? ASCENDING_CHAR : DESCENDING_CHAR);
				tableColumn.setHeaderValue(columnHeader);
				lastSortedColumn = column;

				model.fireTableDataChanged();
				repaint(); /* required when using setReorderingAllowed == false */
			}
		};
	}

	private Comparator<Vector<Object>> getRowComparator(final int column)
	{
		return new Comparator<Vector<Object>>()
		{
			public int compare(Vector<Object> v1, Vector<Object> v2)
			{
				Object o1 = getSongComparatorValue((Song)v1.get(0), column);
				Object o2 = getSongComparatorValue((Song)v2.get(0), column);

				if (o1 == null) return 1;
				if (o2 == null) return -1;

				int order = (o1 instanceof String)
						? ((String)o1).compareToIgnoreCase((String)o2)
						: (Integer)o1 - (Integer)o2;

				if (isDescendingOrder[column]) order *= -1;
				if (column == 8) order *= -1; /* bpm column must be inverted */

				return order;
			}

		};
	}

	private Object getSongComparatorValue(Song song, int index)
	{
		Object value = null;

		if (index == 0) value = song.getFilePath();						else
		if (index == 1) value = song.display().getAuthor();				else
		if (index == 2) value = song.display().getAlbum();				else
		if (index == 3) value = song.display().getTitle();				else
		if (index == 4) value = song.display().getYear();				else
		if (index == 5) value = song.display().getGenre();				else
		if (index == 6) value = song.comment().getNodeValue();			else
		if (index == 7) value = song.infos().getInternalSongLength();	else
		if (index == 8) value = song.bpm().getInternalBpm();			else
		if (index == 9) value = song.fame().getKey();

		return value;
	}

}
