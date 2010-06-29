package org.ajmm.vdj.xml;

import java.util.logging.Logger;

import org.ajmm.framework.xml.XmlReader;
import org.ajmm.vdj.database.Database;
import org.ajmm.vdj.database.exception.IllegalDatabaseException;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.06.17
 */
public abstract class VDJXmlReader
{
	protected static final Logger logger = Logger.getLogger(VDJXmlReader.class.getName());

	public static Database parse(String location) throws Exception
	{
		Database database = (Database)XmlReader.parse(location, VDJXmlNodeFactory.class);
		if (database == null) {
			throw new IllegalDatabaseException();
		}
		return database;
	}

}
