##注意事项
###布局使用tiles3还是thymeleaf layout？
tiles用在jsp解析上可以，但html模板还是用thymeleaf,
结论：推荐使用thymeleaf layout

###thymeleaf的布局使用offical layout 还是 layout-dialect?
都可以，但要注意layout-dialect对于thymeleaf3的支持，当前存在警告，部分语法后续版本会失效


##版本关注

servlet-api待升级到4.0

##异常信息归属
###[WARNING] Error page loop /404.html
是由于thymeleaf在处理th:src="@{/js/security/mainframe.js}"时，该文件不存在引起的


需要注意的是监听器不受Spring 容器的管理,所以不能使用注解进行注入,
同理,如果使用Spring Aop 也不能切入,这时可以考虑使用原生AspectJ代替Spring Aop