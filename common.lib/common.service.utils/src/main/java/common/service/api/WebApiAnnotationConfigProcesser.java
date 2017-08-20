package common.service.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import common.service.api.annotation.ApiMethod;
import common.service.api.annotation.ApiMethodParam;
import common.service.utils.AbstractHelperClass;

public class WebApiAnnotationConfigProcesser extends AbstractHelperClass implements ApplicationContextAware,InitializingBean {
	
	private Map<String,List<ApiMethodInfo>> mappers = 
			new HashMap<String,List<ApiMethodInfo>>();
	
	private WebApplicationContext applicationContext;
	
	/**
	 * <p>方法描述：加载带有{@link common.service.api.annotation.ApiMethodInfo.gjds.app.annotation.AppInterface}注解的接口</p>
	 * <p>首先需要获取所有接口，然后过滤方法中带有@AppInterface</p>
	 * <p>创建人: 王成委  </p>
	 * <p>创建时间: 2015年1月10日 上午10:50:06 </p>
	 */
	public void loadHandlerMapping(){
		this.logger.info("初始化配置");
		Map<String, HandlerMapping> handlers = BeanFactoryUtils.beansOfTypeIncludingAncestors(
				applicationContext, HandlerMapping.class, true, false);
		logger.warn("准备获取所有的HandlerMapping,count=" + handlers.size());
		for(Entry<String, HandlerMapping> entry : handlers.entrySet()){
			HandlerMapping mapping = entry.getValue();
			if(mapping instanceof RequestMappingHandlerMapping){
				logger.warn("发现RequestMappingHandlerMapping,success!");
				RequestMappingHandlerMapping requestHandler = (RequestMappingHandlerMapping)mapping;
				Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestHandler.getHandlerMethods();
				for(Entry<RequestMappingInfo, HandlerMethod> handlerMethod : handlerMethods.entrySet()){
					ApiMethod annotation = handlerMethod.getValue().getMethodAnnotation(ApiMethod.class);
					if(annotation== null)continue;
					PatternsRequestCondition patternsCondition = handlerMethod.getKey().getPatternsCondition();
					String requestUrl = patternsCondition.getPatterns().iterator().next();//SetUtils.first(patternsCondition.getPatterns());
					this.register(requestUrl, annotation,handlerMethod.getValue().getBeanType());
				}
			}
		}
	}
	
	/**
	 * <p>方法描述：注册方法</p>
	 * <p>创建人: 王成委  </p>
	 * <p>创建时间: 2015年1月10日 上午10:50:06 </p>
	 * @param requestUrl
	 * @param annotation
	 * @param beanType 
	 */
	private void register(String requestUrl, ApiMethod annotation,
			Class<?> beanType) {
		String group = annotation.group();
		List<ApiMethodInfo> groupMappers = this.mappers.get(group);
		if(groupMappers == null)
			groupMappers = new ArrayList<ApiMethodInfo>();
		ApiMethodInfo mapper = new ApiMethodInfo();
		mapper.setGroup(group);
		mapper.setController(beanType.getName());
		mapper.setOrder(annotation.order());
		mapper.setName(annotation.value());
		mapper.setUrl(requestUrl);
		mapper.setParams(this.toParameters(annotation.params()));
		groupMappers.add(mapper);
		this.mappers.put(group, groupMappers);
	}
	
	/**
	 * <p>方法描述：读取参数</p>
	 * <p>创建人: 王成委  </p>
	 * <p>创建时间: 2015年1月10日 上午10:50:06 </p>
	 * @param params
	 * @return
	 */
	private List<ApiMethodParamInfo> toParameters(ApiMethodParam[] params){
		List<ApiMethodParamInfo> parameters = new ArrayList<ApiMethodParamInfo>();
		
		for(ApiMethodParam param : params){
			ApiMethodParamInfo bean = new ApiMethodParamInfo();
			bean.setName(param.name());
			bean.setDesc(param.desc());
			if(StringUtils.startsWithIgnoreCase(param.testValue(), "#")){
				String var = param.testValue();
				String value = getByVariable(var.substring(var.indexOf("#")+1));
				bean.setTestValue(value);
			}else{
				bean.setTestValue(param.testValue());
			}
			parameters.add(bean);
		}
		return parameters;
	}
	
	/**
	 * <p>方法描述：获取变量的值</p>
	 * <p>创建人: 王成委  </p>
	 * <p>创建时间: 2015年1月10日 上午10:50:06 </p>
	 * @param var
	 * @return
	 */
	private String getByVariable(String var){
		return "#getByVariable is invalid";//
		//Variable variable = Variable.valueOf(var);
		//return variable.getValue();
	}

	/**
	 * <p>方法描述：对接口方法根据分组排序</p>
	 * <p>创建人: 王成委  </p>
	 * <p>创建时间: 2015年1月20日 下午16:00:06 </p>
	 */
	private void orderMappers(){
		for(Entry<String,List<ApiMethodInfo>> entry : this.mappers.entrySet() ){
			Collections.sort(entry.getValue(), new Comparator<ApiMethodInfo>() {
						
				@Override
				public int compare(ApiMethodInfo o1, ApiMethodInfo o2) {
					Integer one = o1.getOrder();
					Integer two = o2.getOrder();
					if(one != null && two != null) return one.intValue()-two.intValue();
					return 0;
				}
			});
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = (WebApplicationContext)applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.loadHandlerMapping();
		this.orderMappers();
		this.applicationContext.getServletContext().setAttribute("api", this.mappers);
	}
	
	/**
	 * <p>方法描述：刷新接口信息</p>
	 * <p>创建人: 王成委  </p>
	 * <p>创建时间: 2015年1月10日 上午10:50:06 </p>
	 * @throws Exception
	 */
	public void refresh() throws Exception{
		this.mappers = new HashMap<String,List<ApiMethodInfo>>();
		this.afterPropertiesSet();
	}
}