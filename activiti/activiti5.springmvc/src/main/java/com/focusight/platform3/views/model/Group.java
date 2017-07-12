package com.focusight.platform3.views.model;

public class Group implements org.activiti.engine.identity.Group {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String type;
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setId(String arg0) {
		id=arg0;

	}

	@Override
	public void setName(String arg0) {
		name=arg0;

	}

	@Override
	public void setType(String arg0) {
		type=arg0;

	}

}
