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
	
	<title></title>
  	<meta name="description" content="">
</head>
<body>
  <div class="container-fluid">
    <h3 class="page-header">部署流程资源</h3>

    <form action="${ctx}/workflow/deploy" method="post" enctype="multipart/form-data">
      <div class="form-group">
        <label for="exampleInputFile">支持文件格式：zip,bar,bpmn,bpmn20.xml</label>
        <input type="file" id="exampleInputFile" name="file">
        <p class="help-block">Example block-level help text here.</p>
      </div>

      <input type="submit" value="Submit" class="btn btn-primary"/>
    </form>

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
          <th>操作</th>
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
          <td><a target="_blank" href="${ctx}/workflow/read-resource?pdid=${pd.id}&resourceName=${pd.resourceName}">${pd.resourceName}</a></td>
          <td><a target="_blank" href="${ctx}/workflow/read-resource?pdid=${pd.id}&resourceName=${pd.diagramResourceName}">${pd.diagramResourceName}</a></td>
          <td >

          	<a class="btn btn-danger" href="${ctx}/workflow/delete-deployment?deploymentId=${pd.deploymentId}"><i class="glyphicon glyphicon-trash"></i>删除</a>
          	<a class="btn btn-primary" href='${ctx }/workflow/start-process/${pd.id }'><i class="glyphicon glyphicon-play"></i>启动</a>
          	
          </td>
          
        </tr>
      </c:forEach>
      </tbody>

    </table>
  </div>
</body>
</html>
