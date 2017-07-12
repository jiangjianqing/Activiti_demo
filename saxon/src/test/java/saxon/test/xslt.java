package saxon.test;

import static org.junit.Assert.*;

import java.io.InputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;

import my.xslt.XsltUtil;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

public class xslt {

	private XsltUtil xsltUtil=new XsltUtil();
	
	@Test
	public void test1() throws SaxonApiException {
		System.out.println("\n《--------------------以下为增强方式---------------------------------》");
		
		//方式1：略复杂，但可配置能力强
		xsltUtil.doExtendXslt("/xsl/books.xsl", "/data/books.xml", System.out);
	}
	
	@Test
	public void test2() throws TransformerException{
		System.out.println("\n《--------------------以下为标准方式---------------------------------》");
		
		xsltUtil.doSimpleXslt("/xsl/books.xsl", "/data/books.xml", System.out);	
	}

}
