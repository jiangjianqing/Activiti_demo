package common.web.model.sidebar;

import java.util.List;

public class SimpleSidebarGroup {
	private String name;
	private String title;
	private String graphClasses;
	private String uniqueGroupClass;
	private List<SimpleSidebarItem> items;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGraphClasses() {
		return graphClasses;
	}
	public void setGraphClasses(String graphClasses) {
		this.graphClasses = graphClasses;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<SimpleSidebarItem> getItems() {
		return items;
	}
	public void setItems(List<SimpleSidebarItem> items) {
		this.items = items;
	}
	public String getUniqueGroupClass() {
		return uniqueGroupClass;
	}
	public void setUniqueGroupClass(String uniqueGroupClass) {
		this.uniqueGroupClass = uniqueGroupClass;
	}
	
}
