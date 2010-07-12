package org.ajmm.vdj.database;

import org.ajmm.vdj.StringUtil;
import org.xml.sax.Attributes;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.12
 */
public class FavoriteFolder extends AbstractFolder
{
	public static final String ELEMENT_NAME = "FavoriteFolder";
	public static final String ATTRIB_FOLDER_PATH = "FolderPath";
	
	private String folderPath;
	
	public FavoriteFolder(String name, String folderPath) {
		super(name); setFolderPath(folderPath);
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		if (folderPath == null || folderPath.length() == 0) return;
		this.folderPath = folderPath;
	}
	
	public static void parse(Attributes atts, Database database)
	{
		if (atts == null || atts.getLength() == 0 || database == null) return;
		
		String folderPath = atts.getValue(ATTRIB_FOLDER_PATH);
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
