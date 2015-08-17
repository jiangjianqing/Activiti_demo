package common.spring;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * <p>
 * 防止重复提交过滤器
 * </p>
 *
 * @author: chuanli
 * @date: 2013-6-27上午11:19:05
 */
public class AvoidDuplicateSubmissionInterceptor extends
		HandlerInterceptorAdapter {
	private static final Logger LOG = Logger
			.getLogger(AvoidDuplicateSubmissionInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		// User user = UserUtil.getUser();
		// if (user != null) {
		if (1 == 1) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();

			AvoidDuplicateSubmission annotation = method
					.getAnnotation(AvoidDuplicateSubmission.class);
			if (annotation != null) {
				boolean needSaveSession = annotation.needSaveToken();
				if (needSaveSession) {
					String token = "";
					token = new BigInteger(165, new Random()).toString(36)
							.toUpperCase();
					request.getSession(false).setAttribute("token",	token);
					//request.getSession(false).setAttribute("token",	TokenProcessor.getInstance().generateToken());
				}

				boolean needRemoveSession = annotation.needRemoveToken();
				if (needRemoveSession) {
					if (isRepeatSubmit(request)) {
						LOG.info("AvoidDuplicateSubmissionInterceptor提示：请不要提交无效数据");
						//LOG.warn("please don't repeat submit,[user:"+ user.getUsername() + ",url:"+ request.getServletPath() + "]");
						return false;
					}
					request.getSession(false).removeAttribute("token");
				}
			}
		}
		return true;
	}

	private boolean isRepeatSubmit(HttpServletRequest request) {
		String serverToken = (String) request.getSession(false).getAttribute(
				"token");
		if (serverToken == null) {
			return true;
		}
		String clinetToken = request.getParameter("token");
		if (clinetToken == null) {
			return true;
		}
		if (!serverToken.equals(clinetToken)) {
			return true;
		}
		return false;
	}

}
