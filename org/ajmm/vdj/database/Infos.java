package org.ajmm.vdj.database;

import org.ajmm.framework.xml.XmlNode;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.06.16
 */
public class Infos extends XmlNode
{
	public Infos() {
		super("Infos", 0);
	}

	public int getSongLength() {
		return (int)Math.round(getInternalSongLength()/44100.0);
	}

	public int getInternalSongLength() {
		return getAttributeAsInteger("SongLength");
	}

	public int setInternalSongLength(int songLength) {
		return setAttribute("SongLength", songLength);
	}

	public int getFirstSeen() {
		return getAttributeAsInteger("FirstSeen");
	}

	public int setFirstSeen(int firstSeen) {
		return setAttribute("FirstSeen", firstSeen);
	}

	public int getFirstPlay() {
		return getAttributeAsInteger("FirstPlay");
	}

	public int setFirstPlay(int firstPlay) {
		return setAttribute("FirstPlay", firstPlay);
	}

	public int getLastPlay() {
		return getAttributeAsInteger("LastPlay");
	}

	public int setLastPlay(int lastPlay) {
		return setAttribute("LastPlay", lastPlay);
	}

	public int getPlayCount() {
		return getAttributeAsInteger("PlayCount");
	}

	public int setPlayCount(int playCount) {
		return setAttribute("PlayCount", playCount);
	}

	public int getBitrate() {
		return getAttributeAsInteger("Bitrate");
	}

	public int setBitrate(int bitrate) {
		return setAttribute("Bitrate", bitrate);
	}

	public int getFaked() {
		return getAttributeAsInteger("Faked");
	}

	public int setFaked(int faked) {
		return setAttribute("Faked", faked);
	}

}
