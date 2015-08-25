<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.form.*, org.apache.commons.lang3.*" %>
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
	<div class="container">
		<c:if test="${not empty message}">
			<div id="message" class="alert alert-success">${message}</div>
			<!-- 自动隐藏提示信息 -->
			<script type="text/javascript">
			setTimeout(function() {
				$('#message').hide('slow');
			}, 5000);
		</script>
		</c:if>
		<h3>启动流程—
			<c:if test="${hasStartFormKey}">
				[${processDefinition.name}]，版本号：${processDefinition.version}
			</c:if>
			<c:if test="${!hasStartFormKey}">
				[${startFormData.processDefinition.name}]，版本号：${startFormData.processDefinition.version}
			</c:if>
		</h3>
		<hr/>
		<form action="${ctx }/chapter6/process-instance/start/${processDefinitionId}" class="form-horizontal" method="post">
			<c:if test="${hasStartFormKey}">
			${startFormData}
			</c:if>
	
			<c:if test="${!hasStartFormKey}">
				<c:forEach items="${startFormData.formProperties}" var="fp">
					<c:set var="fpo" value="${fp}"/>
					<%
						// 把需要获取的属性读取并设置到pageContext域
						FormType type = ((FormProperty)pageContext.getAttribute("fpo")).getType();
						String[] keys = {"datePattern"};
						for (String key: keys) {
							pageContext.setAttribute(key, ObjectUtils.toString(type.getInformation(key)));
						}
					%>
					<div class="control-group">
						<label class="col-md-2  control-label" for="${fp.id}">${fp.name}:</label>
					<%-- 文本或者数字类型 --%>
					<c:if test="${fp.type.name == 'string' || fp.type.name == 'long'}">					
						<div class="controls">
							<input type="text" id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" value="" />
						</div>
					</c:if>
	
					<%-- 日期 --%>
					<c:if test="${fp.type.name == 'date'}">
					
						<div class="form-group">
			                
			                <div  class="input-group date form_date col-md-5" data-date="" data-type="${fp.type.name}" data-date-format="${fn:toLowerCase(datePattern)}"  data-link-field="dtp_input2" data-link-format="yyyy-mm-dd">
			                    <input id="${fp.id}" name="${fp.id}" class="form-control" size="16" type="text" value="" >
			                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
								<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
			                </div>
							<input type="hidden" id="dtp_input2" value="" /><br/>
			            </div>
			            <%-- 
						<div class="controls">
							<input type="text" id="${fp.id}" name="${fp.id}" class="datepicker"  data-type="${fp.type.name}" data-date-format="${fn:toLowerCase(datePattern)}" />
						</div>
						--%>
					</c:if>
	
					<%-- Javascript --%>
					<c:if test="${fp.type.name == 'javascript'}">
						<script type="text/javascript">${fp.value};</script>
					</c:if>
					</div>
				</c:forEach>
			</c:if>
	
			<%-- 按钮区域 --%>
			<div class="control-group">
				<div class="controls">
					<a href="javascript:history.back();" class="btn"><i class="icon-backward"></i>返回列表</a>
					<button type="submit" class="btn"><i class="icon-play"></i>启动流程</button>
				</div>
			</div>
		</form>
	</div>
	
	<script type="text/javascript">
	
	$(document).ready(function(){
		$('.form_date').datetimepicker({
	        //language:  'fr',
	        weekStart: 1,
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			minView: 2,
			forceParse: 0
	    });
	});
	
	</script>
</body>
</html>