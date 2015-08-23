package my.activiti.bean.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;

public class TaskCreateListener implements TaskListener {

	//Expression字段的set方法可以忽略，如果没有set方法，Activiti引擎会通过反射机制设置变量（即设置访问权限为private的字段）
	private Expression content;//表达式注入字段的类型必须为Expression
	private Expression task;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		System.out.println(task.getValue(delegateTask));
		delegateTask.setVariable("setInTaskCreate", delegateTask.getEventName()+","+content.getValue(delegateTask));
		System.out.println(delegateTask.getEventName()+",任务分配给:"+delegateTask.getAssignee());
		//getEventName()范围：create，assignment，complete
		delegateTask.setAssignee("jenny");//任务重新分配
	}

}
