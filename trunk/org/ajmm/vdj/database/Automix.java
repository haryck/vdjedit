package org.ajmm.vdj.database;

import org.ajmm.vdj.StringUtil;
import org.xml.sax.Attributes;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.12
 */
public class Automix
{
	public static final String ELEMENT_NAME = "Automix";
	public static final String ATTRIB_MIXTYPE = "MixType";
	public static final String ATTRIB_CUT_START = "CutStart";
	public static final String ATTRIB_CUT_END = "CutEnd";
	public static final String ATTRIB_FADE_START = "FadeStart";
	public static final String ATTRIB_FADE_END = "FadeEnd";
	public static final String ATTRIB_REAL_START = "RealStart";
	public static final String ATTRIB_REAL_END = "RealEnd";
	public static final int VALUE_MIX_CUT = 1;
	public static final int VALUE_MIX_TEMPO = 2;

	private int mixType = -1;
	private int cutStart = -1;
	private int cutEnd = -1;
	private int fadeStart = -1;
	private int fadeEnd = -1;
	private int realStart = -1;
	private int realEnd = -1;
	
	public int getMixType() {
		return mixType;
	}

	public void setMixType(int mixType) {
		if (mixType > -1 && mixType <= 3) this.mixType = mixType;
	}

	public int getCutStart() {
		return cutStart;
	}

	public void setCutStart(int cutStart) {
		if (cutStart > -2) this.cutStart = cutStart;
	}

	public int getCutEnd() {
		return cutEnd;
	}

	public void setCutEnd(int cutEnd) {
		if (cutEnd > -2) this.cutEnd = cutEnd;
	}

	public int getFadeStart() {
		return fadeStart;
	}

	public void setFadeStart(int fadeStart) {
		if (fadeStart > -2) this.fadeStart = fadeStart;
	}

	public int getFadeEnd() {
		return fadeEnd;
	}

	public void setFadeEnd(int fadeEnd) {
		if (fadeEnd > -2) this.fadeEnd = fadeEnd;
	}

	public int getRealStart() {
		return realStart;
	}

	public void setRealStart(int realStart) {
		if (realStart > -2) this.realStart = realStart;
	}

	public int getRealEnd() {
		return realEnd;
	}

	public void setRealEnd(int realEnd) {
		if (realEnd > -2) this.realEnd = realEnd;
	}
	
	public void parse(Attributes atts)
	{
		if (atts == null || atts.getLength() == 0) return;
		
		int mixType = StringUtil.parseInt(atts.getValue(Automix.ATTRIB_MIXTYPE));
		int cutStart = StringUtil.parseInt(atts.getValue(Automix.ATTRIB_CUT_START));
		int cutEnd = StringUtil.parseInt(atts.getValue(Automix.ATTRIB_CUT_END));
		int fadeStart = StringUtil.parseInt(atts.getValue(Automix.ATTRIB_FADE_START));
		int fadeEnd = StringUtil.parseInt(atts.getValue(Automix.ATTRIB_FADE_END));
		int realStart = StringUtil.parseInt(atts.getValue(Automix.ATTRIB_REAL_START));
		int realEnd = StringUtil.parseInt(atts.getValue(Automix.ATTRIB_REAL_END));
		
		if (mixType > -1) this.mixType = mixType;
		if (cutStart > -1) this.cutStart = cutStart;
		if (cutEnd > -1) this.cutEnd = cutEnd;
		if (fadeStart > -1) this.fadeStart = fadeStart;
		if (fadeEnd > -1) this.fadeEnd = fadeEnd;
		if (realStart > -1) this.realStart = realStart;
		if (realEnd > -1) this.realEnd = realEnd;

	}

}
