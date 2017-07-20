package common.db.base;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public interface  AbstractEnum<T> {

	/**
	 * 返回code信息
	 * @return
	 */
	public T getCode();
	
	/**
	 * 返回Enum注释信息
	 * @return
	 */
	public String getDescription();
	
	public default Map<T, String> getCodeAndDescriptions(Class<? extends Enum> cls){
		Map<T , String> alls = new HashMap<T, String>();
		
		Method method = null;
		try {
			method = cls.getMethod("values");
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AbstractEnum[] objs = null;
		try {
			objs = (AbstractEnum[]) method.invoke(null);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (AbstractEnum s : objs){
			alls.put((T) s.getCode(), s.getDescription());
		}
		return alls;
	}
	
}
