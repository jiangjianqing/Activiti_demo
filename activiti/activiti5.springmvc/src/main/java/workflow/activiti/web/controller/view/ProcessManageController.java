package workflow.activiti.web.controller.view;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import common.web.controller.AbstractViewController;

@Controller
@RequestMapping("workflow/process")
public class ProcessManageController extends AbstractViewController{
	
	@Autowired
	private RepositoryService repositoryService;
	
	@RequestMapping(value="")
	public ModelAndView getList() throws Exception {
		ModelAndView mav = new ModelAndView(getDefaultRequestMappingUrl());
		List<ProcessDefinition> processDefinitionList=repositoryService.createProcessDefinitionQuery().list();
		mav.addObject("processDefinitionList", processDefinitionList);
		return mav;
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
	
	@RequestMapping(value = "/deploy",method=RequestMethod.POST)
	public String deploy(@RequestParam(value="file") CommonsMultipartFile[] uploadFiles ) throws Exception{
		for(CommonsMultipartFile file : uploadFiles) {
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
				logger.error("error on deploy process,because of file input stream");
			}
		}
		return "redirect:/"+getDefaultRequestMappingUrl();
	}
	
	@RequestMapping(value = "/delete-deployment")
	public String deleteProcessDefinition(@RequestParam("deploymentId") String deploymentId) throws Exception{
		logger.debug("deleteProcessDefinition invoked,deploymentId="+deploymentId);
		
		//2017.08.26：一个流程定义不能直接删除，而需要通过流程定义的部署ID删除。
		//注意：这里删除的是一个部署，用spring统一部署的时候会使得N个流程定义都使用一个部署id，导致全部被删除
		repositoryService.deleteDeployment(deploymentId, true);

		return "redirect:/"+getDefaultRequestMappingUrl();
	}
}
