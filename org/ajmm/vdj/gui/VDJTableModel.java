package org.ajmm.vdj.gui;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.ajmm.vdj.database.Song;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.06.30
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
		
		String identifier = getColumnName(column);
		if (identifier.equals("Filename")) value = song.getFilePath();			    else
		if (identifier.equals("Artist"))   value = song.display().getAuthor();	    else
		if (identifier.equals("Album"))    value = song.display().getAlbum();		else
		if (identifier.equals("Title"))    value = song.display().getTitle();		else
		if (identifier.equals("Year"))     value = song.display().getYear();		else
		if (identifier.equals("Genre"))    value = song.display().getGenre();		else
		if (identifier.equals("Comment"))  value = song.comment().getNodeValue();	else
		if (identifier.equals("Length"))
		{
			int length = song.infos().getSongLength();
			int min = (int)(length/60.0);
			int sec = length-60*min;

			value = min+":"+String.format("%02d", sec);
		}
		else
		if (identifier.equals("BPM")) value = song.bpm().getBpm();			else
		if (identifier.equals("Key")) value = song.fame().getKey();

		/* do not display negative integers by returning value as null */
		if (value instanceof Integer && (Integer)value < 0) value = null;

		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValueAt(Object aValue, int row, int column)
	{
		Vector<Song> vector = (Vector<Song>)dataVector.get(row);
		Song song = vector.get(0);
		String valueString = ((String)aValue).trim();

		String identifier = getColumnName(column);
		if (identifier.equals("Artist"))  song.display().setAuthor(valueString);     	else
		if (identifier.equals("Album"))   song.display().setAlbum(valueString);			else
		if (identifier.equals("Title"))   song.display().setTitle(valueString);			else
		if (identifier.equals("Year"))
		{
			if (valueString.length() == 0) song.display().setYear(-1);	else
			if (valueString.matches("^\\d+$")) {
				song.display().setYear(Integer.parseInt(valueString));
			}
		}
		else
		if (identifier.equals("Genre"))   song.display().setGenre(valueString);			else
		if (identifier.equals("Comment")) song.comment().setNodeValue(valueString);
	}

}
