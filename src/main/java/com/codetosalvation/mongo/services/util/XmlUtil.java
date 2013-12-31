package com.codetosalvation.mongo.services.util;

import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.InputSource;


public final class XmlUtil {
	static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	static {
		dbf.setIgnoringComments(true);
		dbf.setIgnoringElementContentWhitespace(true);
	}
	private XmlUtil() {}

	public static boolean isValidXML(String xml) {
		try{
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Reader reader = null;
			try{
				reader = new StringReader(xml);
				builder.parse(new InputSource(reader));
			}finally {
				if(reader != null) {
					reader.close();
				}
			}
			return true;
		}catch(Exception e) {
			return false;
		}
	}
}
