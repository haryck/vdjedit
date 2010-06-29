package org.ajmm.vdj.database;

import org.ajmm.framework.xml.XmlNode;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.06.16
 */
public class FAME extends XmlNode
{
	public FAME() {
		super("FAME", 0);
	}

	public int getIsScanned() {
		return getAttributeAsInteger("IsScanned");
	}

	public int setIsScanned(int isScanned) {
		return setAttribute("IsScanned", isScanned);
	}

	public int getVolume() {
		return getAttributeAsInteger("Volume");
	}

	public int setVolume(int volume) {
		return setAttribute("Volume", volume);
	}

	public String getKey() {
		return getAttribute("Key");
	}

	public String setKey(String key) {
		return setAttribute("Key", key);
	}

}
