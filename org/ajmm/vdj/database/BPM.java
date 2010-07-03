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
	private static final int MIN_VDJ_BPM = 52920;	// 50.0 bpm
	private static final int MAX_VDJ_BPM = 8820;	// 300.0 bpm

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

	public int setVdjBpm(int bpm)
	{
		if (MAX_VDJ_BPM <= bpm && bpm <= MIN_VDJ_BPM)
			return setAttribute("Bpm", bpm);
		return getVdjBpm();
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
