<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
  <title></title>
  <meta name="description" content="">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/styles/bootstrap.min.css" />

  <script src="<%=request.getContextPath()%>/lib/jquery/jquery.js"></script>
  <script src="<%=request.getContextPath()%>/lib/bootstrap/bootstrap.js"></script>
</head>
<body>
  <div class="container">
    <table class="table">
      <thead>
        <tr>
          <th>流程定义id</th>
          <th>部署id</th>
          <th>流程定义名称</th>
          <th>流程定义key</th>
          <th>版本号</th>
          <th>XML资源名称</th>
          <th>图片资源名称</th>
        </tr>
      </thead>
      <tbody>
      <c:forEach items="${processDefinitionList}" var="pd">
        <tr>
          <td>${pd.id}</td>
          <td>${pd.deploymentId}</td>
          <td>${pd.name}</td>
          <td>${pd.key}</td>
          <td>${pd.version}</td>
          <td>${pd.resourceName}</td>
          <td>${pd.diagramResourceName}</td>
        </tr>
      </c:forEach>
      </tbody>

    </table>
  </div>
</body>
</html>
