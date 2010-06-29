package org.ajmm.vdj.database;

import org.ajmm.framework.xml.XmlNode;

/**
 * 
 * 
 * @author	Andrew Mackrodt
 * @version	2010.06.16
 */
public class Display extends XmlNode
{
	public static final int VALUE_COVER_LOCALFILE	= 1025;
	public static final int VALUE_COVER_FROMTAG		= 2049;
	
	public Display() {
		super("Display", 0);
	}
	
	public String getAuthor() {
		return getAttribute("Author");
	}

	public String setAuthor(String author) {
		return setAttribute("Author", author);
	}

	public String getTitle() {
		return getAttribute("Title");
	}

	public String setTitle(String title) {
		return setAttribute("Title", title);
	}

	public String getGenre() {
		return getAttribute("Genre");
	}

	public String setGenre(String genre) {
		return setAttribute("Genre", genre);
	}

	public String getAlbum() {
		return getAttribute("Album");
	}

	public String setAlbum(String album) {
		return setAttribute("Album", album);
	}

	public int getYear() {
		return getAttributeAsInteger("Year");
	}

	public int setYear(int year) {
		return setAttribute("Year", year);
	}

	public String getColor() {
		return getAttribute("Color");
	}

	public String setColor(String color) {
		return setAttribute("Color", color);
	}

	public int getCover() {
		return getAttributeAsInteger("Cover");
	}

	public int setCover(short cover) {
		return setAttribute("Cover", cover);
	}

	public int getTag() {
		return getAttributeAsInteger("Tag");
	}

	public int setTag(int tag) {
		return setAttribute("Tag", tag);
	}

}

