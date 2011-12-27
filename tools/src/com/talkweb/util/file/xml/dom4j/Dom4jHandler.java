package com.talkweb.util.file.xml.dom4j;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * @author bluesky
 * http://www.ibm.com/developerworks/cn/xml/x-dom4j.html?ca=drs-
 */
public class Dom4jHandler {
	
	public void otherOperate() throws DocumentException{
		SAXReader reader = new SAXReader();
		Document docu = reader.read(new File("/domain.xml"));

		// ��������
		// domain

		// ����Ԫ�ص�����ֵ
		Element graphics = docu.getRootElement().element("devices")
				.element("graphics");
		Attribute attrPort = graphics.attribute("port");
		attrPort.setText("");
		// ����Ԫ��ֵ
		Element nameEle = docu.getRootElement().element("name");
		nameEle.setText("new name");
		// ���ַ�����ʽ����xml
		String docXmlText = docu.asXML();
	}

	public void generateDocument() {

		//������������
		Document document = DocumentHelper.createDocument();

		//��Ӹ�
		Element catalogElement = document.addElement("catalog");
		catalogElement.addComment("An XML Catalog");

		catalogElement.addProcessingInstruction("target", "text");

		//�������
		Element journalElement = catalogElement.addElement("journal");
		//�������
		journalElement.addAttribute("title", "XML Zone");
		journalElement.addAttribute("publisher", "IBM developerWorks");

		Element articleElement = journalElement.addElement("article");
		articleElement.addAttribute("level", "Intermediate");
		articleElement.addAttribute("date", "December-2001");

		Element titleElement = articleElement.addElement("title");
		//����ֵ
		titleElement.setText("Java configuration with XML Schema");

		Element authorElement = articleElement.addElement("author");
		Element firstNameElement = authorElement.addElement("firstname");
		firstNameElement.setText("Marcello");
		Element lastNameElement = authorElement.addElement("lastname");
		lastNameElement.setText("Vitaletti");

		//document.addDocType("catalog", null, "file://c:/Dtds/catalog.dtd");

		try {
			//�������
			XMLWriter output = new XMLWriter(new FileWriter(new File(
					"/home/bluesky/test/catalog/catalog.xml")));
			output.write(document);
			output.flush();
			output.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	//Attribute ����
	//Element Ԫ��
	public void modifyDocument(File inputXml) {
		try {
			//�����Ķ���
			SAXReader saxReader = new SAXReader();
			
			saxReader.setEncoding("utf-8"); 
			
			//����
			Document document = saxReader.read(inputXml);
			
			//��ȡ �ڵ��е����� @ ����
			List<?> list = document.selectNodes("//article/@level");
			
			Iterator<?> iter = list.iterator();
			while (iter.hasNext()) {
				//��������
				Attribute attribute = (Attribute) iter.next();
				//�������ֵ
				if (attribute.getValue().equals("Intermediate"))
					attribute.setValue("Introductory");
			}

			list = document.selectNodes("//article/@date");
			iter = list.iterator();
			while (iter.hasNext()) {
				Attribute attribute = (Attribute) iter.next();
				if (attribute.getValue().equals("December-2001"))
					attribute.setValue("October-2002");
			}
			
			//��ȡ�ӽڵ� ����
			list = document.selectNodes("//article");
			iter = list.iterator();
			while (iter.hasNext()) {
				//�����ӽڵ�
				Element element = (Element) iter.next();
				
				//��ȡ�ӽڵ�
				Iterator iterator = element.elementIterator("title");
				
				while (iterator.hasNext()) {
					Element titleElement = (Element) iterator.next();
					//�ȶ�ֵ
					if (titleElement.getText().equals(
							"Java configuration with XMLSchema"))
						//����ֵ
						titleElement
								.setText("Create flexible and extensible XML schema");
				}
			}
			
			list = document.selectNodes("//article/author");
			iter = list.iterator();
			while (iter.hasNext()) {
				Element element = (Element) iter.next();
				Iterator iterator = element.elementIterator("firstname");
				while (iterator.hasNext()) {
					Element firstNameElement = (Element) iterator.next();
					if (firstNameElement.getText().equals("Marcello"))
						firstNameElement.setText("Ayesha");
				}
			}
			
			list = document.selectNodes("//article/author");
			iter = list.iterator();
			while (iter.hasNext()) {
				Element element = (Element) iter.next();
				Iterator iterator = element.elementIterator("lastname");
				while (iterator.hasNext()) {
					Element lastNameElement = (Element) iterator.next();
					if (lastNameElement.getText().equals("Vitaletti"))
						lastNameElement.setText("Malik");
				}
			}
			
			XMLWriter output = new XMLWriter(new FileWriter(new File(
					"/home/bluesky/test/catalog/catalog-modified.xml")));
			output.write(document);
			output.close();
		}

		catch (DocumentException e) {
			System.out.println(e.getMessage());
			
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] argv) {
		/*Dom4jHandler dom4j = new Dom4jHandler();
		dom4j.generateDocument();*/

		System.out.println("ok");

		Dom4jHandler dom4jParser = new Dom4jHandler();
		dom4jParser.modifyDocument(new File(
				"/home/bluesky/test/catalog/catalog.xml"));
	}
}
