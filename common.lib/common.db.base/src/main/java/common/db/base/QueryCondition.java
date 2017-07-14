package common.db.base;

import java.io.Serializable;

/**
 * 条件对象
 */
public class QueryCondition implements Serializable,Cloneable{
	private static final long serialVersionUID = 1L;

    /** 字段名（表名.列名） **/
    private String field ;

    /** 操作符 **/
    private String operator ;

    /** 值 **/
    private Object value ;

    public QueryCondition() {
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public QueryCondition(String field , String operator , Object value) {
        this.field = field ;
        this.operator = operator ;
        this.value = value ;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
