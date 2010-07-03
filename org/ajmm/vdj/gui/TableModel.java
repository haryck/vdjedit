package org.ajmm.vdj.gui;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.ajmm.vdj.database.Infos;
import org.ajmm.vdj.database.Song;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.06.30
 */
public class TableModel extends DefaultTableModel
{
	private static final long serialVersionUID = -5497574911935034884L;
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MMM-yyyy HH:mm");

	protected void setDataVector(Vector<Vector<Song>> dataVector) {
		this.dataVector = dataVector;
	}

	public static String formatFileSize(Song song)
	{
		int fileSize = song.getFileSize();
		return (fileSize < 1) ? null : Math.round(fileSize / 10485.76)/100.0+" mb";
	}

	public static String formatSongLength(Song song)
	{
		double length = song.infos().getSongLength();
		int min = (int)(length/60.0);
		int sec = (int)Math.round(length-60*min);
		return min+":"+String.format("%02d", sec);
	}

	public static double formatBpm(Song song)
	{
		double bpm = song.bpm().getBpm();
		return Math.round(10*bpm)/10.0;
	}

	public static String formatFirstSeen(Song song) {
		return formatDateString(song.infos().getFirstSeen());
	}

	public static String formatFirstPlay(Song song) {
		return formatDateString(song.infos().getFirstPlay());
	}

	public static String formatLastPlay(Song song) {
		return formatDateString(song.infos().getLastPlay());
	}

	public static String formatFileType(Song song)
	{
		String name = song.getFilePath();
		int off = (name == null) ? null : name.lastIndexOf('.')+1;
		return (off > 0) ? name.substring(off) : null;
	}


	private static String formatDateString(int vdjDate)
	{
		Date date = Infos.fromVdjDate(vdjDate);
		return (date == null) ? null : SDF.format(date);
	}

	public static String formatHidden(Song song)
	{
		int flag = song.getFlag();
		boolean hidden = flag > 0 && ((flag & Song.VALUE_FLAG_HIDDEN) > 0);

		return (hidden) ? "true" : "false";
	}

	@SuppressWarnings("unchecked")
	public Song getSong(int index)
	{
		Vector<Song> vector = (Vector<Song>)dataVector.get(index);
		return vector.get(0);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getValueAt(int row, int column)
	{
		Song song = (Song)((Vector<Song>)dataVector.get(row)).get(0);
		Object value = null;

		String identifier = getColumnName(column);

		if (identifier.equals("Filename"))    value = song.getFilePath();			    else
		if (identifier.equals("Artist"))      value = song.display().getAuthor();	    else
		if (identifier.equals("Album"))       value = song.display().getAlbum();		else
		if (identifier.equals("Title"))       value = song.display().getTitle();		else
		if (identifier.equals("Year"))        value = song.display().getYear();			else
		if (identifier.equals("Genre"))       value = song.display().getGenre();		else
		if (identifier.equals("Comment"))     value = song.comment().get();				else
		if (identifier.equals("Length"))      value = formatSongLength(song);			else
		if (identifier.equals("BPM"))	      value = formatBpm(song);					else
		if (identifier.equals("Key")) 	      value = song.fame().getKey();				else
		if (identifier.equals("Bitrate"))     value = song.infos().getBitrate();		else
		if (identifier.equals("Play Count"))  value = song.infos().getPlayCount();		else
		if (identifier.equals("First Seen"))  value = formatFirstSeen(song);			else
		if (identifier.equals("First Play"))  value = formatFirstPlay(song);			else
		if (identifier.equals("Last Play"))   value = formatLastPlay(song);				else
		if (identifier.equals("Cues"))		  value = song.cue().size();				else
		if (identifier.equals("Filetype"))    value = formatFileType(song);				else
		if (identifier.equals("Filesize"))    value = formatFileSize(song);				else
		if (identifier.equals("LinkedVideo")) value = song.link().getVideo();			else
		if (identifier.equals("Composer"))    value = song.display().getComposer();		else
		if (identifier.equals("Hidden"))	  value = formatHidden(song);

		// do not display negative numbers by returning value as null
		if (value instanceof Integer && (Integer)value < 0) value = null;	else
		if (value instanceof Double  && (Double) value < 0) value = null;

		return value;
	}

}
