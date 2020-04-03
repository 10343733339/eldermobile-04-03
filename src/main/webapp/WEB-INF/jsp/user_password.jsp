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

	<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="${path}/lib/jquery/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="${path}/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${path}/util/h-ui/js/H-ui.min.js"></script> 
<script type="text/javascript" src="${path}/util/h-ui.admin/js/H-ui.admin.js"></script> <!--/_footer /作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${path}/lib/jquery.validation/1.14.0/jquery.validate.js"></script> 
<script type="text/javascript" src="${path}/lib/jquery.validation/1.14.0/validate-methods.js"></script> 
<script type="text/javascript" src="${path}/lib/jquery.validation/1.14.0/messages_zh.js"></script> 
<script type="text/javascript">


$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	
	$("#form-admin-add").validate({
		rules:{
			newPassword:{
				required:true,
				rangelength:[5,10],
			},
			password2:{
				required:true,
				equalTo: "#newPassword",
				rangelength:[5,10],
			},
		},
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
            $.post('updateUserPassword.do', $("#form-admin-add").serialize(), function (data) {
				if (data.code == 1) {
					parent.layer.msg(data.msg,{icon:1,time:5000});
					//parent.table.ajax.reload();
					var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
					parent.layer.close(index);  // 关闭layer
				}else{
					layer.msg(data.msg,{icon:2,time:5000});
				}
            }, 'json');
		}
	});
});
</script> 

<body>
<article class="page-container">
		<form class="form form-horizontal" id="form-admin-add">
			<input type="hidden" id="id" name="id" value="${user.id}" />
			<div class="row cl" id="passwordstyle">
				<label class="form-label col-xs-4 col-sm-3"><span
					class="c-red">*</span>新密码：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"
						type="password" class="input-text" autocomplete="off"
						placeholder="密码" id="newPassword" name="newPassword">
				</div>
			</div>
			<div class="row cl" id="passwordstyle1">
				<label class="form-label col-xs-4 col-sm-3"><span
					class="c-red">*</span>确认密码：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"
						type="password" class="input-text" autocomplete="off"
						placeholder="确认新密码" id="password2" name="password2">
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3"></label>
				<div class="formControls col-xs-8 col-sm-9">
					<button class="btn btn-primary radius" type="submit">
						<i class="Hui-iconfont"></i> 保存
					</button>
					<button onClick="layer_close();" class="btn btn-default radius"
						type="button">&nbsp;&nbsp;取消&nbsp;&nbsp;</button>
				</div>
			</div>
		</form>
	</article>
</body>
</html>