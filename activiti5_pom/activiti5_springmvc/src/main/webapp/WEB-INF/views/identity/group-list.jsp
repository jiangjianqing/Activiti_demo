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

	<script data-main="<%=request.getContextPath()%>/app/main" src="<%=request.getContextPath()%>/lib/requirejs/require.js"></script>
	<title>Group列表</title>
  	<meta name="description" content="">
</head>
<body>

	<div class="modal fade" id="dlgAddGroup" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">Add Group</h4>
				</div>
				<div class="modal-body">
					<form id="formAddGrop" class="form-horizontal" action="${ctx}/identity/group/add" method="post">
						<div class="form-group">
							<label for="group.id" class="col-sm-2 control-label ">id</label>
							<div class="col-sm-10">
								<input type="text" name="id" id="group.id" placeholder="please input string">
								<span class="help-block">唯一编码（id）</span>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="group.name">name</label>
							<div class="col-sm-10">
								<input type="text" name="name" id="group.name" placeholder="please input string">
								<span class="help-block">用户组名称（name）</span>
							</div>
						</div>
						<div class="form-group">

							<label class="col-sm-2 control-label" >type</label>
							<div class="col-sm-10">
								<div class="radio">
									<label>
										<input type="radio" name="type" value="assignment" checked>
										assignment
									</label>
								</div>
								<div class="radio">
									<label>
										<input type="radio" name="type" value="security-role">
										security-role
									</label>
								</div>
								<span class="help-block">用户组类型</span>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="button" id="createGroup" class="btn btn-primary">Add</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="dlgDelGroup" tabindex="-1" role="dialog" aria-labelledby="delGroupModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="delGroupModalLabel">Delete Group</h4>
				</div>
				<div class="modal-body">
					<form id="formDelGrop" action="abc" method="post"></form>
					<p>确定要删除该用户组吗？</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">No</button>
					<button type="button" id="delGroup" class="btn btn-primary">Yes</button>
				</div>
			</div>
		</div>
	</div>

	<div class="container-fluid">
		<button class="btn btn-primary" data-toggle="modal" data-target="#dlgAddGroup">添加Group</button>
		<table class="table" id="grouplist">
			<caption>Group表</caption>
			<thead>
				<tr>
					<th>id</th>
					<th>name</th>
					<th>type</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${grouplist}" var="group" varStatus="status">
				<tr>
					<td>${group.id}</td>
					<td>${group.name}</td>
					<td>${group.type}</td>
					<td><div class="btn-group"><a class="btn btn-default disabled" ><i class="glyphicon glyphicon-adjust"></i>修改</a><a name="del" href="${ctx}/identity/group/del/${group.id}" class="btn btn-danger"><i class="glyphicon glyphicon-remove"></i>删除</a></div></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
</body>

<script>
	jQuery(document).ready(function ($) {
		$("#dlgAddGroup").on("click",function(event){
			if (event.target.id==="createGroup"){
				console.log("测试 modal");
				$(this).modal('hide');
				$("#formAddGrop").submit();
			}
		})

		function showDelGroupDialog(dtd){
			var dfd= $.Deferred();
			$("#dlgDelGroup").modal("show");
			var btnSelector="#dlgDelGroup button";
			$(btnSelector).on("click",function(event){
				if(event.target.id==="delGroup"){
					dfd.resolve();
				}
				else{
					dfd.reject();
				}
				$(btnSelector).off("click");
				$("#dlgDelGroup").modal("hide");
				return true;
			})
			return dfd.promise();
		}

		var $formDelGrop=$("#formDelGrop");
		$("#grouplist td a[name='del']").on("click",function(event){
			$this=$(this);
			$.when(showDelGroupDialog()).done(function(){
				$formDelGrop.attr("action",$this.attr("href"));
				$formDelGrop.submit();
			});
			return false;
		})
	});
</script>

</html>