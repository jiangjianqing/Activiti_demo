package common.service.utils;

/**
 * 回调接口：只用于通过匿名类直接new接口的场合
 * @author jjq
 *
 */
public interface CallBack {
	/**  
     * 执行回调方法  
     * @param objects   将处理后的结果作为参数返回给回调方法  
     */    
    public void execute(Object... objects );

}
