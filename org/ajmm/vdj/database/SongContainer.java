package org.ajmm.vdj.database;

import java.util.Set;

/**
*
*
* @author	Andrew Mackrodt
* @version	2010.07.12
*/
public interface SongContainer
{
	public Set<Song> getSongs();
	public boolean addSong(Song song);
	public boolean removeSong(Song song);
}
