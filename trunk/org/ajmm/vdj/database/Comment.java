package org.ajmm.vdj.database;

import org.ajmm.framework.xml.XmlNode;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.01
 */
public class Comment extends XmlNode
{
	public Comment() {
		super("Comment", 0);
	}

	public String get() {
		return super.getNodeValue();
	}

	public String set(String comment) {
		return super.setNodeValue(comment);
	}

}
