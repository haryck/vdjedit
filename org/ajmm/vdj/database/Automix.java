package org.ajmm.vdj.database;

import org.ajmm.framework.xml.XmlNode;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.06.16
 */
public class Automix extends XmlNode
{
	public Automix() {
		super("Automix", 0);
	}

	public int getMixType() {
		return getAttributeAsInteger("MixType");
	}

	public int setMixType(int mixType) {
		return setAttribute("MixType", mixType);
	}

	public int getCutStart() {
		return getAttributeAsInteger("CutStart");
	}

	public int setCutStart(int cutStart) {
		return setAttribute("CutStart", cutStart);
	}

	public int getCutEnd() {
		return getAttributeAsInteger("CutEnd");
	}

	public int setCutEnd(int cutEnd) {
		return setAttribute("CutEnd", cutEnd);
	}

	public int getFadeStart() {
		return getAttributeAsInteger("FadeStart");
	}

	public int setFadeStart(int fadeStart) {
		return setAttribute("FadeStart", fadeStart);
	}

	public int getFadeEnd() {
		return getAttributeAsInteger("FadeEnd");
	}

	public int setFadeEnd(int fadeEnd) {
		return setAttribute("FadeEnd", fadeEnd);
	}

	public int getRealStart() {
		return getAttributeAsInteger("RealStart");
	}

	public int setRealStart(int realStart) {
		return setAttribute("RealStart", realStart);
	}

	public int getRealEnd() {
		return getAttributeAsInteger("RealEnd");
	}

	public int setRealEnd(int realEnd) {
		return setAttribute("RealEnd", realEnd);
	}

}
