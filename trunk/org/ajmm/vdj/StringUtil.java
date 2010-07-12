package org.ajmm.vdj;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.12
 */
public abstract class StringUtil
{	
	public static int parseInt(String string) {
		if (string != null && string.matches("^\\d+$"))
			return Integer.parseInt(string);
		return -1;
	}
	
}
