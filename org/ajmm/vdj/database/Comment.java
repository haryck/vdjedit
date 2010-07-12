package org.ajmm.vdj.database;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.03
 */
public class Comment
{
	public static final String ELEMENT_NAME = "Comment";
	
	private String value;
	
	public String get() {
		return value;
	}

	public void set(String value) {
		this.value = value;
	}

}
