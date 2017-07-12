package saxon.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.xml.sax.SAXException;

import net.sf.saxon.Configuration;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trans.XPathException;

public class xquery {

	@Test
	public void test() throws XPathException, SAXException, IOException, ParserConfigurationException {
		 DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();  
         DocumentBuilder builder = builderFactory.newDocumentBuilder();  
           
         //从文档中加载xml内容  
         InputStream is = Class.class.getResourceAsStream("/flight-data.xml");  
         //注意：下面会导致is被关闭
         //Document document = builder.parse(is);  
         //document.normalize(); //去掉XML文档中空白部分  
           
         //从字符串中加载xml内容  
         //StringReader sr = new StringReader("<flight><row flightno=\"CA3411\" airline_code=\"CA\" airline_namecn=\"中国国际航空公司\" airline_nameen=\"Air China\" city_code=\"SHA\" city_namecn=\"上海虹桥\" city_nameen=\"Shanghai\" flight_date=\"20130202\" flight_time=\"2300\" status_code=\"fly\" status_namecn=\"起飞\" status_nameen=\"Fly\" checkin_counter=\"M2-3\" gate=\"A118\"/></flight>");  
         //InputSource is = new InputSource(sr);  
         //Document document = builder.parse(is);  
         //document.normalize(); //去掉XML文档中空白部分  
           
         //xQuery表达式  
         StringBuffer sb = new StringBuffer();  
         sb.append(" for $s in /flight/row where 1=1 ");  
         sb.append(" and contains(upper-case($s/@flightno), 'CA') ");  
         sb.append(" and contains(upper-case($s/@city_namecn), '海') ");  
         sb.append(" and upper-case($s/@airline_code)='CA' ");  
         sb.append(" and $s/@flight_date='20130202' ");  
         sb.append(" and $s/@flight_time>='2300' ");  
         sb.append(" and $s/@flight_time<='2300' ");  
         sb.append(" and $s/@status_code='fly' ");  
         sb.append(" return $s ");  
           
         Configuration config = new Configuration();  
           
         //静态查询上下文  
         StaticQueryContext sqc = config.newStaticQueryContext();  
         XQueryExpression expression = sqc.compileQuery(sb.toString());  
           
         //动态查询上下文  
         DynamicQueryContext dynamicContext = new DynamicQueryContext(config);  
         
         //新的方式
         dynamicContext.setContextItem(config.buildDocumentTree(new StreamSource(is)).getRootNode());
         //deprecated
         //dynamicContext.setContextItem(config.buildDocument(new StreamSource(is)));

           
         Properties props = new Properties();  
         props.setProperty(OutputKeys.METHOD, "xml");  
         props.setProperty(OutputKeys.INDENT, "yes");  
         props.setProperty(OutputKeys.ENCODING, "utf8");  
         props.setProperty(OutputKeys.VERSION, "1.0");  
           
         //根据xQuery表达式解析xml文件，返回符合条件的数据，存储到StreamResult对象  
         expression.run(dynamicContext, new StreamResult(System.out), props);  
           
	}

}
