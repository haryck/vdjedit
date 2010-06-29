package org.ajmm.framework.xml;

import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.ajmm.framework.StringUtil;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * A convenience class to manage XML files (save/load/create) without having
 * to handle the exceptions normally associated with these methods. Methods
 * will return false or null if the requested operation has been unsuccessful.
 * The method {@link XmlUtil#exception()} is provided to get the exact error.
 * 
 * @author	Andrew Mackrodt
 * @version	2010.06.17
 */
public abstract class XmlUtil
{
	private static Exception exception;
	
	/**
	 * Load an xml document from the file-system. Returns an XmlNode if
	 * successful or null if an exception occurred. Use the method
	 * {@link XmlUtil#exception()} to retrieve the exception.
	 * 
	 * @param location	the location of the xml file to load
	 * @param nFactory	the factory used to create objects from xml nodes
	 * @return			the loaded document if successful
	 */
	public static XmlNode load(String location, final Class<? extends XmlNodeFactory> factoryClass)
	{	
		final LinkedList<XmlNode> nodelist = new LinkedList<XmlNode>();
		Exception exception = null;
		
		try
		{
			final Method method = factoryClass.getMethod("getInstance", (Class[])null);
			final XmlNodeFactory factoryInstance = (XmlNodeFactory)method.invoke(null, (Object[])null);
			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			parser.parse(location, new DefaultHandler()
			{
				private StringBuilder sb = new StringBuilder();
				
				@Override
				public void endElement(String uri, String localName, String qName)
				{
					if (nodelist.size() > 1 &&
							nodelist.getLast().getName().equals(qName)) {
						XmlNode node = nodelist.removeLast();
						if (sb.length() > 0) node.setNodeValue(sb.toString());
						nodelist.getLast().addChild(node);
					}					
				}

				@Override
				public void startElement(String uri, String localName, String qName, Attributes atts)
				{
					if (sb.length() > 0) sb = new StringBuilder();
					XmlNode node = factoryInstance.parse(qName);
					for (int i = 0; i < atts.getLength(); i++) {
						node.setAttribute(atts.getQName(i), atts.getValue(i));
					}
					nodelist.add(node);
				}
				
				@Override
				public void characters(char[] ch, int start, int length)
				{
					if (length > 0)
					{
						String nodeValue = new String(ch, start, length);
						nodeValue = StringUtil.clean(nodeValue);
						
						if (nodeValue.length() > 0) sb.append(nodeValue);
					}
				}
				
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally {
			XmlUtil.exception = exception;
		}
		
		return (exception == null) ? nodelist.poll() : null;
	}
	
	/**
	 * Save an xml document to the file-system. Returns true if successful
	 * otherwise an exception has occurred. Use the {@link XmlUtil#exception()}
	 * method to retrieve the exception.
	 * 
	 * @param document	the document to save
	 * @param location	the location to save the xml file
	 * @return			true if the document saved with no errors
	 */
	public static boolean save(Document document, String location)
	{
		Exception exception = null;
		try
		{
	        OutputFormat format = new OutputFormat(document);
	        format.setEncoding("UTF-8");
	        format.setIndent(1);
	        format.setLineWidth(0);
	        format.setLineSeparator("\r\n");
			
	        FileOutputStream outputStream = new FileOutputStream(location);
	        XMLSerializer serializer = new XMLSerializer(outputStream, format);
	        serializer.serialize(document);
	        
			outputStream.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally {
			XmlUtil.exception = exception;
		}
		
		return exception == null;
	}
	
	/**
	 * Convenience method to create a new xml document. This will typically
	 * always return the new document but can return null if an exception
	 * occurred. Use {@link XmlUtil#exception()} to get the exception. 
	 * 
	 * @return	the new empty xml document
	 */
	public static Document create()
	{
		Exception exception = null;
		Document document = null;
		try {
			DocumentBuilder documentBuilder =
				DocumentBuilderFactory.newInstance().newDocumentBuilder();
			document = documentBuilder.newDocument();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally {
			XmlUtil.exception = exception;
		}
		
		return document;
	}
	
	/**
	 * Returns the exception last thrown by any method of this class
	 * 
	 * @return	the exception last thrown 
	 */
	public static Exception exception() {
		return exception;
	}
	
}
