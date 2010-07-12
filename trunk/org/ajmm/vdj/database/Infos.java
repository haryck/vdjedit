package org.ajmm.vdj.database;

import java.util.Calendar;

import org.ajmm.vdj.StringUtil;
import org.xml.sax.Attributes;

/**
 *
 *
 * @author Andrew Mackrodt
 * @version 2010.07.12
 */
public class Infos
{
	public static final String ELEMENT_NAME = "Infos";
	public static final String ATTRIB_SONG_LENGTH = "SongLength";
	public static final String ATTRIB_FIRST_SEEN = "FirstSeen";
	public static final String ATTRIB_FIRST_PLAY = "FirstPlay";
	public static final String ATTRIB_LAST_PLAY = "LastPlay";
	public static final String ATTRIB_PLAY_COUNT = "PlayCount";
	public static final String ATTRIB_BITRATE = "Bitrate";
	public static final String ATTRIB_BPM_TAG = "BpmTag";
	public static final String ATTRIB_FAKED = "Faked";

	private int songLength = -1;
	private int firstSeen = -1;
	private int firstPlay = -1;
	private int lastPlay = -1;
	private int playCount = -1;
	private int bitrate = -1;
	private int bpmTag = -1;
	private int faked = -1;

	public double getSongLength() {
		return Infos.fromVdjSongLength(getVdjSongLength());
	}

	public void setSongLength(double songLength) {
		if (songLength > -2)
			this.songLength = Infos.toVdjSongLength(songLength);
	}

	public int getVdjSongLength() {
		return songLength;
	}

	public void setVdjSongLength(int songLength) {
		if (songLength > -2) this.songLength = songLength;
	}

	public int getFirstSeen() {
		return firstSeen;
	}

	public void setFirstSeen(int firstSeen) {
		if (firstSeen > -2) this.firstSeen = firstSeen;
	}

	public int getFirstPlay() {
		return firstPlay;
	}

	public void setFirstPlay(int firstPlay) {
		if (firstPlay > -2) this.firstPlay = firstPlay;
	}

	public int getLastPlay() {
		return lastPlay;
	}

	public void setLastPlay(int lastPlay) {
		if (lastPlay > -2) this.lastPlay = lastPlay;
	}

	public int getPlayCount() {
		return playCount;
	}

	public void setPlayCount(int playCount) {
		if (playCount > -2) this.playCount = playCount;
	}

	public int getBitrate() {
		return bitrate;
	}

	public void setBitrate(int bitrate) {
		if (bitrate > -2) this.bitrate = bitrate;
	}

	public double getBpmTag() {
		return BPM.fromVdjBpm(getVdjBpmTag());
	}

	public void setBpmTag(double bpmTag) {
		if (bpmTag > -2) this.bpmTag = BPM.toVdjBpm(bpmTag);
	}

	public int getVdjBpmTag() {
		return bpmTag;
	}

	public void setVdjBpmTag(int bpmTag) {
		if (bpmTag > -2) this.bpmTag = bpmTag;
	}

	public int getFaked() {
		return faked;
	}

	public void setFaked(int faked) {
		if (faked > -2) this.faked = faked;
	}

	public static double fromVdjSongLength(int songLength) {
		return (songLength > -1) ? songLength/44100.0 : -1;
	}

	public static int toVdjSongLength(double songLength) {
		return (songLength > -1) ? (int)Math.round(441000*songLength)/10 : -1;
	}

	public static Calendar fromVdjDate(int date)
	{
		// return null value if the value is not in a valid virtual dj format
		if (date < 101010000 || date > 2101012359) return null;

		String dateString = String.valueOf(date);
		int offset = (dateString.length() == 10) ? 2 : 1;
		
		String sYear = dateString.substring(0, offset);
		String sMonth = dateString.substring(sYear.length(), sYear.length() + 2);
		String sDate = dateString.substring(sYear.length() + 2, sYear.length() + 4);
		String sHour = dateString.substring(sYear.length() + 4, sYear.length() + 6);
		String sMin = dateString.substring(sYear.length() + 6, sYear.length() + 8);
		sYear = (sYear.length() == 2) ? "20" + sYear : "200" + sYear;
		
		int year = Integer.parseInt(sYear);
		int month = Integer.parseInt(sMonth)-1;
		date = Integer.parseInt(sDate);
		int hour = Integer.parseInt(sHour);
		int min = Integer.parseInt(sMin);

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, date, hour, min);

		return calendar;
	}

	public static int toVdjDate(Calendar calendar)
	{
		if (calendar == null) throw new NullPointerException();	
		
		int year = calendar.get(Calendar.YEAR);
		if (year < 2000 || year > 2099) {
			throw new IllegalArgumentException(
					"calendar must be greater than 1999-12-31 23:59:59 " +
					"and less than 2022-01-01 00:00:00");
		}

		int off = (year < 2010) ? 3 : 2;
		String date =
				String.valueOf(year).substring(off, 4) +		
				String.valueOf(calendar.get(Calendar.MONTH)+1) +
				String.valueOf(calendar.get(Calendar.DATE)) +
				String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) +
				String.valueOf(calendar.get(Calendar.MINUTE));
		
		return Integer.parseInt(date);
	}
	
	public void parse(Attributes atts)
	{
		if (atts == null || atts.getLength() == 0) return;
		
		int songLength = StringUtil.parseInt(atts.getValue(Infos.ATTRIB_SONG_LENGTH));
		int firstSeen = StringUtil.parseInt(atts.getValue(Infos.ATTRIB_FIRST_SEEN));
		int firstPlay = StringUtil.parseInt(atts.getValue(Infos.ATTRIB_FIRST_PLAY));
		int lastPlay = StringUtil.parseInt(atts.getValue(Infos.ATTRIB_LAST_PLAY));
		int playCount = StringUtil.parseInt(atts.getValue(Infos.ATTRIB_PLAY_COUNT));
		int bitrate = StringUtil.parseInt(atts.getValue(Infos.ATTRIB_BITRATE));
		int bpmTag = StringUtil.parseInt(atts.getValue(Infos.ATTRIB_BPM_TAG));
		int faked = StringUtil.parseInt(atts.getValue(Infos.ATTRIB_FAKED));
		
		if (songLength > -1) this.songLength = songLength;
		if (firstSeen > -1) this.firstSeen = firstSeen;
		if (firstPlay > -1) this.firstPlay = firstPlay;
		if (lastPlay > -1) this.lastPlay = lastPlay;
		if (playCount > -1) this.playCount = playCount;
		if (bitrate > -1) this.bitrate = bitrate;
		if (bpmTag > -1) this.bpmTag = bpmTag;
		if (faked > -1) this.faked = faked;
	}

}
