package org.ajmm.vdj.database;

/**
 * 
 * 
 * @author	Andrew Mackrodt
 * @version	2010.06.14
 */
public class FavoriteFolder extends AbstractFolder
{
	public FavoriteFolder() {
		super("FavoriteFolder", 0);
	}

	public String getFolderPath() {
		return getAttribute("FolderPath");
	}

	public String setFolderPath(String folderPath) {
		return setAttribute("FolderPath", folderPath);
	}

}
