package org.ajmm.framework.xml;

import java.lang.reflect.Method;
import java.util.logging.Logger;

public abstract class XmlReader
{
	protected static final Logger logger = Logger.getLogger(XmlReader.class.getName());

	@SuppressWarnings("unchecked")
	public static XmlNode parse(String location, Class<? extends XmlNodeFactory> factoryClass)
	{
		Class<? extends XmlNode> type = null;

		try
		{
			Method method = factoryClass.getMethod("getParentNodeClass", (Class[])null);
			type = (Class<? extends XmlNode>)method.invoke(null, (Object[])null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		long start = System.currentTimeMillis();
		logger.info("attempting to parse " + type.getName() +" \"" + location + "\"");

		XmlNode xmlNode = XmlUtil.load(location, factoryClass);
		if (!type.isInstance(xmlNode)) {
			return null;
		}

		long elapsed = System.currentTimeMillis() - start;
		String message = "successfully parsed " + type.getName() + " in " + elapsed + "ms";
		logger.info(message);

		return xmlNode;
	}
}
