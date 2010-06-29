package org.ajmm.framework.xml;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.ajmm.framework.StringUtil;

/**
 * 
 * 
 * @author	Andrew Mackrodt
 * @version	2010.06.19
 */
public class XmlNode
{	
	private final Set<XmlNode> children;
	private final String name;
	private final Map<String, String> attributes;
	private String text;
	
	public XmlNode(String name, int childCapacity)
	{
		this.children	= new LinkedHashSet<XmlNode>(childCapacity);
		this.name		= name;
		this.attributes	= new TreeMap<String, String>();
	}
	
	public XmlNode(String name)
	{
		this.children	= new LinkedHashSet<XmlNode>();
		this.name		= name;
		this.attributes	= new TreeMap<String, String>();
	}
	
	public String getNodeValue() {
		return text;
	}

	public int getNodeValueLength() {
		return (text == null) ? -1 : text.length(); 
	}
	
	public String setNodeValue(String text)
	{
		String oldValue = this.text;
		this.text = text;
		
		return oldValue;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean hasChildren() {
		return children.size() > 0;
	}
	
	public boolean hasChild(XmlNode element) {
		return children.contains(element);
	}
	
	public boolean addChild(XmlNode element)
	{
//		if (element.getChildCount() == 0
//				&& element.getAttributeCount() == 0
//				&& element.getNodeValueLength() < 1)
//			return false;
//		
		return children.add(element);
	}
	
	public boolean removeChild(XmlNode element) {
		return children.remove(element);
	}
	
	public Set<XmlNode> getChildren() {
		return Collections.unmodifiableSet(children);
	}
	
	public int getChildCount() {
		return children.size();
	}
	
	public boolean hasAttributes() {
		return attributes.size() > 0;
	}

	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}
	
	public String getAttribute(String name) {
		return attributes.get(name);
	}
	
	public int getAttributeAsInteger(String name)
	{
		String valueString = attributes.get(name);
		int value = -1;
		if (valueString != null && valueString.matches("^\\d+$")) {
			value = Integer.parseInt(valueString);
		}
		return value;
	}
	
	public String setAttribute(String name, String value)
	{
		String oldValueString = null;
		value = StringUtil.clean(value);

		/* add an attribute to the document */
		if (value != null && value.length() > 0)
			oldValueString = attributes.put(name, value);
		/* remove an attribute from the document */
		else 
			oldValueString = attributes.remove(name);

		return oldValueString;
	}
	
	public int setAttribute(String name, int value)
	{
		String oldValueString = null;
		int oldValue = -1;
		
		/* add an attribute to the document */
		if (value >= 0)
			oldValueString = attributes.put(name, ""+value);
		
		/* remove an attribute from the document */
		else
			oldValueString = attributes.remove(name);
		
		
		if (oldValueString != null&& oldValueString.matches("^\\d+$")) {
			oldValue = Integer.parseInt(oldValueString);
		}

		return oldValue;
	}
	
	public String removeAttribute(String name) {
		return setAttribute(name, null);
	}
	
	public Map<String, String> getAttributes() {
		return Collections.unmodifiableMap(attributes);
	}
	
	public int getAttributeCount() {
		return attributes.size();
	}
	
	public boolean isEmpty()
	{
		return
				getChildCount() == 0
						&& getAttributeCount() == 0
						&& getNodeValueLength() < 1;
	}
	
}
