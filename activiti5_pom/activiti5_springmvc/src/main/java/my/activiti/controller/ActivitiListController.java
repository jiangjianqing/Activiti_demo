package my.activiti.controller;

import com.focusight.platform3.controller.UserController;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

@Controller
@RequestMapping("/workflow")
public class ActivitiListController {
	private static final Logger log = LoggerFactory
			.getLogger(UserController.class);

	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private FormService formService;
	
	@RequestMapping(value = "/process-list")
	public ModelAndView getProcessList(){
		ModelAndView mav=new ModelAndView("workflow/process-list");
		List<ProcessDefinition> processDefinitionList=repositoryService.createProcessDefinitionQuery().list();
		mav.addObject("processDefinitionList", processDefinitionList);
		return mav;
	}

	@RequestMapping(value = "/delete-deployment")
	public String deleteProcessDefinition(@RequestParam("deploymentId") String deploymentId){
		System.out.println("deleteProcessDefinition invoked,deploymentId="+deploymentId);
		//注意：这里删除的是一个部署，用spring统一部署的时候会使得N个流程定义都使用一个部署id，导致全部被删除
		repositoryService.deleteDeployment(deploymentId, true);

		return "redirect:/workflow/process-list";
	}

	@RequestMapping(value = "/read-resource")
	public void readResource(@RequestParam("pdid") String processDefinitionId,
							 @RequestParam("resourceName") String resourceName,HttpServletResponse response) throws Exception{
		ProcessDefinitionQuery pdq=repositoryService.createProcessDefinitionQuery();
		ProcessDefinition pd=pdq.processDefinitionId(processDefinitionId).singleResult();
		InputStream resourceAsStream=repositoryService.getResourceAsStream(pd.getDeploymentId(),resourceName);
		byte[] b=new byte[1024];
		int len=-1;
		while ((len=resourceAsStream.read(b,0,1024))!=-1){
			response.getOutputStream().write(b,0,len);
		}
	}

	/***
	 *  文件上传功能，该功能需要在bean配置中添加multipartResolver，并引入commons-fileupload包
	 *  否则会出现下面的错误
  		org.springframework.web.util.NestedServletException: 
  		Request processing failed; nested exception is java.lang.IllegalArgumentException: 
  		Expected MultipartHttpServletRequest: is a MultipartResolver configured?
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/deploy")
	public String deploy(@RequestParam(value="file") MultipartFile file){
		String filenName=file.getOriginalFilename();
		try{
			InputStream fileInputStream=file.getInputStream();

			String extension= FilenameUtils.getExtension(filenName);
			DeploymentBuilder deploymentBuilder=repositoryService.createDeployment();
			if(extension.equals("zip") || extension.equals("bar")){
				ZipInputStream zip=new ZipInputStream(fileInputStream);
				deploymentBuilder.addZipInputStream(zip);
			}else{
				deploymentBuilder.addInputStream(filenName,fileInputStream);
			}
			deploymentBuilder.deploy();
		}catch (Exception ex){
			log.error("error on deploy process,because of file input stream");
		}
		return "redirect:/workflow/process-list";
	}
	
	@RequestMapping(value = "/redeploy/all")
	public String redeployAll() throws Exception{
		return "redirect:/workflow/process-list";
	}
	
	@RequestMapping(value = "/start-process/{processDefinitionId}")
	public ModelAndView readStartForm(@PathVariable("processDefinitionId") String processDefinitionId){
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        boolean hasStartFormKey = processDefinition.hasStartFormKey();
		ModelAndView mav=new ModelAndView("workflow/start-process-form");
		
		// 根据是否有formkey属性判断使用哪个展示层
		// 判断是否有formkey属性
        if (hasStartFormKey) {
            Object renderedStartForm = formService.getRenderedStartForm(processDefinitionId);
            mav.addObject("startFormData", renderedStartForm);
            mav.addObject("processDefinition", processDefinition);
        } else { // 动态表单字段
            StartFormData startFormData = formService.getStartFormData(processDefinitionId);
            mav.addObject("startFormData", startFormData);
        }
        mav.addObject("hasStartFormKey", hasStartFormKey);
        mav.addObject("processDefinitionId", processDefinitionId);
		return mav;
	}
}
