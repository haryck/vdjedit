package org.ajmm.vdj.database;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.12
 */
public abstract class AbstractFolder implements Comparable<AbstractFolder>
{
	public static final String ELEMENT_NAME = "AbstractFolder";
	public static final String ATTRIB_NAME = "Name";
	public static final String ATTRIB_ORDER = "Order";
	
	private String name = "No Name";
	private int order = 1;
	
	public AbstractFolder(String name) {
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name != null && name.length() > 0) this.name = name;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		if (order > 0) this.order = order;
	}

	public int compareTo(AbstractFolder a) {
		return order - a.order;
	}

}
