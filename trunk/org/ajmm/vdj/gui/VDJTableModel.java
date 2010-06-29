package org.ajmm.vdj.gui;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.ajmm.vdj.database.Song;

/**
 * 
 * 
 * @author	Andrew Mackrodt
 * @version	2010.06.19
 */
public class VDJTableModel extends DefaultTableModel
{
	private static final long serialVersionUID = -5497574911935034884L;

	protected void setDataVector(Vector<Vector<Song>> dataVector) {
		this.dataVector = dataVector;
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return column > 0 && column < 7;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getValueAt(int row, int column)
	{
		Song song = (Song)((Vector<Song>)dataVector.get(row)).get(0);
		Object value = null;

		if (column == 0) value = song.getFilePath();			else
		if (column == 1) value = song.display().getAuthor();	else
		if (column == 2) value = song.display().getAlbum();		else
		if (column == 3) value = song.display().getTitle();		else
		if (column == 4) value = song.display().getYear();		else
		if (column == 5) value = song.display().getGenre();		else
		if (column == 6) value = song.comment().getNodeValue();	else
		if (column == 7)
		{
			int length = song.infos().getSongLength();
			int min = (int)(length/60.0);
			int sec = length-60*min;

			value = min+":"+String.format("%02d", sec);
		}
		else
		if (column == 8) value = song.bpm().getBpm();			else
		if (column == 9) value = song.fame().getKey();

		/* do not display negative integers by returning value as null */ 
		if (value instanceof Integer && (Integer)value == -1) value = null;
		
		return value;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setValueAt(Object aValue, int row, int column)
	{
		Vector<Song> vector = (Vector<Song>)dataVector.get(row);
		Song song = vector.get(0);
		String valueString = ((String)aValue).trim();

		if (column == 1) song.display().setAuthor(valueString);			else
		if (column == 2) song.display().setAlbum(valueString);			else
		if (column == 3) song.display().setTitle(valueString);			else
		if (column == 4)
		{
			if (valueString.length() == 0) song.display().setYear(-1);	else
			if (valueString.matches("^\\d+$")) {
				song.display().setYear(Integer.parseInt(valueString));
			}
		}
		else
		if (column == 5) song.display().setGenre(valueString);			else
		if (column == 6) song.comment().setNodeValue(valueString);
		
	}
	
}
