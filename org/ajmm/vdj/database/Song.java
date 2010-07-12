package org.ajmm.vdj.database;

import java.util.LinkedList;
import java.util.List;

import org.ajmm.vdj.StringUtil;
import org.xml.sax.Attributes;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.12
 */
public class Song implements Comparable<Song>
{
	public static final String ELEMENT_NAME = "Song";
	public static final String ATTRIB_FILE_PATH = "FilePath";
	public static final String ATTRIB_FILE_SIZE = "FileSize";
	public static final String ATTRIB_FLAG = "Flag";
	public static final int VALUE_FLAG_HIDDEN		= 1;
	public static final int VALUE_FLAG_SOUNDCARD	= 4;
	public static final int VALUE_FLAG_KARAOKE		= 32;
	public static final int VALUE_FLAG_VIDEO		= 64;
	
	private static final char[] ILLEGAL_CHARACTERS = new char[] {
		'?', '<', '>', ':', '*', '|', '"'
	};

	private Display display;
	private Infos infos;
	private Comment comment;
	private BPM bpm;
	private FAME fame;
	private Automix automix;
	private Link link;
	private List<Cue> cues;
	private String filePath;
	private int fileSize = -1;
	private int flag = -1;

	public Song(String filePath, int fileSize) {
		setFilePath(filePath);
		setFileSize(fileSize);
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		if (filePath != null && isValidFilePath(filePath))
			this.filePath = filePath;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		if (fileSize > -1) this.fileSize = fileSize;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		if (flag > -2) this.flag = flag;
	}

	public Display display() {
		return (display == null) ? display = new Display() : display;
	}

	public Infos infos() {
		return (infos == null) ? infos = new Infos() : infos;
	}

	public Comment comment() {
		return (comment == null) ? comment = new Comment() : comment;
	}

	public BPM bpm() {
		return (bpm == null) ? bpm = new BPM() : bpm;
	}

	public FAME fame() {
		return (fame == null) ? fame = new FAME() : fame;
	}

	public Automix automix() {
		return (automix == null) ? automix = new Automix() : automix;
	}

	public List<Cue> cues() {
		return (cues == null) ? cues = new LinkedList<Cue>() : cues;
	}

	public Link link() {
		return (link == null) ? link = new Link() : link;
	}

	public int compareTo(Song s) {
		return (""+filePath).compareToIgnoreCase(""+s.filePath);
	}
	
	public static Song parse(Attributes atts, SongContainer sc)
	{
		if (atts == null || atts.getLength() == 0 || sc == null) return null;
		
		String filePath = atts.getValue(Song.ATTRIB_FILE_PATH);
		int fileSize = StringUtil.parseInt(atts.getValue(Song.ATTRIB_FILE_SIZE));
		if (filePath != null && isValidFilePath(filePath) && fileSize > -1)
		{
			Song song = new Song(filePath, fileSize);
			sc.addSong(song);
			
			int flag = StringUtil.parseInt(atts.getValue(Song.ATTRIB_FLAG));
			if (flag > -1) song.setFlag(flag);
			
			return song;
		}
		
		return null;
	}
	
	private static boolean isValidFilePath(String string)
	{
		if (string == null || string.length() < 4) return false;
		for (char c1 : string.substring(3).toCharArray()) {
			for (char c2 : ILLEGAL_CHARACTERS) {
				if (c1 == c2) return false;
			}
		}
		return true;
	}

}
