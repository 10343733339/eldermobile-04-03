<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Home</title>
<!-- Custom Theme files -->

<!-- Custom Theme files -->
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />

<link href="<%=path%>/static/h-ui/css/H-ui.min.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/static/h-ui.admin/css/H-ui.login.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/static/h-ui.admin/css/style.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/css/ribbon/style.css" rel="stylesheet" type="text/css" media="all"/>
<link href="<%=path%>/css/ribbon/iconfont.css" rel="stylesheet" type="text/css" />
<script src="<%=path%>/lib/jquery/1.9.1/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/lib/layer/2.4/layer.js"></script>
</head>
<body onload="loadTopWindow()">
<div class="login">
	<h2>后台系统</h2>
	<div class="login-top">
		<h1>LOGIN FORM</h1>
		<form>
						<input type="text" placeholder="用户名..." name="loginName" id="loginName" />
						<br/>
						<input  type="password" placeholder="密码..." name="password" id="password" />
						<br/>
		
        </form>
	    <div class="forgot">
	    	<input type="submit" onclick="login()" value="Login" >
	    </div>
	</div>
	<div class="login-bottom">
		<h3>System Version &nbsp; V1.0  &nbsp; </h3>
	</div>
</div>	
<div class="copyright">
	<p style="opacity:0.5;">Copyright @2017 深圳××科技有限公司 </p>

	</div>


</body>
   <script type="text/javascript">
	//登陆页面
	//判断当前窗口是否有顶级窗口，如果有就让当前的窗口的地址栏发生变化， 
	function loadTopWindow() {
		if (window.top != null && window.top.document.URL != document.URL) {
			window.top.location = document.URL; //这样就可以让登陆窗口显示在整个窗口了 
		}
	}
	// 登录
	function login() {
		$.post('<%=path%>/loginJson.do', {loginName:$('#loginName').val(),password:$('#password').val(),code:$('#code').val()},function(data) {
			if (data.code == 1) {
				//layer.alert(data.msg, {icon : 1,time : 5000});
				location.href =data.obj;
			} else {
				if(data.obj == "codeError"){
					$("#captchaImage").attr("src","login/captcha.do?timestamp=" + (new Date()).valueOf());
					$("#code").val("");
				}
				layer.alert(data.msg, {icon : 2});
			}
		}, 'json');
	}

	//为keyListener方法注册按键事件
	document.onkeydown = keyListener;
	function keyListener(e) {
		// 当按下回车键，执行我们的代码
		if (e.keyCode == 13) {
			login();
		}
	}
</script>
</html>