package common.web.controller;

import common.service.utils.AbstractHelperClass;

/**
 * 所有Rest控制器的基类，额外还能为@ControllerAdvice指定basePackageClasses参数，方便识别
 * 
 * 没有使用@RestController是考虑与Spring3.x的兼容
 * @author jjq
 *
 */
public abstract class AbstractRestController extends AbstractHelperClass {

}
