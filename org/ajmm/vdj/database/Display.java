package org.ajmm.vdj.database;

import org.ajmm.vdj.StringUtil;
import org.xml.sax.Attributes;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.12
 */
public class Display
{
	public static final String ELEMENT_NAME = "Display";
	public static final String ATTRIB_AUTHOR = "Author";
	public static final String ATTRIB_TITLE = "Title";
	public static final String ATTRIB_GENRE = "Genre";
	public static final String ATTRIB_ALBUM = "Album";
	public static final String ATTRIB_COMPOSER = "Composer";
	public static final String ATTRIB_YEAR = "Year";
	public static final String ATTRIB_COLOR = "Color";
	public static final String ATTRIB_COVER = "Cover";
	public static final String ATTRIB_TAG = "Tag";	
	public static final int VALUE_COVER_LOCALFILE = 1025;
	public static final int VALUE_COVER_FROMTAG = 2049;

	private String author;
	private String title;
	private String genre;
	private String album;
	private String composer;
	private int year = -1;
	private int color = -1;
	private int cover = -1;
	private int tag = -1;
		
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		if (year == -1 || year > 1900) this.year = year;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		if (color > -2) this.color = color;
	}

	public int getCover() {
		return cover;
	}

	public void setCover(int cover) {
		if (cover > -2) this.cover = cover;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		if (tag > -2) this.tag = tag;
	}
	
	public void parse(Attributes atts)
	{
		if (atts == null || atts.getLength() == 0) return;
		
		String author = atts.getValue(ATTRIB_AUTHOR);
		String title = atts.getValue(ATTRIB_TITLE);
		String genre = atts.getValue(ATTRIB_GENRE);
		String album = atts.getValue(ATTRIB_ALBUM);
		String composer = atts.getValue(ATTRIB_COMPOSER);
		int year = StringUtil.parseInt(atts.getValue(ATTRIB_YEAR));
		int color = StringUtil.parseInt(atts.getValue(ATTRIB_COLOR));
		int cover = StringUtil.parseInt(atts.getValue(ATTRIB_COVER));
		int tag = StringUtil.parseInt(atts.getValue(ATTRIB_TAG));
		
		if (author != null && author.length() > 0) this.author = author;
		if (title != null && title.length() > 0) this.title = title;
		if (genre != null && genre.length() > 0) this.genre = genre;
		if (album != null && album.length() > 0) this.album = album;
		if (composer != null && composer.length() > 0) this.composer = composer;
		if (year > 1900) this.year = year;
		if (color > -1) this.color = color;
		if (cover > -1) this.cover = cover;
		if (tag > -1) this.tag = tag;
		
	}

}

