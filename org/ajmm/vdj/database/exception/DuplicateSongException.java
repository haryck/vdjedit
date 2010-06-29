package org.ajmm.vdj.database.exception;

/**
 * 
 * 
 * @author	Andrew Mackrodt
 * @version	2010.06.11
 */
public class DuplicateSongException extends Exception
{
	private static final long serialVersionUID = 3249337101094138156L;

	public DuplicateSongException(String message) {
		super(message);
	}
	
	public DuplicateSongException() {

	}
}
