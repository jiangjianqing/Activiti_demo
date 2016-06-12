package my.drools;

import org.kie.api.KieBaseConfiguration;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import com.sample.DroolsTest.Message;

/**
 * 该方法展示的是drools5.x时的创建过程，已标记为deprecated
 * @author ztxs
 *
 */
public class Drools5_Test {

	public static final void main(String[] args) {
		try {

			// load up the knowledge base
			KnowledgeBase kbase = readKnowledgeBase();
			StatefulKnowledgeSession kSession = kbase
					.newStatefulKnowledgeSession();

			Message message = new Message();
			message.setMessage("Hello World");
			message.setStatus(Message.HELLO);
			kSession.insert(message);
			//代码与rule进行交互必须通过FactHandle
			FactHandle factHandle=kSession.getFactHandle(message);
			kSession.fireAllRules();
			System.out.println("FactHandle test:"+factHandle.toString());
			kSession.dispose();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private static KnowledgeBase readKnowledgeBase() throws Exception {

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();

		kbuilder.add(ResourceFactory.newClassPathResource("rules/Sample.drl"),
				ResourceType.DRL);
		// kbuilder.add(ResourceFactory.newClassPathResource("Nagpur.drl"),
		// ResourceType.DRL);

		KnowledgeBuilderErrors errors = kbuilder.getErrors();

		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}

		KieBaseConfiguration kbConf = KnowledgeBaseFactory
				.newKnowledgeBaseConfiguration();
		kbConf.setProperty("org.drools.sequential", "true");
		
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(kbConf);
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

		return kbase;
	}
}
