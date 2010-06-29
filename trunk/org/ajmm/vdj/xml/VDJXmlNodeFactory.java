package org.ajmm.vdj.xml;

import java.util.Map;
import java.util.TreeMap;

import org.ajmm.framework.xml.XmlNode;
import org.ajmm.framework.xml.XmlNodeFactory;
import org.ajmm.vdj.database.Automix;
import org.ajmm.vdj.database.BPM;
import org.ajmm.vdj.database.Comment;
import org.ajmm.vdj.database.Database;
import org.ajmm.vdj.database.Display;
import org.ajmm.vdj.database.FAME;
import org.ajmm.vdj.database.FavoriteFolder;
import org.ajmm.vdj.database.FilterFolder;
import org.ajmm.vdj.database.Infos;
import org.ajmm.vdj.database.Link;
import org.ajmm.vdj.database.Song;
import org.ajmm.vdj.database.VirtualFolder;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.06.17
 */
public class VDJXmlNodeFactory extends XmlNodeFactory
{
	private static final Map<String, Class<? extends XmlNode>> map;
	private static VDJXmlNodeFactory factoryInstance;

	static
	{
		map = new TreeMap<String, Class<? extends XmlNode>>();

		map.put( "VirtualDJ_Database",	Database.class			);
		map.put( "FavoriteFolder",		FavoriteFolder.class	);
		map.put( "FilterFolder",		FilterFolder.class		);
		map.put( "VirtualFolder",		VirtualFolder.class		);
		map.put( "Song",				Song.class				);
		map.put( "Display",				Display.class			);
		map.put( "Infos",				Infos.class				);
		map.put( "Comment",				Comment.class			);
		map.put( "BPM",					BPM.class				);
		map.put( "FAME",				FAME.class				);
		map.put( "Automix",				Automix.class			);
		map.put( "Link",				Link.class				);
	}

	private VDJXmlNodeFactory() {

	}

	public static VDJXmlNodeFactory getInstance()
	{
		if (factoryInstance == null)
			factoryInstance = new VDJXmlNodeFactory();

		return factoryInstance;
	}

	public static Class<? extends XmlNode> getParentNodeClass() {
		return Database.class;
	}

	@Override
	public XmlNode parse(String qName) {
		return super.parse(qName, map);
	}

}
