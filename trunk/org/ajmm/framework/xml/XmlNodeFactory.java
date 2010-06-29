package org.ajmm.framework.xml;

import java.util.Map;

/**
 * Super classes must implement the following public static methods:
 * <ul>
 * 		<li>Class&lt;? extends XmlNode&gt; getParentNodeClass()</li>
 * 		<li>XmlNode parse(String qName)</li>
 * </ul>
 * 
 * @author	Andrew Mackrodt
 * @version	2010.06.17
 */
public abstract class XmlNodeFactory
{
	protected XmlNodeFactory() {
		
	}
	
	public abstract XmlNode parse(String qName);
	
	protected static XmlNode parse(String qName,
			Map<String, Class<? extends XmlNode>> nodeMap)
	{
		XmlNode node = null;
		
		if (nodeMap != null)
		{
			try
			{
				Class<? extends XmlNode> nodeClass = nodeMap.get(qName);
				if (nodeClass != null) {
					node = nodeClass.newInstance();
				}
			}
			catch (Exception e) { 
				e.printStackTrace();
			}
		}
		
		return (node == null) ? new XmlNode(qName) :  node;
	}

}
