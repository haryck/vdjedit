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
 * @version	2010.07.01
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

	protected String getNodeValue() {
		return text;
	}

	protected int getNodeValueLength() {
		return (text == null) ? -1 : text.length();
	}

	protected String setNodeValue(String text)
	{
		String oldValue = this.text;
		this.text = text;

		return oldValue;
	}

	protected String getName() {
		return name;
	}

	protected boolean hasChildren() {
		return children.size() > 0;
	}

	protected boolean hasChild(XmlNode element) {
		return children.contains(element);
	}

	protected boolean addChild(XmlNode element) {
		return children.add(element);
	}

	protected boolean removeChild(XmlNode element) {
		return children.remove(element);
	}

	protected Set<XmlNode> getChildren() {
		return Collections.unmodifiableSet(children);
	}

	protected int getChildCount() {
		return children.size();
	}

	protected boolean hasAttributes() {
		return attributes.size() > 0;
	}

	protected boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}

	protected String getAttribute(String name) {
		return attributes.get(name);
	}

	protected int getAttributeAsInteger(String name) {
		return parseInt(attributes.get(name));
	}

	protected String setAttribute(String name, String value) {
		value = StringUtil.clean(value);
		String oldValue = (value != null && value.length() > 0)
				? attributes.put(name, value)	  // add attribute
				: attributes.remove(name);		  // remove attribute
		return oldValue;
	}

	protected int setAttribute(String name, int value) {
		String oldValue = (value >= 0)
			    ? attributes.put(name, ""+value)  // add attribute
			    : attributes.remove(name);		  // remove attribute
	    return parseInt(oldValue);
	}

	protected String removeAttribute(String name) {
		return setAttribute(name, null);
	}

	protected Map<String, String> getAttributes() {
		return Collections.unmodifiableMap(attributes);
	}

	protected int getAttributeCount() {
		return attributes.size();
	}

	protected boolean isEmpty() {
		return getChildCount() == 0
				&& getAttributeCount() == 0
				&& getNodeValueLength() < 1;
	}

	protected int parseInt(String string) {
	    return
	    		(string != null && string.matches("^\\d+$"))
	    		? Integer.parseInt(string) : -1;
	}

}
