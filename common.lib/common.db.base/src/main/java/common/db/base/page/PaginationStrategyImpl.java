package common.db.base.page;

public class PaginationStrategyImpl implements PaginationStrategy {
	private int startPoint;
    private int endPoint;
	@Override
	public int getStartPoint() {
		return startPoint;
	}

	@Override
	public int getEndPoint() {
		return endPoint;
	}

	@Override
	public void setDataPoint(int currentPage, int pageSize) {
		startPoint= (currentPage - 1) * pageSize;
        endPoint = startPoint+pageSize-1;
	}

}
