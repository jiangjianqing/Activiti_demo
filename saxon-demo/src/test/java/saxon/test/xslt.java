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

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

public class xslt {

	@Test
	public void test1() throws TransformerException, SaxonApiException {
		System.out.println("\n《--------------------以下为增强方式---------------------------------》");
		
		//方式1：略复杂，但可配置能力强
		Processor proc = new Processor(false);
        XsltCompiler comp = proc.newXsltCompiler();
        XsltExecutable exp = comp.compile(new StreamSource(Class.class.getResourceAsStream("/styles/books.xsl")));
        XdmNode source = proc.newDocumentBuilder().build(new StreamSource(Class.class.getResourceAsStream("/data/books.xml")));
        
        Serializer out = proc.newSerializer(System.out);
        //Serializer out = proc.newSerializer(new File("books.html"));
        out.setOutputProperty(Serializer.Property.METHOD, "html");
        out.setOutputProperty(Serializer.Property.INDENT, "yes");
        XsltTransformer trans = exp.load();
        trans.setInitialContextNode(source);
        trans.setDestination(out);
        trans.transform();

		
 
	}
	
	@Test
	public void test2() throws TransformerException{
		System.out.println("\n《--------------------以下为标准方式---------------------------------》");
		
		//方式2：最简单
		//创建一个转换工厂  
		TransformerFactory tFactory = TransformerFactory.newInstance();  
		//用指定的XSLT样式单文件创建一个转换器  
		Transformer transformer = tFactory.newTransformer(new StreamSource(Class.class.getResourceAsStream("/styles/books.xsl")));  
		//执行转换，并将转换后的目标文档作为响应输出  
		transformer.transform(new StreamSource(Class.class.getResourceAsStream("/data/books.xml")), new StreamResult(System.out)); 		
	}

}
