package org.ajmm.vdj.database;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.06.14
 */
public class FilterFolder extends AbstractFolder
{
	public FilterFolder() {
		super("FilterFolder", 0);
	}

	public String getFilter() {
		return getAttribute("Filter");
	}

	public String setFilter(String filter) {
		return setAttribute("Filter", filter);
	}

}
