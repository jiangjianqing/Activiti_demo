package common.db.base;

import java.util.List;

public class QueryResult<T> {
	private List<T> resultlist;
    private int totalRecord;

    public QueryResult(List<T> resultList,int totalRecord){
    	setData(resultList,totalRecord);
    }
    public List<T> getResultlist() {
        return resultlist;
    }

    private void setResultlist(List<T> resultList) {
        this.resultlist = resultList;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    private void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }
    
    public void setData(List<T> resultList,int totalRecord){
    	setResultlist(resultList);
    	setTotalRecord(totalRecord);
    }
    
}
