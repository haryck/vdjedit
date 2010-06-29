package org.ajmm.vdj.database;

import org.ajmm.framework.xml.XmlNode;

/**
 * 
 * 
 * @author	Andrew Mackrodt
 * @version	2010.06.16
 */
public abstract class AbstractFolder extends XmlNode implements Comparable<AbstractFolder>
{	
	public AbstractFolder(String name, int childCapacity) {
		super(name, childCapacity);
	}
	
	public AbstractFolder(String name) {
		super(name);
	}

	public String getFolderName() {
		return getAttribute("Name");
	}

	public String setFolderName(String folderName) {
		return setAttribute("Name", folderName);
	}
	
	public int getOrder() {
		return getAttributeAsInteger("Order");
	}

	public int setOrder(int order) {
		return setAttribute("Order", order);
	}
	
	public int compareTo(AbstractFolder a) {
		return getOrder() - a.getOrder();
	}
	
}
