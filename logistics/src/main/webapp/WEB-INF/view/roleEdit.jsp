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
<!-- <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,role-scalable=no" /> -->
<meta http-equiv="Cache-Control" content="no-siteapp" />
<link rel="stylesheet" type="text/css" href="static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css" href="lib/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/style.css" />
<link rel="stylesheet" type="text/css"  href="lib/zTree/v3/css/zTreeStyle/zTreeStyle.css">

<title>添加角色 - 角色管理 - H-ui.admin v3.1</title>
</head>
<body>
<article class="page-container">
	<form class="form form-horizontal" id="myForm" action="${empty role?'role/insert.do':'role/update.do'}" method="post">
	<!-- 隐藏域 -->
	<input type="hidden" name="roleId" value="${role.roleId}" />
	
	<input type="hidden" name="permissionIds" id="permissionIds" />
	<div class="row cl">
		<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>角色名称：</label>
		<div class="formControls col-xs-8 col-sm-9">
			<input type="text" class="input-text" value="${role.rolename}" ${empty role?'':'disabled' }  placeholder="" id="rolename" name="rolename">
		</div>
	</div>
	<div class="row cl">
		<label class="form-label col-xs-4 col-sm-3">角色介绍：</label>
		<div class="formControls col-xs-8 col-sm-9">
			<textarea name="remark" cols="" rows="" class="textarea"  placeholder="说点什么...100个字符以内" dragonfly="true" >${role.remark}</textarea>
		</div>
	</div>
	<div class="row cl">
		<label class="form-label col-xs-4 col-sm-3">角色权限：</label>
		<div class="formControls col-xs-8 col-sm-9">
			<ul id="permissionTree" class="ztree"></ul>
		</div>
	</div>
	
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
<script type="text/javascript" src="static/h-ui.admin/js/H-ui.admin.js"></script>
<script type="text/javascript" src="lib/zTree/v3/js/jquery.ztree.all-3.5.min.js"></script>

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="lib/jquery.validation/1.14.0/jquery.validate.js"></script> 
<script type="text/javascript" src="lib/jquery.validation/1.14.0/messages_zh.js"></script> 
<script type="text/javascript" src="lib/jquery.validation/1.14.0/validate-methods.js"></script> 
<script type="text/javascript">
$(function(){
	
	$("#myForm").validate({
		rules:{
			rolename:{
				required:true,
				minlength:2,
				remote:{
					type:"post",
					url:"role/checkName.do",
					data:{name:function(){
						return $("#rolename").val();
					}},
					dataType:"json"
				}
			}
		},
		messages:{
			rolename:{
				required:"角色名不能为空",
				remote:"该角色名已存在",
				minlength:"角色名至少为两位",
			}
		},
		submitHandler:function(form){
			getCheckedIds();
			
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

//定义zTree的设置 
var setting = {	
		check: {
			enable: true
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		async: {
			enable: true,
			url:"role/getAllPermission.do",
			dataFilter: filter
		},
		//异步加载完毕以后回调的函数
		callback: {
			onAsyncSuccess: zTreeOnAsyncSuccess
		}
		
};
//过滤数据格式化
function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		for (var i=0, l=childNodes.length; i<l; i++) {
			childNodes[i].id = childNodes[i].permissionId;
			childNodes[i].pId = childNodes[i].parentId;
			childNodes[i].open = true;
		}
		return childNodes;
	}
	
$(function(){
	//初始化zTree
	$.fn.zTree.init($("#permissionTree"), setting);
})

//获取选中的复选框的值
function getCheckedIds(){
	//获取zTree树对象
	var treeObj = $.fn.zTree.getZTreeObj("permissionTree");
	//获取复选框选中的节点
	var nodes = treeObj.getCheckedNodes(true);
	//声明一个数组，添加permissionId
	var permissionIdArr = [];
	for(var i = 0 ;i<nodes.length;i++){
		var node = nodes[i];
		//获取每个数据的id
		var permissionId = node.permissionId;
		//将一个个permissionId添加到数组
		permissionIdArr.push(permissionId);
	}
	/*
	使用js数组的join方法或者toString方法将数组转换成字符串
		var str1 = 数组.join("自定义分隔符")
	*/
	//var permissionIds = permissionIdArr.join(",");
	var permissionIds = permissionIdArr.toString();//1,8,9,20...
	console.log("permissionIds :",permissionIds);
	//将权限的id字符设置到角色标的隐藏域中
	$("#permissionIds").val(permissionIds);
}

function zTreeOnAsyncSuccess(event, treeId, treeNode, msg){
	//获取zTree树对象
	var treeObj = $.fn.zTree.getZTreeObj("permissionTree");
	
	var permissionIds = "${role.permissionIds}";
	var permissionIdArr = permissionIds.split(",");
	for(var i = 0;i<permissionIdArr.length;i++){
		//获取具体的permissionId值
		var permissionId = permissionIdArr[i];
		console.log("permissionId["+i+"]",permissionId);
		//zTree可以根据 指定属性的值获取对应的节点
		//通过zTree数据 id的属性对应的值获取对应的节点
		var node = treeObj.getNodeByParam("id", permissionId, null);
		//console.log(node);
		//让其选中的节点选中
		treeObj.checkNode(node, true, false);
	}
}
</script> 
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>