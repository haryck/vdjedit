package org.ajmm.vdj.database;

import org.ajmm.vdj.StringUtil;
import org.xml.sax.Attributes;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.12
 */
public class Cue
{
	public static final String ELEMENT_NAME = "Cue";
	public static final String ATTRIB_NUM = "Num";
	public static final String ATTRIB_NAME = "Name";
	public static final String ATTRIB_POS = "Pos";

	private int num = 1;
	private String name = "No Name";
	private int pos = 0;
	
	public Cue(int pos) {
		setPos(pos);
	}
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		if (num > 0) this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		if (pos > -1) this.pos = pos;
	}
	
	public static void parse(Attributes atts, Song song)
	{
		if (atts == null || atts.getLength() == 0 || song == null) return;
		
		int pos = StringUtil.parseInt(atts.getValue("Pos"));
		if (pos > -1)
		{
			String name = atts.getValue("Name");
			int num = StringUtil.parseInt(atts.getValue("Num"));
			
			Cue cue = new Cue(pos);
			song.cues().add(cue);
			
			if (name != null && name.length() > 0) cue.setName(name);
			if (num > 0) cue.setNum(num);
		}
	}

}
