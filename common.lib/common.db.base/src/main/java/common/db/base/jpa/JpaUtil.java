package common.db.base.jpa;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Query;

public class JpaUtil {
	/**
	 * Entity的默认别名
	 */
	private static String defaultAlias="o";
	
	public static String getDefaultAlias() {
		return defaultAlias;
	}

	public static void setDefaultAlias(String alias) {
		JpaUtil.defaultAlias = alias;
	}

	/**
     * 获取实体的名称
     * 
     * @param <T>
     * @param entityClass
     *            实体类
     * @return
     */
    public static <T> String getEntityName(Class<T> entityClass) {
    	//20160128改用短名称
    	//String entityname = entityClass.getName();
        String entityname = entityClass.getSimpleName();
        Entity entity = entityClass.getAnnotation(Entity.class);
        if (entity.name() != null && !"".equals(entity.name())) {
            entityname = entity.name();
        }
        return entityname;
    }
     
    /**
     * 创建Select后所要查询的字段名称字符串
     * @param fields 
     *          需要查询的字段
     * @param alias  
     *          表的别名
     * @return
     *          拼接成的字段名字符串
     */
    public static String buildSelect(String[] fields, String alias) {
    	if(alias==null || alias.trim().isEmpty())
    		alias=getDefaultAlias();
        StringBuffer sf_select = new StringBuffer("SELECT");
        if (fields!=null && fields.length>0){
        	for (String field : fields) {
                sf_select.append(" ").append(alias).append(".").append(field)
                        .append(",");
            }
        }else{
        	sf_select.append(" ").append(alias).append(" ");
        }
        
        return (sf_select.substring(0, sf_select.length() - 1)).toString();
    }
    
    public static <T> String buildSelect(Class<T> clazz,String[] fields, String alias) {
    	if(alias==null || alias.trim().isEmpty())
    		alias=getDefaultAlias();
    	String ret=String.format(" %s from %s %s ", buildSelect(fields,alias),JpaUtil.getEntityName(clazz),alias);    	
        return ret;
    }
     
    /**
     * 创建Select后所要查询的字段名称字符串，并作为实体类的构造函数
     * @param fields
     * @param alias
     * @return
     */
    public static String buildSelect(String className,String[] fields, String alias) {
    	if(alias==null || alias.trim().isEmpty())
    		alias=getDefaultAlias();
        StringBuffer sf_select = new StringBuffer("SELECT new ").append(className).append("(");
        for (String field : fields) {
            sf_select.append(" ").append(alias).append(".").append(field)
                    .append(",");
        }
        return (sf_select.substring(0, sf_select.length() - 1))+")";
    }
     
     
    /**
     * 组装order by语句
     * 
     * @param orderby
     *      列名为key ,排序顺序为value的map
     * @return
     *      Order By 子句
     */
    public static String buildOrderby(LinkedHashMap<String, String> orderby,String alias) {
    	if(alias==null || alias.trim().isEmpty())
    		alias=getDefaultAlias();
        StringBuffer orderbyql = new StringBuffer("");
        if (orderby != null && orderby.size() > 0) {
            orderbyql.append(" order by ");
            for (String key : orderby.keySet()) {
                orderbyql.append(alias).append(".").append(key).append(" ").append(
                        orderby.get(key)).append(",");
            }
            orderbyql.deleteCharAt(orderbyql.length() - 1);//删除最后一个多余的“，”
        }
        return orderbyql.toString();
    }
    /**
     * 组装order by语句
     * @param orderby
     * @return
     */
    public static String buildOrderby(LinkedHashMap<String, String> orderby){
    	return buildOrderby(orderby,null);
    }
    
    public static<T> String getPkFieldWithoutAlias(Class<T> clazz){
    	String out = "";
        try {
        	Field[] fields =  clazz.getDeclaredFields();
        	for(Field f : fields){
        	      //String filedName = f.getName();
        	      //System.out.println("属性名称:【"+filedName+"】");

        	      //1、获取属性上的指定类型的注释
        	      Annotation annotation = f.getAnnotation(Id.class);
        	      
        	      //有该类型的注释存在
        	      if (annotation!=null) {
        	    	  out = f.getName();
        	    	  return out;
        	        //强制转化为相应的注释	
        	        //XmlElement xmlElement = (XmlElement)annotation;
        	        //3、获取属性上的指定类型的注释的指定方法
        	        //具体是不是默认值可以去查看源代码
        	        //if (xmlElement.name().equals("##default")) {
        	        //  System.out.println("属性【"+filedName+"】注释使用的name是默认值: "+xmlElement.name());
        	        //}else {
        	        //  System.out.println("属性【"+filedName+"】注释使用的name是自定义的值: "+xmlElement.name());
        	        //}
        	      }
        	}
            PropertyDescriptor[] propertyDescriptors = Introspector
                    .getBeanInfo(clazz).getPropertyDescriptors();
            for (PropertyDescriptor propertydesc : propertyDescriptors) {
            	propertydesc.getPropertyType();
                Method method = propertydesc.getReadMethod();
                if (method != null && method.isAnnotationPresent(Id.class)) {
//                  PropertyDescriptor[] ps = Introspector.getBeanInfo(
//                          propertydesc.getPropertyType())
//                          .getPropertyDescriptors();
                    out =  propertydesc.getName();
//                          + "."
//                          + (!ps[1].getName().equals("class") ? ps[1]
//                                  .getName() : ps[0].getName()
//                                  );
                    return out;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if("".equals(out))
        	throw new RuntimeException("entity上没有标注id，无法获取primarykey field");
        return out;
    }
     
    /**
     * 得到Count聚合查询的聚合字段,既是主键列
     * @param <T>
     *              实体类型
     * @param clazz     
     *              实体类
     * @param alias
     *              表别名
     * @return
     *              聚合字段名(主键名)
     */
    public static <T> String getPkField(Class<T> clazz, String alias) {
    	if(alias==null || alias.trim().isEmpty())
    		alias=getDefaultAlias();
        return alias+"."+getPkFieldWithoutAlias(clazz);
    }
    /**
     * 获取主键
     * @param clazz
     * @return
     */
    public static <T> String getPkField(Class<T> clazz){
    	return getPkField(clazz,null);
    }
     
    /**
     * 设置查询参数，支持位置参数和命名参数两种
     * @param query 
     *          查询
     * @param queryParams
     *          查询参数
     */
    public static Query setQueryParams(Query query, Map queryParams) {
        if (queryParams != null) {
        	/*20170714 移除位置参数功能
            if (queryParams instanceof Object[]) {
                Object[] params = (Object[]) queryParams;
                if (params.length > 0) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i + 1, params[i]);
                    }
                }
            } else if (queryParams instanceof Map) {
                Map params = (Map) queryParams;
                Iterator<String> it = params.keySet().iterator();
                while(it.hasNext()){
                    String key = it.next();
                    query.setParameter(key, params.get(key));
                }
            }else{
            	throw new RuntimeException("无效的queryParams类型，只支持Object[]和map");
            }*/
        	
            Iterator<String> it = queryParams.keySet().iterator();
            while(it.hasNext()){
                String key = it.next();
                query.setParameter(key, queryParams.get(key));
            }
        }
        return query;
    }
     
}
