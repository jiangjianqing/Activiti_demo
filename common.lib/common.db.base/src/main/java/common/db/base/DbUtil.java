package common.db.base;

import java.util.Collection;

public class DbUtil {
	/**
     * 将集合中的字符串拼接成为SQL语句中 in的形式 'aaa','bbb','ccc'
     * @param values
     * @return
     */
    public static String toSQLIn(Collection<String> values){
        if(values == null || values.isEmpty())
            return null;
         
        String[] strvalues = new String[0];
        strvalues = (String[]) values.toArray(new String[values.size()]);
         
        return toSQLIn(strvalues);
    }
     
    /**
     * 将字符串数组中的字符串拼接成为SQL语句中 in的形式 'aaa','bbb','ccc'
     * @param values
     * @return
     */
    public static String toSQLIn(String[] values){
        StringBuffer bf_sqlin = new StringBuffer();
        if(values == null || values.length == 0)
            return null;
         
        int len = values.length;
        for(int i = 0 ; i < len ; i++){
            bf_sqlin = bf_sqlin.append(", '").append(values[i]).append("' ");
        }
        String str_sqlin = bf_sqlin.substring(1).toString();
         
        return str_sqlin;
    }
    
    public static String toSqlWhere(String condition){
    	if(condition==null) condition="";
        String tmp=condition.trim().toLowerCase();
        if(tmp.length()>0 && !tmp.startsWith("where ")){
        	condition="where "+condition;
        }
        return condition;
    }
}
