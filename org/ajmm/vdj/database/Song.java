package org.ajmm.vdj.database;

import org.ajmm.framework.xml.XmlNode;

/**
 * 
 * 
 * @author	Andrew Mackrodt
 * @version	2010.06.19
 */
public class Song extends XmlNode implements Comparable<Song>
{
	public static final int VALUE_FLAG_HIDDEN		= 1;
	public static final int VALUE_FLAG_SOUNDCARD	= 4;
	public static final int VALUE_FLAG_KARAOKE		= 32;
	public static final int VALUE_FLAG_VIDEO		= 64;
	
	private Display display;
	private Infos infos;
	private Comment comment;
	private BPM bpm;
	private FAME fame;
	private Automix automix;
	private Link link;
	
	public Song() {
		super("Song", 7);
	}

	public String getFilePath() {
		return getAttribute("FilePath");
	}

	public String setFilePath(String filePath) {
		return setAttribute("FilePath", filePath);
	}

	public int getFileSize() {
		return getAttributeAsInteger("FileSize");
	}

	public int setFileSize(int fileSize) {
		return setAttribute("FileSize", fileSize);
	}

	public int getFlag() {
		return getAttributeAsInteger("Flag");
	}
	
	public int setFlag(int flag) {
		return setAttribute("Flag", flag);
	}
	
	@Override
	public boolean addChild(XmlNode element)
	{
		boolean added = super.addChild(element);
		if (added)
		{
			if (element instanceof Display)	display	= (Display)element;	else
			if (element instanceof Infos)	infos	= (Infos)element;	else
			if (element instanceof Comment)	comment	= (Comment)element;	else
			if (element instanceof BPM)		bpm		= (BPM)element;		else
			if (element instanceof FAME)	fame	= (FAME)element;	else
			if (element instanceof Automix)	automix	= (Automix)element;	else
			if (element instanceof Link)	link	= (Link)element;
		}
		
		return added;
	}
	
	public Display display()
	{
		if (display == null) addChild(new Display());
		return display;
	}

	public Infos infos()
	{
		if (infos == null) addChild(new Infos());
		return infos;
	}

	public Comment comment()
	{
		if (comment == null) addChild(new Comment());
		return comment;
	}

	public BPM bpm()
	{
		if (bpm == null) addChild(new BPM());
		return bpm;
	}

	public FAME fame()
	{
		if (fame == null) addChild(new FAME());
		return fame;
	}

	public Automix automix()
	{
		if (automix == null) addChild(new Automix());
		return automix;
	}

	public Link link()
	{
		if (link == null) addChild(new Link());
		return link;
	}

	public int compareTo(Song s) {
		return getFilePath().compareToIgnoreCase(s.getFilePath());
	}
	
}
