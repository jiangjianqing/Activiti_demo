package common.service.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import common.service.utils.SpringContextHolder;

public class RequestMappingUtils {
	
	public static Map<String , List<RequestMappingMethodInfo>>  getAllRequestMappings(RequestMappingHandlerMapping requestMappingHandlerMapping) {
		
		Map<String , List<RequestMappingMethodInfo>> controllerRequestMappingSet = new HashMap<String,List<RequestMappingMethodInfo>>();  
		
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> requestMappingInfoHandlerMethodEntry : handlerMethods.entrySet())
        {
            RequestMappingInfo requestMappingInfo = requestMappingInfoHandlerMethodEntry.getKey();
            HandlerMethod mappingInfoValue = requestMappingInfoHandlerMethodEntry.getValue();

            RequestMethodsRequestCondition methodCondition = requestMappingInfo.getMethodsCondition();
            //SetUtil.first
            //String requestType = methodCondition.getMethods().iterator().next().name();
            String requestType="";
            for (RequestMethod requestMethod : methodCondition.getMethods()) {  
            	if (requestType.length()>0) 
            		requestType = requestType + ",";
            	requestType = requestType + requestMethod.name();
            	//hashMap.put("url", url);  
            } 
            
            
            PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();

            String requestUrl="";
            for (String url : patternsCondition.getPatterns()) { 
            	if(requestUrl.length()>0)
            		requestUrl = requestUrl + ",";
            	requestUrl = requestUrl + url;
            	//hashMap.put("url", url);  
            } 

            String controllerName = mappingInfoValue.getBeanType().toString();
            String requestMethodName = mappingInfoValue.getMethod().getName();
            Class<?>[] methodParamTypes = mappingInfoValue.getMethod().getParameterTypes();
            RequestMappingMethodInfo item = new RequestMappingMethodInfo(requestUrl, requestType, controllerName, requestMethodName, methodParamTypes);

            List<RequestMappingMethodInfo> requestToMethodItemList = controllerRequestMappingSet.get(controllerName);
            if(requestToMethodItemList==null) {
            	requestToMethodItemList = new ArrayList<RequestMappingMethodInfo>();
            	controllerRequestMappingSet.put(controllerName, requestToMethodItemList);
            }
            requestToMethodItemList.add(item);
        }
        return controllerRequestMappingSet;
	
	}
	
	
	/**
	 * "2017.08.20 z这里存在尚未解决的错误，allRequestMappings.size 总是 ==0 "
	 * @return
	 */
	@Deprecated
	public static Map<String , List<RequestMappingMethodInfo>> getAllRequestMappings() {
		/*
		ServletContext servletContext = request.getSession().getServletContext();
        if (servletContext == null)
        {
            return null;
        }*/
        //WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		
		ApplicationContext appContext = SpringContextHolder.getApplicationContext();
		
        //请求url和处理方法的映射
        //获取所有的RequestMapping

        /*2017.08.20 z这里存在尚未解决的错误，allRequestMappings.size 总是 ==0 */
        Map<String, HandlerMapping> allRequestMappings = BeanFactoryUtils.beansOfTypeIncludingAncestors(appContext, 
        		HandlerMapping.class, true, false);
        System.out.println("RequestMappingUtils error requestMappings=0, if requestMappings count="+allRequestMappings.size());

        for (HandlerMapping handlerMapping : allRequestMappings.values())
        {
            //本项目只需要RequestMappingHandlerMapping中的URL映射
            if (handlerMapping instanceof RequestMappingHandlerMapping)
            {
                RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) handlerMapping;
                return RequestMappingUtils.getAllRequestMappings(requestMappingHandlerMapping);
            }
        }
        return null;
	}
}
