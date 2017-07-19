package common.db.base.page;

import java.util.ArrayList;
import java.util.List;

import common.db.base.exception.OutOfPageRangeException;

public class PageObject<T> {
	private static int defaultPageSize;
	static{
		defaultPageSize=10;
	}
	
	public static void setDefaultPageSize(int pageSize){
		defaultPageSize=pageSize;
	}
	
	public static int getDefaultPageSize(){
		return defaultPageSize;
	}
	
	private PageInfo pageInfo;
	
    private List<T> pageData;

    /**
     * @param dataCount   总数据数
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * 注: 默认分页逻辑 startPoint & endPoint 是oracle实现</br>
     * 如果需要使用到其他数据库请实现PaginationStrategy接口</br>
     * 使用该类的 setPaginationStrategy 方法获得相应的实现</br>
     * @throws OutOfPageRangeException 
     */
    private void initPageObject(int dataCount, int currentPage,int pageSize) throws OutOfPageRangeException{
    	if(pageSize==0)throw new IllegalArgumentException(" 单页数据设置 [pageSize]不能为小于等于 0  当前[pageSize]的值是 : "+pageSize);
    	pageInfo = new PageInfo(dataCount , currentPage , pageSize);
    }
    
    /**
     * @param dataCount   总数据数
     * @param currentPage 当前页
     * 注: 默认分页逻辑 startPoint & endPoint 是oracle实现</br>
     * 如果需要使用到其他数据库请实现PaginationStrategy接口</br>
     * 使用该类的 setPaginationStrategy 方法获得相应的实现</br>
     * @throws OutOfPageRangeException 
     */
    public PageObject(int dataCount, int currentPage) throws OutOfPageRangeException {
    	initPageObject(dataCount,currentPage,defaultPageSize);        
    }
    
    /**
     * @param dataCount   总数据数
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * 注: 默认分页逻辑 startPoint & endPoint 是oracle实现</br>
     * 如果需要使用到其他数据库请实现PaginationStrategy接口</br>
     * 使用该类的 setPaginationStrategy 方法获得相应的实现</br>
     * @throws OutOfPageRangeException 
     */
    public PageObject(int dataCount, int currentPage,int pageSize) throws OutOfPageRangeException {
    	initPageObject(dataCount,currentPage,pageSize);  
    }
    
    /**
     * 设置分页对象集合
     * @return 分页对象集合
     */
    public List<T> getPageData() {
        return pageData;
    } 
    /**
     * 获得分页对象集合
     * @param pageList 分页对象集合
     */
    public void setPageData(List<T> pageList)
    {
        this.pageData =pageList;
    }
    
   
    public PageInfo getPageInfo() {
		return pageInfo;
	}

	@Override
    public String toString() {
        return pageInfo.toString();
    }
}
