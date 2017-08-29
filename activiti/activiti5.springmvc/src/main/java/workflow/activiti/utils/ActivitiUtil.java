package workflow.activiti.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.FormService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.springframework.web.bind.annotation.PathVariable;

import common.service.utils.SpringContextHolder;

public class ActivitiUtil {
	/**
	 * 根据输入的参数生成提交数据，process和task通用
	 * @param hasFormKey
	 * @param formProperties
	 * @param request
	 * @return
	 */
	public static Map<String,String> generateFormValueMap(boolean hasFormKey,List<FormProperty> formProperties,HttpServletRequest request){
		Map<String,String> formValues=new HashMap<String,String>();
		if(hasFormKey){//外置表单
			Map<String,String[]> parameterMap=request.getParameterMap();
			Set<Entry<String,String[]>> entrySet=parameterMap.entrySet();
			for(Entry<String,String[]> entry:entrySet){
				String key=entry.getKey();
				formValues.put(key, entry.getValue()[0]);
			}
		}else{//动态表单
			for(FormProperty prop:formProperties){
				if(prop.isWritable()){
					String value=request.getParameter(prop.getId());
					formValues.put(prop.getId(), value);
				}
			}
		}	
		return formValues;
	}
	
	public static void completeTask(String taskId,HttpServletRequest request) {
		FormService formService= SpringContextHolder.getBean(FormService.class);
		TaskFormData taskFormData=formService.getTaskFormData(taskId);
		boolean hasFormKey=taskFormData.getFormKey()!=null && taskFormData.getFormKey().length()>0;
		List<FormProperty> formProperties=taskFormData.getFormProperties();
		Map<String,String> formValues=ActivitiUtil.generateFormValueMap(hasFormKey,formProperties,request);//生成提交数据
		formService.submitTaskFormData(taskId, formValues);
	}
}
