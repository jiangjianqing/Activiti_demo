package my.xslt;

import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

public class XsltUtil {

	public Serializer doExtendXslt(String xlsFile, String xmlDataFile, OutputStream outputStream) throws SaxonApiException {

		// 方式1：略复杂，但可配置能力强
		Processor proc = new Processor(false);
		XsltCompiler comp = proc.newXsltCompiler();
		XsltExecutable exp = comp.compile(new StreamSource(Class.class.getResourceAsStream(xlsFile)));
		XdmNode source = proc.newDocumentBuilder()
				.build(new StreamSource(Class.class.getResourceAsStream(xmlDataFile)));

		Serializer out = proc.newSerializer(outputStream);
		// Serializer out = proc.newSerializer(new File("books.html"));
/*注意：这里可以单独设置输出的属性，但是会导致丢失<?xml version="1.0" encoding="UTF-8"?>
		out.setOutputProperty(Serializer.Property.METHOD, "html");
		out.setOutputProperty(Serializer.Property.INDENT, "yes");
		out.setOutputProperty(Serializer.Property.OMIT_XML_DECLARATION, "no");
		out.setOutputProperty(Serializer.Property.STANDALONE, "yes");*/
		XsltTransformer trans = exp.load();
		trans.setInitialContextNode(source);
		trans.setDestination(out);
		trans.transform();
		return out;
	}

	public Result doSimpleXslt(String xlsFile, String xmlDataFile,OutputStream outputStream) throws TransformerException {
		// 方式2：最简单
		// 创建一个转换工厂
		TransformerFactory tFactory = TransformerFactory.newInstance();
		// 用指定的XSLT样式单文件创建一个转换器
		Transformer transformer = tFactory
				.newTransformer(new StreamSource(Class.class.getResourceAsStream(xlsFile)));
		// 执行转换，并将转换后的目标文档作为响应输出
		Result result=new StreamResult(outputStream);
		transformer.transform(new StreamSource(Class.class.getResourceAsStream(xmlDataFile)),
				result);
		return result;
	}

}
