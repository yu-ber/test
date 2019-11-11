<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
/* http://localhost:8080/logistics/ */
%>
    
<!DOCTYPE HTML>
<html>
<head>
<!-- 设置页面的 基本路径，页面所有资源引入和页面的跳转全部基于 base路径 -->

<base href="<%=basePath%>">
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<link rel="stylesheet" type="text/css" href="static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css" href="lib/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/style.css" />
<title>添加管理员 - 管理员管理 - H-ui.admin v3.1</title>
</head>
<body>
<article class="page-container">
	<form class="form form-horizontal" id="myForm" action="${empty user?'admin/insert.do':'admin/update.do'}" method="post">
	<!-- 隐藏域 -->
	<input type="hidden" name="userId" value="${user.userId}" />
	<div class="row cl">
		<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>管理员名称：</label>
		<div class="formControls col-xs-8 col-sm-9">
			<input type="text" class="input-text" value="${user.username}" ${empty user?'':'disabled' } placeholder="" id="username" name="username">
		</div>
	</div>
	<div class="row cl">
		<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>真实姓名：</label>
		<div class="formControls col-xs-8 col-sm-9">
			<input type="text" class="input-text" value="${user.realname }" placeholder="" id="realname" name="realname">
		</div>
	</div>
	<div class="row cl">
		<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>初始密码：</label>
		<div class="formControls col-xs-8 col-sm-9">
			<input type="password" class="input-text" autocomplete="off" value="" placeholder="密码" id="password" name="password">
		</div>
	</div>
	<div class="row cl">
		<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>确认密码：</label>
		<div class="formControls col-xs-8 col-sm-9">
			<input type="password" class="input-text" autocomplete="off"  placeholder="确认新密码" id="password2" name="password2">
		</div>
	</div>
	
	
	<div class="row cl">
		<label class="form-label col-xs-4 col-sm-3">角色：</label>
		<div class="formControls col-xs-8 col-sm-9"> <span class="select-box" style="width:150px;">
			<select class="select" name="roleId" size="1">
				<option value="0">请选择角色</option>
				<c:forEach items="${roles}" var="role">
					<option value="${role.roleId}" ${user.roleId eq role.roleId ? 'selected':''}>${role.rolename}</option>
				</c:forEach>
			</select>
			</span> </div>
	</div>
	<!-- <div class="row cl">
		<label class="form-label col-xs-4 col-sm-3">备注：</label>
		<div class="formControls col-xs-8 col-sm-9">
			<textarea name="" cols="" rows="" class="textarea"  placeholder="说点什么...100个字符以内" dragonfly="true" onKeyUp="$.Huitextarealength(this,100)"></textarea>
			<p class="textarea-numberbar"><em class="textarea-length">0</em>/100</p>
		</div>
	</div> -->
	<div class="row cl">
		<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
			<input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
		</div>
	</div>
	</form>
</article>

<!--_footer 作为公共模版分离出去--> 
<script type="text/javascript" src="lib/jquery/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="static/h-ui/js/H-ui.min.js"></script> 
<script type="text/javascript" src="static/h-ui.admin/js/H-ui.admin.js"></script> <!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="lib/jquery.validation/1.14.0/jquery.validate.js"></script> 
<script type="text/javascript" src="lib/jquery.validation/1.14.0/messages_zh.js"></script> 
<script type="text/javascript" src="lib/jquery.validation/1.14.0/validate-methods.js"></script> 
<script type="text/javascript">
$(function(){
	
	$("#myForm").validate({
		rules:{
			username:{
				required:true,
				minlength:4,
				maxlength:16,
				remote:{
					type:"post",
					url:"admin/checkUsername.do",
					data:{username:function(){
						return $("#username").val();
					}},
					dataType:"json"
				}				
			},
			realname:{
				required:true,
				minlength:2,
				isChinese:true
			},
			password:{
				required:true,
				minlength:3
			},
			password2:{
				required:true,
				equalTo: "#password"
			},
			roleId:{
				min:1
			}
		},
		messages:{
			username:{
				required:"账号不能为空",
				minlength:"用户名不能少于4位",
				maxlength:"用户名不能超过16位",
				remote:"该用户名已存在"
			},
			realname:{
				required:"真实姓名不能为空",
				minlength:"真实姓名必须大于2位",
				isChinese:"真实姓名只能为汉字"
			},
			password:{
				required:"密码不能为空",
				minlength:"密码不能少于3位"
			},
			password2:{
				equalTo:"两次密码不一致"
			},
			roleId:"角色不能为空"
			
		},
		submitHandler:function(form){
			/* 
			form : 表单 ，是一个原生js的DOM对象
			
			提交表单思路，使用 Jquery的Ajax操作提交数据，提交成功以后，刷新 bootstrap-table 表格
			
			必须先将 form 原生的js的DOM对象转换成 Jquery对象才能使用Jquery方法
			
			原生DOM对象转Jquery对象
				form ---转--》 $(form)
			Jqeuery对象转原生DOM对象
				$(form) ----转-----》 $(form)[0]
		*/
			$(form).ajaxSubmit(function(data){
				
				//弹出一个提示消息
				layer.msg(data.msg, {time: 1000, icon:data.code},function(){
					
					//删除成功，刷新一下表格
					if(data.code == 1){
						parent.refreshTable();
						parent.layer.closeAll();
					}
				});
				
			})
				
			
		}
	});
});
</script> 
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>