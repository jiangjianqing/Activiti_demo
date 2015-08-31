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
	
	<title>User列表</title>
  	<meta name="description" content="">
</head>
<body>

	<div class="modal fade" id="dlgDelUser" tabindex="-1" role="dialog" aria-labelledby="delUserModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="delUserModalLabel">Delete User</h4>
				</div>
				<div class="modal-body">
					<form id="formDel" action="abc" method="post"></form>
					<p>确定要删除该用户吗？</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">No</button>
					<button type="button" id="delUser" class="btn btn-primary">Yes</button>
				</div>
			</div>
		</div>
	</div>

	<div class="container-fluid">
		<button class="btn btn-primary" data-toggle="modal" data-target="#dlgAddUser">添加User</button>
		<table class="table">
			<caption>UserList</caption>
			<thead>
				<tr>
					<th>Id</th>
					<th>FirstName</th>
					<th>LastName</th>
					<th>Email</th>
					<th>Password</th>
					<th>Groups</th>
					<th>Operate</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${userlist}" var="user" varStatus="status">
				<tr data-id="${user.id}">
					<td>${user.id}</td>
					<td>${user.firstName}</td>
					<td>${user.lastName}</td>
					<td>${user.email}</td>
					<td>${user.password}</td>
					<td name="groups"></td>
					<td>
						<div class="btn-group">
							<a name="modify" class="btn btn-default"><i class="glyphicon glyphicon-adjust"></i> 修改</a>
							<a name="del" href="${ctx}/identity/user/del/${user.id}" class="btn btn-danger" ><i class="glyphicon glyphicon-remove"></i>删除</a>
						</div>
						<div class="btn-group">
							<a name="adjustMembership" class="btn btn-default"><i class="glyphicon glyphicon-adjust"></i>Membership</a>
						</div>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>

		<div class="modal fade" id="dlgAddUser" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">Add User</h4>
					</div>
					<div class="modal-body">
						<form id="formAddUser" class="form-horizontal" action="${ctx}/identity/user/add" method="post">
							<div class="form-group">
								<label for="group.id" class="col-sm-2 control-label ">id</label>
								<div class="col-sm-10">
									<input type="text" name="id" id="group.id" placeholder="please input string">
									<span class="help-block">唯一编码（id）</span>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label" for="user.firstName">firstName</label>
								<div class="col-sm-10">
									<input type="text" name="firstName" id="user.firstName" placeholder="please input string">
									<span class="help-block">用户姓（firstName）</span>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label" for="user.lastName">lastName</label>
								<div class="col-sm-10">
									<input type="text" name="lastName" id="user.lastName" placeholder="please input string">
									<span class="help-block">用户名（lastName）</span>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label" for="user.email">email</label>
								<div class="col-sm-10">
									<input type="text" name="email" id="user.email" placeholder="please input string">
									<span class="help-block">邮件地址（email）</span>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label" for="user.password">password</label>
								<div class="col-sm-10">
									<input type="text" name="password" id="user.password" placeholder="please input string">
									<span class="help-block">用户密码（password）</span>
								</div>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						<button type="button" id="createUser" class="btn btn-primary">Add</button>
					</div>
				</div>
			</div>
		</div>

		<div class="modal fade" id="dlgAdjustMembership" tabindex="-1" role="dialog" aria-labelledby="myMembershipModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myMembershipModalLabel">Add User</h4>
					</div>
					<div class="modal-body">
						<form id="formCreateMembership" class="form-horizontal" action="${ctx}/identity/user/addgroup" method="post">
							<input type="hidden" name="userId" value="jjq">
							<div class="checkbox">
								<label>
									<input type="checkbox" name="groupIds" value="deptLeader">detpLeader 管理员
								</label>
							</div>
							<div class="checkbox">
								<label>
									<input type="checkbox" name="groupIds" value="root" checked>root 管理员
								</label>
							</div>

						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						<button type="button" id="createMembership" class="btn btn-primary">Add</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script>
	"use strict"
	jQuery(document).ready(function($){

		function ShowDelUserDialog(){
			var dfd= $.Deferred();
			$("#dlgDelUser").modal("show");
			var btnSelector="#dlgDelUser button";
			$(btnSelector).one("click",function(event){
				if (event.target.id==="delUser"){
					dfd.resolve();
					$("#dlgDelUser").modal("hide");
				}else{
					dfd.reject();
				}
				//$(btnSelector).off("click");20150831:用one代替on

			});
			return dfd.promise();
		}
		$("table a[name='del']").on("click",function(event){
			var $this=$(this);
			$.when(ShowDelUserDialog()).done(function(){
				var $form=$("#dlgDelUser #formDel");
				$form.attr("action",$this.attr("href"));
				$form.submit();
			})
			return false;
		});
		$("table a[name='modify']").on("click",function(event){
			//console.log(this);
			//console.log($(this).parents("tr[data-id]")[0]);
			$.ajax(ctx+"/identity/user/groups/"+$(this).parents("tr[data-id]").data("id"),{
				type:"GET",
				dataType:"json",
				success:function(data, textStatus, jqXHR){
					$.each(data,function(index){
						console.log(this.name);
						console.log(this.type);
					})
					console.log(data);
				}
			});
			console.log(ctx);
			return false;
		});

		var refreshCurrentGroups=function(userId,$group){
			$.post(ctx+"/identity/user/groups/"+userId,function(data){
				var groups="";
				$.each(data,function(index){
					if(groups!=="")
						groups+=",";
					//groups+=(this.name+"["+this["type"]+"]");
					groups+=String.format("{0}[{1},{2}]",this.id,this.name,this["type"]);
				});
				$group.text(groups);
			});
		}

		$("td[name='groups']").each(function(index){
			var $this=$(this);
			var userId=$this.parents("tr[data-id]").data("id");
			refreshCurrentGroups(userId,$this);
		})

		$("#createUser").on("click",function(event){
			$("#formAddUser").submit();
		});

		function ShowAdjustMembershipDialog(){
			var dfd= $.Deferred();
			$("#dlgAdjustMembership").modal("show");
			var btnSelector="#dlgAdjustMembership button";
			$(btnSelector).one("click",function(event){
				if(event.target.id==="createMembership"){
					dfd.resolve();
					$("#dlgAdjustMembership").modal("hide");
				}else{
					dfd.reject();
				}
				//$(btnSelector).off("click");20150831:用one代替on
			});
			return dfd.promise();
		}
		$("table a[name='adjustMembership']").on("click",function(event){
			var $this=$(this);
			var $form=$("#dlgAdjustMembership #formCreateMembership");
			$form.empty();
			//1、填充当前用户
			var $tr=$this.parents("tr[data-id]");
			var userId=$tr.data("id");
			var $tdGroup=$tr.find("td[name='groups']");
			var $hideinput=$(String.format("<input type='hidden' name='userId' value='{0}'>",userId));
			$form.append($hideinput);

			$.get(ctx+"/identity/group/list.json",function(groupdata){
				var template="<div class='checkbox'>\
					<label>\
					<input type='checkbox' name='groupIds' value='{0}'>{0} {1}\
					</label>\
				</div>";
				//2、遍历添加用户组
				$.each(groupdata,function(index){
					var $checkbox=$(template.format(this.id,this["name"]));
					$form.append($checkbox);
				});
				//3、已经在该用户组则设置checked
				$.post(ctx+"/identity/user/groups/"+userId,function(groupdata){
					$.each(groupdata,function(index){
						$form.find(String.format("input[value='{0}']",this.id)).prop("checked","checked");
					});
				});
			});
			$.when(ShowAdjustMembershipDialog()).done(function(){
				$.post($form.prop("action"),$form.serialize(),function(data){
					refreshCurrentGroups(userId,$tdGroup);
					console.log(String.format("服务器返回数据【应该为乱码】：{0}",data));
				})

			})
		})
	});
</script>
</html>