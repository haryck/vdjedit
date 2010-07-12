package org.ajmm.vdj.database;

import org.xml.sax.Attributes;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.12
 */
public class Link
{
	public static final String ELEMENT_NAME = "Link";
	public static final String ATTRIB_WMP = "Wmp";
	public static final String ATTRIB_VIDEO = "Video";

	private String wmp;
	private String video;
	
	public String getWmp() {
		return wmp;
	}

	public void setWmp(String wmp) {
		this.wmp = wmp;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}
	
	public void parse(Attributes atts)
	{
		if (atts == null || atts.getLength() == 0) return;
		
		String wmp = atts.getValue(ATTRIB_WMP);
		String video = atts.getValue(ATTRIB_VIDEO);

		if (wmp != null) setWmp(wmp);
		if (video != null) setVideo(video);
	}

}
