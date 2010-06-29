package org.ajmm.vdj.database;

import org.ajmm.framework.xml.XmlNode;

/**
 * 
 * 
 * @author	Andrew Mackrodt
 * @version	2010.06.20
 */
public class BPM extends XmlNode
{
	public BPM() {
		super("BPM", 0);
	}

	public double getBpm() {
		return (int)Math.round(26460000.0/getInternalBpm())/10.0;
	}
	
	public int getInternalBpm() {
		return getAttributeAsInteger("Bpm");
	}

	public int setInternalBpm(int bpm) {
		return setAttribute("Bpm", bpm);
	}

	public int getPhase() {
		return getAttributeAsInteger("Phase");
	}

	public int setPhase(int phase) {
		return setAttribute("Phase", phase);
	}
	
}
