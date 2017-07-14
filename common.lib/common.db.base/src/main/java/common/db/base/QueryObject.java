package common.db.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询对象，用于前后台传输查询条件。 拼接查询语句并返回参数
 */
public class QueryObject implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 条件列表 **/
	private List<QueryCondition> queryConditions;

	/** 参数列表 **/
	private Object[] queryParams;

	/** where语句 **/
	private String whereQL;

	/**
	 * 添加查询条件
	 * 
	 * @author slx
	 * @date 2009-6-24 上午11:18:16
	 * @modifyNote
	 * @param field
	 *            字段名
	 * @param operator
	 *            操作符
	 * @param value
	 *            值
	 */
	public void addCondition(String field, String operator, Object value) {
		addCondition(new QueryCondition(field, operator, value));
	}

	/**
	 * 添加查询条件
	 * 
	 * @author slx
	 * @date 2009-6-24 上午11:19:03
	 * @modifyNote
	 * @param condition
	 *            条件对象
	 */
	public void addCondition(QueryCondition condition) {
		getQueryConditions().add(condition);
	}

	/**
	 * 设置条件列表
	 * 
	 * @author slx
	 * @date 2009-6-24 上午11:19:41
	 * @modifyNote
	 * @param queryConditions
	 */
	public void setQueryConditions(List<QueryCondition> queryConditions) {
		this.queryConditions = queryConditions;
	}

	public List<QueryCondition> getQueryConditions() {
		if (queryConditions == null) {
			queryConditions = new ArrayList<QueryCondition>();
		}
		return queryConditions;
	}

	/**
	 * 得到where语句 <br>
	 * table.field1 = ? AND table.field2 = ?
	 * 
	 * @author slx
	 * @date 2009-6-24 上午11:20:17
	 * @modifyNote
	 * @return
	 */
	public String getWhereQL() {
		buildQuery();
		return whereQL;
	}

	/**
	 * 得到参数列表
	 * 
	 * @author slx
	 * @date 2009-6-24 上午11:03:54
	 * @modifyNote
	 * @return
	 */
	public Object[] getQueryParams() {
		buildQuery();
		return queryParams;
	}

	/**
	 * 构造查询，初始化where和params
	 * 
	 * @author slx
	 * @date 2009-6-24 上午11:31:08
	 * @modifyNote
	 */
	protected void buildQuery() {
		StringBuffer sf_where = new StringBuffer("");
		int size = getQueryConditions().size();
		queryParams = new Object[size];

		for (int i = 0; i < size; i++) {
			QueryCondition condition = getQueryConditions().get(i);
			if (condition.getValue() == null || condition.getValue().toString().trim().equals("")) {
				continue;
			}
			sf_where.append(" AND ").append(condition.getField()).append(" ").append(condition.getOperator())
					.append(" ? ");
			queryParams[i] = condition.getValue();
		}
		whereQL = sf_where.toString();
		whereQL = whereQL.replaceFirst("AND", "");
	}

	public QueryCondition findQueryCondition(String field, String operator) {

		for (QueryCondition queryCondition : queryConditions) {
			if (field.equals(queryCondition.getField()) && operator.equals(queryCondition.getOperator())) {
				return queryCondition;
			}
		}
		return null;
	}
}
