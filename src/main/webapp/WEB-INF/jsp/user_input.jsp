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
<title>编辑用户</title>
</head>
<script type="text/javascript">
$(function(){
	$('select2').select2();
	loadUserRole();
	//loadUserPlatform();
	$(":radio[name='status'][value='${user.status }']").prop("checked", "checked");
	$("select[name='rank'] option[value='${user.rank}']").prop("selected", "true");
	
	debugger;
	if('${user.id}'!=''){
		$('#passwordstyle').remove();
		$('#passwordstyle1').remove();
	}
	
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	
	// 手机号码验证  
	jQuery.validator.addMethod("isMobile", function(value, element) {  
	    var length = value.length;  
	    var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;  
	    return this.optional(element) || (length == 11 && mobile.test(value));  
	}, "请正确填写您的手机号码"); 
	
	$("#user_form").validate({
		rules:{
			account:{
				required:true,
				isAccount:true,
				rangelength:[3,10],
			},
			nickname:{
				required:true,
				rangelength:[1,10],
			},
			password:{
				required:true,
				rangelength:[5,10],
			},
			password2:{
				required:true,
				equalTo: "#password",
				rangelength:[5,10],
			},
			telephone:{
				isMobile:true,
				required:true,
			},
			email:{
				email:true,
			},
			idcard:{
				isIdCardNo:true,
			},
			rank:{
				required:true,
			}
			
		},
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
            $.post('save.do', $("#user_form").serialize(), function (data) {
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

//自定义验证	

//账号
$.validator.addMethod("isAccount",function(value,element){
	 var reg = /^\w+$/;
   return this.optional(element) || (reg.test(value));
},"<font color='#E47068'>只能输入数字、字母、下划线</font>");



function loadUserRole(){  
	$.ajax({  
		type : "POST", //使用post方法访问后台  
		dataType : "json", //返回json格式的数据  
		url: "${path}/role/combobox.do?selectType=select", //要访问的后台地址   
		success : function(result) {//result为返回的数据  
			for(var i=0; i<result.length; i++){  
				if(result[i].value=='${user.roleId }'){
				$("#roleId").append($("<option selected='selected' value=\""+result[i].value+"\">"+result[i].text+"</option>")); 
				}else{
				$("#roleId").append($("<option value=\""+result[i].value+"\">"+result[i].text+"</option>")); 
				}
			}
		}  
	});  
}

</script> 

<body>
<article class="page-container">
		<form class="form form-horizontal" id="user_form">
			<input type="hidden" id="id" name="id" value="${user.id}" />
			<c:if test="${!empty user}">
				<input type="hidden" id="password" name="password"
					value="${user.password}" />
			</c:if>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3"><span
					class="c-red">*</span>账号：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="text" class="input-text" value="${user.account}"
						placeholder="" id="account" name="account"
						onkeyup="this.value=this.value.replace(/^ +| +$/g,'')">
				</div>
			</div>
			<div class="row cl" id="passwordstyle">
				<label class="form-label col-xs-4 col-sm-3"><span
					class="c-red">*</span>初始密码：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="password" class="input-text" autocomplete="off"
						value="${user.password}" placeholder="初始密码" id="password"
						name="password"
						onkeyup="this.value=this.value.replace(/^ +| +$/g,'')">
				</div>
			</div>
			<div class="row cl" id="passwordstyle1">
				<label class="form-label col-xs-4 col-sm-3"><span
					class="c-red">*</span>确认密码：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="password" class="input-text" autocomplete="off"
						value="${user.password}" placeholder="确认密码" id="password2"
						name="password2"
						onkeyup="this.value=this.value.replace(/^ +| +$/g,'')">
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3"><span
					class="c-red">*</span>昵称：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="text" class="input-text" value="${user.nickname}"
						placeholder="昵称" id="nickname" name="nickname"
						onkeyup="this.value=this.value.replace(/^ +| +$/g,'')">
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3"><span
					class="c-red">*</span>手机：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="text" class="input-text" value="${user.telephone}"
						placeholder="手机" id="telephone" name="telephone"
						onkeyup="this.value=this.value.replace(/^ +| +$/g,'')">
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">邮箱：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="text" class="input-text" value="${user.email}"
						placeholder="邮箱" id="email" name="email"
						onkeyup="this.value=this.value.replace(/^ +| +$/g,'')">
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">身份证号：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="text" class="input-text" value="${user.idcard}"
						placeholder="身份证号" id="idcard" name="idcard"
						onkeyup="this.value=this.value.replace(/^ +| +$/g,'')">
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3"><span
					class="c-red">*</span>级别：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<%-- <input type="text" class="input-text" value="${user.rank}" placeholder="级别" id="rank" name="rank"> --%>
					<select class="select-box" id="rank" name="rank">
						<option value="">请选择...</option>
						<option value="1">超级会员</option>
						<option value="2">高级会员</option>
						<option value="3">普通会员</option>
					</select>
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3"><span
					class="c-red">*</span>状态：</label>
				<div class="formControls col-xs-8 col-sm-9 skin-minimal">
					<div class="radio-box">
						<input name="status" type="radio" id="sex-1" value="1" checked>
						<label for="sex-1">启用</label>
					</div>
					<div class="radio-box">
						<input type="radio" id="sex-2" value="0" name="status"> <label
							for="sex-2">停用</label>
					</div>
				</div>
			</div>

			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3"><span
					class="c-red">*</span>角色：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<select class="select-box" name="roleId" id="roleId" size="1">
					</select>
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
		</form>
	</article>
</body>
</html>