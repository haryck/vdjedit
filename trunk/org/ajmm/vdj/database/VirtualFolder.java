package org.ajmm.vdj.database;

import java.util.Set;
import java.util.TreeSet;

import org.ajmm.vdj.StringUtil;
import org.xml.sax.Attributes;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.12
 */
public class VirtualFolder extends AbstractFolder implements SongContainer
{
	public static final String ELEMENT_NAME = "VirtualFolder";
	
	private final Set<Song> songs = new TreeSet<Song>();

	public VirtualFolder(String name) {
		super(name);
	}

	public Set<Song> getSongs() {
		return songs;
	}

	public boolean addSong(Song song) {
		return songs.add(song);
	}

	public boolean removeSong(Song song) {
		return songs.remove(song);
	}
	
	public static void parse(Attributes atts, Database database)
	{
		if (atts == null || atts.getLength() == 0 || database == null) return;
		
		String name = atts.getValue(ATTRIB_NAME);
		if (name == null || name.length() == 0)
			name = "No Name";
		String sOrder = atts.getValue(ATTRIB_ORDER);
		int order = StringUtil.parseInt(sOrder);
		
		VirtualFolder folder = new VirtualFolder(name);
		database.addFolder(folder);
		if (order > 0) folder.setOrder(order);
	}

}
