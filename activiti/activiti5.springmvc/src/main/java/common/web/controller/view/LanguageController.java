package common.web.controller.view;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.support.RequestContext;

import common.web.utils.AbstractHelperClass;

@Controller
@RequestMapping(value = "/language")
public class LanguageController extends AbstractHelperClass {

	/**
	 * 注意在spring-i18n.xml中，session与cookie的LocaleResolver都是同一个命名，当心冲突
	 */
	@Resource
	private CookieLocaleResolver localeResolver;
	
	@RequestMapping(value="/change", method = {RequestMethod.GET})
	public String setCurrentLanguage(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam(value="langType", defaultValue="zh") String langType){

		Locale locale=null;
		switch(langType){
			case "zh":
				locale = new Locale("zh", "CN"); 	        
				break;
			case "en":
				locale = new Locale("en", "US"); 
				break;
			default:
				locale=LocaleContextHolder.getLocale();
		}
		//设置基于cookie的国际化信息
		localeResolver.setLocale (request, response, locale);
		//(new CookieLocaleResolver()).setLocale (request, response, locale);
		//设置基于session的国际化信息
		request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,locale); 
	    
		//20160211 注意：cookie和session不需要同时使用，但这么写代码比较简洁，只需要在spring-i18n.xml中修改使用哪种
		
	    //从后台代码获取国际化信息
	    RequestContext requestContext = new RequestContext(request);
	    
	    logger.warn(String.format("i18n.username=%s", requestContext.getMessage("i18n.username")));
	    logger.warn(String.format("i18n.password=%s", requestContext.getMessage("i18n.password")));
	    //model.addAttribute("money", requestContext.getMessage("i18n.username"));
	    //model.addAttribute("date", requestContext.getMessage("date"));
	    
	    //这里直接跳转到根视图，没有考虑上次所处的页面
	    String viewName="redirect:/";
		return viewName;
	}
}
