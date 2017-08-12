package my.activiti.bean.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class ProcessStartExecutionListener implements ExecutionListener {

	@Override
	public void notify(DelegateExecution execution) {
		execution.setVariable("setInStartListener", true);
		System.out.println(this.getClass().getSimpleName()+","+execution.getEventName());
		//getEventName()范围：start，end
	}

}
