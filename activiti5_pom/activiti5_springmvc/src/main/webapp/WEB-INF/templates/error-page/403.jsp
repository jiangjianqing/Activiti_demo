<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
<title>403 错误</title>  
</head>  
<body>  
 	<h1>HTTP Status 403 - Access is denied</h1>

    <c:choose>
        <c:when test="${empty username}">
            <h2>You do not have permission to access this page!</h2>
        </c:when>
        <c:otherwise>
            <h2>
                Username : ${username} <br /> You do not have permission to access
                this page!
            </h2>
        </c:otherwise>
    </c:choose>
</body>  
</html>