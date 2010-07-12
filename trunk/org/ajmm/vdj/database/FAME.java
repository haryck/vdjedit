package org.ajmm.vdj.database;

import org.ajmm.vdj.StringUtil;
import org.xml.sax.Attributes;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.12
 */
public class FAME
{
	public static final String ELEMENT_NAME = "FAME";
	public static final String ATTRIB_IS_SCANNED = "IsScanned";
	public static final String ATTRIB_VOLUME = "Volume";
	public static final String ATTRIB_KEY = "Key";

	private int isScanned = -1;
	private int volume = -1;
	private String key;
	
	public int getIsScanned() {
		return isScanned;
	}

	public void setIsScanned(int isScanned) {
		if (isScanned > -2) this.isScanned = isScanned;
	}

	public double getVolume() {
		return FAME.fromVdjVolume(getVdjVolume());
	}

	public void setVolume(double volume) {
		if (volume > -2) this.volume = FAME.toVdjVolume(volume);
	}
	
	public int getVdjVolume() {
		return volume;
	}

	public void setVdjVolume(int volume) {
		if (volume > -2) this.volume = volume;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public static double fromVdjVolume(int volume) {
		return (volume > -1) ? 20*Math.log10(volume/8231.0) : Double.NaN;
	}

	public static int toVdjVolume(double volume) {
		return (volume > -1) ? (int)Math.round(Math.pow(10, volume/20)) : -1;
	}
	
	public void parse(Attributes atts)
	{
		if (atts == null || atts.getLength() == 0) return;
		
		int isScanned = StringUtil.parseInt(atts.getValue(FAME.ATTRIB_IS_SCANNED));
		int volume = StringUtil.parseInt(atts.getValue(FAME.ATTRIB_VOLUME));
		String key = atts.getValue(FAME.ATTRIB_KEY);
		
		if (isScanned > -1) setIsScanned(isScanned);
		if (volume > -1) setVdjVolume(volume);
		if (key != null && key.length() > 0) setKey(key);
	}

}
