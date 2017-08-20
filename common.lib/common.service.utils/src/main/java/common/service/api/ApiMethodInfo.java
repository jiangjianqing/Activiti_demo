package common.service.api;

import java.util.List;

public class ApiMethodInfo {
	private String group;
	private String beanType;
	private String controller;
	private int order;
	private String name;
	private String url;
	private List<ApiMethodParamInfo> params;
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getController() {
		return controller;
	}
	public void setController(String controller) {
		this.controller = controller;
	}
	public String getBeanType() {
		return beanType;
	}
	public void setBeanType(String beanType) {
		this.beanType = beanType;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<ApiMethodParamInfo> getParams() {
		return params;
	}
	public void setParams(List<ApiMethodParamInfo> params) {
		this.params = params;
	}
	
}
