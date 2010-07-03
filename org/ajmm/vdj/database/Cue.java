package org.ajmm.vdj.database;

import org.ajmm.framework.xml.XmlNode;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.03
 */
public class Cue extends XmlNode
{
	public Cue() {
		super("Cue", 0);
	}

	public int getNum() {
		return getAttributeAsInteger("Num");
	}

	public int setNum(int num) {
		return setAttribute("Num", num);
	}

	public String getTitle() {
		return getAttribute("Name");
	}

	public String setTitle(String title) {
		return setAttribute("Name", title);
	}

	public int getPos() {
		return getAttributeAsInteger("Pos");
	}

	public int setPos(int pos) {
		return setAttribute("Pos", pos);
	}

}
