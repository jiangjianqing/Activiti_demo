package common.web.model;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;

public class RequestParamNaming  implements Serializable{

	@Value("${naming.requestParam.page}")
	private String page;
	
	@Value("${naming.requestParam.pageSize}")
	private String pageSize;
	
	@Value("${naming.requestParam.querySort}")
	private String querySort;
	
	@Value("${naming.requestParam.queryOrder}")
	private String queryOrder;

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getQuerySort() {
		return querySort;
	}

	public void setQuerySort(String querySort) {
		this.querySort = querySort;
	}

	public String getQueryOrder() {
		return queryOrder;
	}

	public void setQueryOrder(String queryOrder) {
		this.queryOrder = queryOrder;
	}
}
