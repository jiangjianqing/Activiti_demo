package my.activiti.bean.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class TaskAssigneeListener implements TaskListener {

	
	@Override
	public void notify(DelegateTask delegateTask) {
		System.out.println("TaskAssigneeListener,任务分配给："+delegateTask.getAssignee());

	}

}
