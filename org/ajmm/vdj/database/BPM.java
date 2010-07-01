package org.ajmm.vdj.database;

import org.ajmm.framework.xml.XmlNode;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.01
 */
public class BPM extends XmlNode
{
	public BPM() {
		super("BPM", 0);
	}

	public double getBpm() {
		return BPM.fromVdjBpm(getVdjBpm());
	}

	public double setBpm(double bpm)
	{
		int vdjBpm = BPM.toVdjBpm(bpm);
		int before = setVdjBpm(vdjBpm);

		return BPM.fromVdjBpm(before);
	}

	public int getVdjBpm() {
		return getAttributeAsInteger("Bpm");
	}

	public int setVdjBpm(int bpm) {
		return setAttribute("Bpm", bpm);
	}

	public int getPhase() {
		return getAttributeAsInteger("Phase");
	}

	public int setPhase(int phase) {
		return setAttribute("Phase", phase);
	}

	public static double fromVdjBpm(int vdjBpm) {
		return 2646000.0/vdjBpm;
	}

	public static int toVdjBpm(double bpm) {
		return (int)Math.round(26460000.0/bpm)/10;
	}

}
