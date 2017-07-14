package common.db.base.exception;

public class NoFieldChangedException extends Exception {
	
	private String entityName;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoFieldChangedException(String entityName,String msg){
		super(msg);
		setEntityName(entityName);
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	

}
