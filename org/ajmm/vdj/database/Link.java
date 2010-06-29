package org.ajmm.vdj.database;

import org.ajmm.framework.xml.XmlNode;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.06.16
 */
public class Link extends XmlNode
{
	public Link() {
		super("Link", 0);
	}

	public String getWmp() {
		return getAttribute("Wmp");
	}

	public String setWmp(String wmp) {
		return setAttribute("Wmp", wmp);
	}

	public String getVideo() {
		return getAttribute("Video");
	}

	public String setVideo(String video) {
		return setAttribute("Video", video);
	}

}
