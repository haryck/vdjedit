package org.ajmm.vdj.database;

import org.ajmm.vdj.StringUtil;
import org.xml.sax.Attributes;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.12
 */
public class BPM
{
	public static final String ELEMENT_NAME = "BPM";
	public static final String ATTRIB_BPM = "Bpm";
	public static final String ATTRIB_PHASE = "Phase";
	private static final int MIN_BPM = 50;
	private static final int MAX_BPM = 300;
	private static final int MIN_VDJ_BPM = 52920;	// 50.0 bpm
	private static final int MAX_VDJ_BPM = 8820;	// 300.0 bpm

	private int bpm = -1;
	private int phase = -1;
	
	public double getBpm() {
		return BPM.fromVdjBpm(getVdjBpm());
	}

	public void setBpm(double bpm) {
		if (bpm == -1 || (
				MIN_BPM <= bpm && bpm <= MAX_BPM))
			this.bpm = toVdjBpm(bpm);
	}

	public int getVdjBpm() {
		return bpm;
	}

	public void setVdjBpm(int bpm) {
		if (bpm == -1 || (
				MAX_VDJ_BPM <= bpm && bpm <= MIN_VDJ_BPM))
			this.bpm = bpm;
	}

	public int getPhase() {
		return phase;
	}

	public void setPhase(int phase) {
		if (phase > -2) this.phase = phase;
	}

	public static double fromVdjBpm(int bpm) {
		return (bpm > -1) ? 2646000.0/bpm : -1;
	}

	public static int toVdjBpm(double bpm) {
		return (bpm > -1) ? (int)Math.round(26460000.0/bpm)/10 : -1;
	}
	
	public void parse(Attributes atts)
	{
		if (atts == null || atts.getLength() == 0) return;
		
		int bpm = StringUtil.parseInt(atts.getValue(BPM.ATTRIB_BPM));
		int phase = StringUtil.parseInt(atts.getValue(BPM.ATTRIB_PHASE));
		
		if (MAX_VDJ_BPM <= bpm && bpm <= MIN_VDJ_BPM) this.bpm = bpm;
		if (phase > -1) this.phase = phase;
	}

}
