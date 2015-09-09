package my.activiti.bean;

import org.activiti.engine.repository.ProcessDefinition;

public class MyProcessDefinition implements ProcessDefinition {

	private String id;
	private String name;
	private String category;
	private String key;
	private String description;
	private int version;
	private String resourceName;
	private String deploymentId;
	private String diagramResourceName;

	private String tenantId;
	

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public void setDiagramResourceName(String diagramResourceName) {
		this.diagramResourceName = diagramResourceName;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public String getResourceName() {
		return resourceName;
	}

	@Override
	public String getDeploymentId() {
		return deploymentId;
	}

	@Override
	public String getDiagramResourceName() {
		return diagramResourceName;
	}

	@Override
	public boolean hasStartFormKey() {
		return false;
	}

	@Override
	public boolean hasGraphicalNotation() {
		return false;
	}

	@Override
	public boolean isSuspended() {
		return false;
	}

	@Override
	public String getTenantId() {
		return tenantId;
	}

}
