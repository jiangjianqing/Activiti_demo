<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<html>
<head>
	<script>
	<%--  
	    var notLogon = ${empty user};
	    if (notLogon) {
	      location.href = '${ctx}/login.jsp?timeout=true';
	    }
	    --%>
	    <!-- 如果没有登录 ，则转向登录界面 -->
    </script>
	<%@ include file="/common/meta.jsp"%>
	<%@ include file="/common/include-base-files.jsp"%>

	<script data-main="<%=request.getContextPath()%>/app/main-${type}" src="<%=request.getContextPath()%>/lib/requirejs/require.js"></script>
	<title>Group列表</title>
  	<meta name="description" content="">
</head>
<body id="appView">

</body>

</html>