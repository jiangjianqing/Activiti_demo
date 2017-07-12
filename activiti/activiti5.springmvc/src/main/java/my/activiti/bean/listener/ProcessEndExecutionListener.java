package my.activiti.bean.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class ProcessEndExecutionListener implements ExecutionListener {

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		execution.setVariable("setInEndListener", true);
		System.out.println(this.getClass().getSimpleName()+","+execution.getEventName());
	}

}
