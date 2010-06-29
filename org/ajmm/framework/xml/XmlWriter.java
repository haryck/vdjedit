package org.ajmm.framework.xml;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.06.17
 */
public abstract class XmlWriter
{
	protected static final Logger logger = Logger.getLogger(XmlWriter.class.getName());

	private final XmlNode xmlNode;
	private final String location;
	private Document document;

	protected XmlWriter(XmlNode xmlNode, String location)
	{
		this.xmlNode = xmlNode;
		this.location = location;
	}

	protected void write() throws Exception
	{
		/* create an empty xml document or throw exception if it fails */
		if ((document = XmlUtil.create()) == null) throw XmlUtil.exception();

		Element docElement = document.createElement(xmlNode.getName());

		if (xmlNode.getAttributeCount()  > 0) writeAttributes(docElement, xmlNode);
		if (xmlNode.getNodeValueLength() > 0)
		{
			String nodeValue = xmlNode.getNodeValue();
			docElement.setTextContent(nodeValue);
		}

		Iterator<XmlNode> itr = xmlNode.getChildren().iterator();
		while (itr.hasNext()) {
			append(docElement, itr.next());
		}

		document.appendChild(docElement);

		/* save the created xml document or throw exception if it fails */
		if (!XmlUtil.save(document, location)) throw XmlUtil.exception();
	}

	private void append(Element parent, XmlNode xmlNode)
	{
		Element element = document.createElement(xmlNode.getName());
		if (xmlNode.getAttributeCount()  > 0) writeAttributes(element, xmlNode);
		if (xmlNode.getNodeValueLength() > 0)
		{
			String nodeValue = xmlNode.getNodeValue();
			element.setTextContent(nodeValue);
		}

		if (xmlNode.getChildCount() > 0)
		{
			Iterator<XmlNode> itr = xmlNode.getChildren().iterator();
			while (itr.hasNext()) {
				append(element, itr.next());
			}
		}

		if (!xmlNode.isEmpty()) parent.appendChild(element);
	}

	private void writeAttributes(Element element, XmlNode xmlNode)
	{
		Map<String, String> attributes = xmlNode.getAttributes();
		Iterator<String> itr = attributes.keySet().iterator();
		while (itr.hasNext())
		{
			String name  = itr.next();
			String value = attributes.get(name);
			element.setAttribute(name, value);
		}
	}

}
