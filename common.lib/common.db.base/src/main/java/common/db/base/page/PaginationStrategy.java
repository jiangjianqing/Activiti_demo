package common.db.base.page;

public interface PaginationStrategy {
	/**
     * 获取数据起始点
     * @return
     */
    public int getStartPoint();
    /**
     * 获取数据结束点
     * @return
     */
    public int getEndPoint();
    /**
     * 设置起分页始点和结束点，注意：该数据与具体数据的ID毫无关系，其只是表示了当前数据在总集合中的范围，具体公式为：PageSize=EndPoint-StartPoint+1
     * @param currentPage
     * @param pageSize
     */
    public void setDataPoint(int currentPage,int pageSize);
}
