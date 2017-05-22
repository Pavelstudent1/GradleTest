package xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.SAXException;

public class JDOMExample
{
	public static void main(String[] args) throws JDOMException, IOException, SAXException, ParserConfigurationException
	{
		InputStream fileInputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("catalogue2.xml");
		SAXBuilder builder = new SAXBuilder();

		Document doc = builder.build(fileInputStream);

		Format format = Format.getPrettyFormat();
		format.setIndent("    ");
		XMLOutputter outputter = new XMLOutputter(format);
		outputter.output(doc, System.out);
	}

}
