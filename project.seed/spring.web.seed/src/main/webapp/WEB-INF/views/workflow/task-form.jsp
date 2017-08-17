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

  <title>task-form</title>
  <meta name="description" content="">

</head>
<body>
  <div class="container">
      <c:if test="${not empty message}">
          <div id="message" class="alert alert-success">${message}</div>
          <script>
              setTimeout(function(){
                  $("#message").hide("slow");
              },5000);
          </script>
      </c:if>
      <%-- taskFormData.task和task指向了同一个对象 --%>
      <h3>任务办理-[${task.name}]，流程定义ID：[${task.processDefinitionId}]
      </h3>
      <hr/>
      <form action="${ctx}/workflow/complete-task/${task.id}" class="form-horizontal" method="post">
          <c:if test="${fn:length(task.formKey)>0}">
              ${taskFormData}
          </c:if>
          <c:if test="${fn:length(task.formKey)==0}">
          <c:forEach items="${taskFormData.formProperties}" var="fp">
              <c:set var="fpo" value="${fp}"/>
              <c:set var="readonly" value="${fp.writable?'':'readonly'}"/>
              <c:set var="disabled" value="${fp.writable?'':'disabled'}"/>
              <c:set var="required" value="${fp.required?'required':''}"/>
              <%
                  // 把需要获取的属性读取并设置到pageContext域
                  FormType type = ((FormProperty)pageContext.getAttribute("fpo")).getType();
                  //这里获取了日期模式和enum的formvalues两个数据
                  String[] keys = {"datePattern", "values"};

                  for (String key: keys) {
                      pageContext.setAttribute(key, type.getInformation(key));
                  }
              %>
              <div class="form-group">
                  <label class="col-md-2 control-label" for="${fp.id}">${fp.name}:</label>
                  <div class="col-md-5">
                  <c:choose>
                      <c:when test="${fp.type.name=='string' || fp.type.name=='long'}">
                          <input type="text" id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" value="${fp.value}" class="form-control"  ${disabled}>
                      </c:when>
                      <c:when test="${fp.type.name=='date'}">
                          <div  class="input-group date form_date col-md-5"  data-date="${fp.value}" data-type="${fp.type.name}" data-date-format="${fn:toLowerCase(datePattern)}"  data-link-field="dtp_input2_${fp.id}" data-link-format="yyyy-mm-dd">
                              <input id="${fp.id}" name="${fp.id}" ${disabled} class="form-control" size="16" type="text" value="${fp.value}" >
                              <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                              <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                          </div>
                          <input type="hidden" id="dtp_input2_${fp.id}" value="" /><br/>
                      </c:when>
                      <c:when test="${fp.type.name=='enum'}">
                          <select id="${fp.id}" name="${fp.id}" ${disabled} ${require} class="form-control">
                              <c:forEach items="${values}" var="item">
                              <option value="${item.key}" <c:if test="${item.value==fp.value}">selected</c:if> > ${item.value}</option>
                              </c:forEach>
                          </select>
                      </c:when>
                      <c:when test="${fp.type.name=='javascript'}">
                          <script>${fp.value};</script>
                      </c:when>
                      <c:otherwise>
                          <span class="label label-danger">${fp.id} 该字段没有被处理</span>
                      </c:otherwise>
                  </c:choose>
                  </div>
              </div>
          </c:forEach>
          </c:if>
          <%-- 按钮区域 --%>
          <div class="control-box">
              <div class="controls-pane">
                  <a href="javascript:history.back();" class="btn"><i class="icon-backward"></i>返回列表</a>
                  <button type="submit" class="btn"><i class="icon-ok"></i>完成任务</button>
              </div>
          </div>
      </form>
  </div>
</body>

<script type="text/javascript">

    jQuery(document).ready(function($){
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
        $("input:disabled").parents(".form_date").find(".input-group-addon").hide();
    });

</script>
</html>
