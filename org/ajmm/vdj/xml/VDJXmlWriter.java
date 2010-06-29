package org.ajmm.vdj.xml;

import org.ajmm.framework.xml.XmlWriter;
import org.ajmm.vdj.database.Database;

/**
 * 
 * 
 * @author	Andrew Mackrodt
 * @version	2010.06.17
 */
public class VDJXmlWriter extends XmlWriter
{	
	private VDJXmlWriter(Database database, String location) {
		super(database, location);
	}

	public static void save(Database database, String location) throws Exception {
		new VDJXmlWriter(database, location).write();
	}

}
