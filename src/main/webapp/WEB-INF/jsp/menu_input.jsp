<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="resource.jsp" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />

<link rel="stylesheet" type="text/css" href="${path}/util/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="${path}/util/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css" href="${path}/lib/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css" href="${path}/util/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css" href="${path}/util/h-ui.admin/css/style.css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/static/css/select2.css" />

	<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="${path}/lib/jquery/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="${path}/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${path}/util/h-ui/js/H-ui.min.js"></script> 
<script type="text/javascript" src="${path}/util/h-ui.admin/js/H-ui.admin.js"></script> <!--/_footer /作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${path}/lib/jquery.validation/1.14.0/jquery.validate.js"></script> 
<script type="text/javascript" src="${path}/lib/jquery.validation/1.14.0/validate-methods.js"></script> 
<script type="text/javascript" src="${path}/lib/jquery.validation/1.14.0/messages_zh.js"></script> 
<script type="text/javascript" src="<%=path%>/static/js/select2.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/util/jquery.ztree.core-3.5.js"></script>
<link href="<%=path%>/static/util/easyui-1.4.2/themes/default/easyui.css" rel="stylesheet" />  
<link href="<%=path%>/static/util/easyui-1.4.2/themes/icon.css" rel="stylesheet" /> 
<script src="<%=path%>/static/util/easyui-1.4.2/jquery.easyui.min.js" type="text/javascript"></script>
<script src="<%=path%>/static/util/easyui-1.4.2/locale/easyui-lang-zh_CN.js" type="text/javascript"></script> 


<title>编辑菜单</title>
</head>
<script type="text/javascript">
$(function(){	
	//loadParent();
	
	$(":radio[name='status'][value='${menu.status }']").prop("checked", "checked");
	$("select[name='_parentId'] option[value='${menu._parentId}']").prop("selected", "true");
	
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	
	$("#menu_form").validate({
		rules:{
			name:{
				required:true
			}
		},
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
            $.post('_save.do', $("#menu_form").serialize(), function (data) {
				if (data.code == 1) {
					parent.layer.msg(data.msg,{icon:1,time:5000});
					parent.table.ajax.reload();
					var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
					parent.layer.close(index);  // 关闭layer
				}else{
					layer.msg(data.msg,{icon:2,time:5000});
				}
            }, 'json');
		}
	});
});

/* function loadParent(){
	$('#_parentId').combotree({
        url:'parentMenu.do?selectType=select',
	    multiple:false,//是否可多选
	    editable:false,//是否可编辑
        height:28,
	    width:260,
        valueField:'id',
        onBeforeLoad:function(node,param){
            param.id = "${model.id}";
        }
	});
	$('#_parentId').combotree('setValue','${menu._parentId }');
} */
</script> 

<body>
<article class="page-container">
	<form class="form form-horizontal" id="menu_form">
		<input type="hidden" id="id" name="id" value="${menu.id}"/>
		<input type="hidden" id="functionType" name="functionType" value="0"/>
		<input type="hidden" id="type" name="type" value="0"/>
		<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3"> 上级菜单：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<span class="select-box">
					 <select name="_parentId"  id="_parentId" class="select">
					         <option selected value="">请选择</option>
							<c:forEach items="${menus}" var="menu">
							  <option  value="${menu.id}">${menu.name}</option>
							</c:forEach>
					 </select>
					</span>
				</div>
		</div>
				<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>菜单名称：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value="${menu.name}" placeholder="菜单名称" id="name" name="name">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">url地址：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value="${menu.url}" placeholder="url" id="url" name="url">
			</div>
		</div>
		
	  <div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">图标代码：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value='${menu.icon.replaceAll("&","&amp")}' placeholder="图标" id="icon" name="icon">
			</div>
		</div>
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">状态：</label>
			<div class="formControls col-xs-8 col-sm-9 skin-minimal">
				<div class="radio-box">
					<input name="status" type="radio" id="sex-1" value="1" checked>
					<label for="sex-1">启用</label>
				</div>
				<div class="radio-box">
					<input type="radio" id="sex-2" value="0" name="status">
					<label for="sex-2">停用</label>
				</div>
			</div>
		</div>
		
		<div class="row cl">
			<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
				<input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
			</div>
		</div>
	</form>
</article>
</body>
</html>