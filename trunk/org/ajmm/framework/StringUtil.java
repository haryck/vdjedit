package org.ajmm.framework;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.06.11
 */
public abstract class StringUtil
{
	public static String clean(String string)
	{
		if (string == null || string.length() == 0 ||
				((string = string.trim().replaceAll("  +", " "))).length() == 0)
			return "";

		return string;
	}
}
