package org.ajmm.vdj.database;

import java.util.Set;
import java.util.TreeSet;

import org.ajmm.vdj.database.exception.DuplicateSongException;

/**
 * 
 * 
 * @author	Andrew Mackrodt
 * @version	2010.06.11
 */
public class VirtualFolder extends AbstractFolder
{
	private final Set<Song> songs = new TreeSet<Song>();
	
	public VirtualFolder() {
		super("VirtualFolder", 16);
	}
	
	public Set<Song> getSongs() {
		return songs;
	}

	public boolean addSong(Song song) throws DuplicateSongException
	{
		if (songs.contains(song)) throw new DuplicateSongException();
		
		return songs.add(song);
	}
	
	public boolean removeSong(Song song) {
		return songs.remove(song);
	}
	
}
