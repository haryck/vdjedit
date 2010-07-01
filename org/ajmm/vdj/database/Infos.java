package org.ajmm.vdj.database;

import java.util.Calendar;
import java.util.Date;

import org.ajmm.framework.xml.XmlNode;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.01
 */
public class Infos extends XmlNode
{
	public Infos() {
		super("Infos", 0);
	}

	public double getSongLength() {
		return Infos.fromVdjSongLength(getVdjSongLength());
	}

	public double setSongLength(double songLength)
	{
		int vdjSongLength = Infos.toVdjSongLength(songLength);
		int before = setVdjSongLength(vdjSongLength);

		return Infos.fromVdjSongLength(before);
	}

	public int getVdjSongLength() {
		return getAttributeAsInteger("SongLength");
	}

	public int setVdjSongLength(int songLength) {
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

	public double getBpmTag() {
		return BPM.fromVdjBpm(getVdjBpmTag());
	}

	public double setBpmTag(double bpmTag)
	{
		int vdjBpmTag = BPM.toVdjBpm(bpmTag);
		int before = setVdjSongLength(vdjBpmTag);

		return BPM.fromVdjBpm(before);
	}

	public int getVdjBpmTag() {
		return getAttributeAsInteger("BpmTag");
	}

	public int setVdjBpmTag(int vdjBpmTag) {
		return setAttribute("BpmTag", vdjBpmTag);
	}

	public int getFaked() {
		return getAttributeAsInteger("Faked");
	}

	public int setFaked(int faked) {
		return setAttribute("Faked", faked);
	}

	public static double fromVdjSongLength(int vdjSongLength) {
		return vdjSongLength/44100.0;
	}

	public static int toVdjSongLength(double songLength) {
		return (int)Math.round(441000*songLength)/10;
	}

	public static Date fromVdjDate(int vdjDate)
	{
		// return null value if the value is not in a valid virtual dj format
		if (vdjDate < 101010000 || vdjDate > 2101012359) return null;

		String s = ""+vdjDate;

		String year  = (s.length() == 10) ? s.substring(0, 2) : ""+s.charAt(0);
		String month = s.substring(year.length(), year.length()+2);
		String date  = s.substring(year.length()+2, year.length()+4);
		String hour  = s.substring(year.length()+4, year.length()+6);
		String min   = s.substring(year.length()+6, year.length()+8);

		year = (year.length() == 2) ? "20"+year : "200"+year;

		Calendar calendar = Calendar.getInstance();
		calendar.set(
				Integer.parseInt(year), Integer.parseInt(month),
				Integer.parseInt(date), Integer.parseInt(hour),
				Integer.parseInt(min));

		return calendar.getTime();
	}

	public static int toVdjDate(Date date) {
		return -1;
	}

}
