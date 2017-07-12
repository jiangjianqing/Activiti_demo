package drools.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message.Level;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.io.KieResources;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.io.ResourceFactory;

import com.sample.DroolsTest.Message;

public class Drools6_Test {
	private String kieBaseName="MyKBase";
	private String kieSessionName="MyKSession";
	private String packageName="rules";
	private KieContainer kContainer;

	@Before
	public void setUp() throws Exception {
		KieServices kieServices = KieServices.Factory.get();
		KieResources resources = kieServices.getResources();
		//1、先创建KieModuleModel；
		KieModuleModel kieModuleModel = kieServices.newKieModuleModel();
		//2、创建KieBaseModel；
		KieBaseModel baseModel = kieModuleModel.newKieBaseModel(
				kieBaseName).addPackage(packageName);
		//3、创建 KieSessionModel；
		baseModel.newKieSessionModel(kieSessionName);
		
		KieFileSystem fileSystem = kieServices.newKieFileSystem();

		//4、生产一个xml文件，就是kmodule.xml文件；
		String xml = kieModuleModel.toXML();
		System.out.println("以下为代码生成的kmodule信息：");
		System.out.println(xml);
		//5、将kmodulexml文件写入到KieFileSystem中；
		fileSystem.writeKModuleXML(xml);

		//6、将资源写入KieFileSystem
		//20151014,特别注意：这里是将一个物理drl文件 映射为 PathResource中的文件
		
		String drlPackagePath=String.format("%s/Sample.drl",packageName);
		String drlFilePath=String.format("src/main/resources/%s",drlPackagePath);
		//Resource resource=resources.newClassPathResource(drlPackagePath);
		//20151014重点：修改Resource的来源就可以改造为通过db存储 drl规则文件
		Resource resource=resources.newFileSystemResource(drlFilePath);
		//fileSystem.write(drlFilePath,resource);//虽然可用但不实用fileSystem.write参数含义：path（drl物理文件路径），Resource（kmodule中包文件路径）
		fileSystem.write(resource);//推荐用法
		
		//7、通过KieBuilder进行构建就将该kmodule加入到KieRepository中
		//这样就将自定义的kmodule加入到引擎中了，就可以按照之前的方法进行使用了
		KieBuilder kb = kieServices.newKieBuilder(fileSystem);
		kb.buildAll();
		if (kb.getResults().hasMessages(Level.ERROR)) {
			throw new RuntimeException("Build Errors:\n"
					+ kb.getResults().toString());
		}
		
		//8、获取KieContainer
		kContainer = kieServices.newKieContainer(kieServices
				.getRepository().getDefaultReleaseId());
		
		//20151014重点：
		//  getKieClasspathContainer将kmodule.xml文件中的kbase.package映射到了src/main/resources的目录
		//这是与上述获取KieContainer代码的最大差别
		//ks.getKieClasspathContainer();
	}

	@Test
	public void test() {
		assertNotNull(kContainer.getKieBase(kieBaseName));
		KieSession kSession = kContainer.newKieSession(kieSessionName);
		Message message = new Message();
		message.setMessage("Hello World");
		message.setStatus(Message.HELLO);
		kSession.insert(message);
		kSession.fireAllRules();
		
		kSession.dispose();
	}

}
