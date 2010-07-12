package org.ajmm.vdj.database;

import org.ajmm.vdj.StringUtil;
import org.xml.sax.Attributes;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.12
 */
public class FilterFolder extends AbstractFolder
{
	public static final String ELEMENT_NAME = "FilterFolder";
	public static final String ATTRIB_FILTER = "Filter";
	
	private String filter;
	
	public FilterFolder(String name, String filter) {
		super(name); setFilter(filter);
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		if (filter == null || filter.length() == 0) return;
		this.filter = filter;
	}
	
	public static void parse(Attributes atts, Database database)
	{
		if (atts == null || atts.getLength() == 0 || database == null) return;
		
		String folderPath = atts.getValue(ATTRIB_FILTER);
		if (folderPath != null)
		{
			String name = atts.getValue(ATTRIB_NAME);
			if (name == null || name.length() == 0)
				name = "No Name";
			String sOrder = atts.getValue(ATTRIB_ORDER);
			int order = StringUtil.parseInt(sOrder);

			FilterFolder folder = new FilterFolder(name, folderPath);
			database.addFolder(folder);
			if (order > 0) folder.setOrder(order);
		}
	}

}
