package common.service.utils;

public class RequestMappingMethodInfo {
	public String controllerName;
    public String methodName;
    public String requestType;
    public String requestUrl;
    public Class<?>[] methodParmaTypes;

    public RequestMappingMethodInfo(String requestUrl, String requestType, String controllerName, String requestMethodName, 
    		Class<?>[] methodParmaTypes)
    {
        this.requestUrl = requestUrl;
        this.requestType = requestType;
        this.controllerName = controllerName;
        this.methodName = requestMethodName;
        this.methodParmaTypes = methodParmaTypes;

    }
}
