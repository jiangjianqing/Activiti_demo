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

	private static String defaultAlias="o";

	public static String getDefaultAlias() {
		return defaultAlias;
	}

	public static void setDefaultAlias(String alias) {
		JpaUtil.defaultAlias = alias;
	}

    public static <T> String getEntityName(Class<T> entityClass) {

        String entityname = entityClass.getSimpleName();
        Entity entity = entityClass.getAnnotation(Entity.class);
        if (entity.name() != null && !"".equals(entity.name())) {
            entityname = entity.name();
        }
        return entityname;
    }

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
            orderbyql.deleteCharAt(orderbyql.length() - 1);
        }
        return orderbyql.toString();
    }

    public static String buildOrderby(LinkedHashMap<String, String> orderby){
    	return buildOrderby(orderby,null);
    }

    public static<T> String getPkFieldWithoutAlias(Class<T> clazz){
    	String out = "";
        try {
        	Field[] fields =  clazz.getDeclaredFields();
        	for(Field f : fields){

        	      Annotation annotation = f.getAnnotation(Id.class);

        	      if (annotation!=null) {
        	    	  out = f.getName();
        	    	  return out;

        	      }
        	}
            PropertyDescriptor[] propertyDescriptors = Introspector
                    .getBeanInfo(clazz).getPropertyDescriptors();
            for (PropertyDescriptor propertydesc : propertyDescriptors) {
            	propertydesc.getPropertyType();
                Method method = propertydesc.getReadMethod();
                if (method != null && method.isAnnotationPresent(Id.class)) {

                    out =  propertydesc.getName();

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

    public static <T> String getPkField(Class<T> clazz, String alias) {
    	if(alias==null || alias.trim().isEmpty())
    		alias=getDefaultAlias();
        return alias+"."+getPkFieldWithoutAlias(clazz);
    }

    public static <T> String getPkField(Class<T> clazz){
    	return getPkField(clazz,null);
    }

    public static Query setQueryParams(Query query, Object queryParams) {
        if (queryParams != null) {
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
            }
        }
        return query;
    }

}

