<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE HTML>
<html>
<head>
	<title>设置功能</title>
	<meta charset="utf-8">
	<meta name="renderer" content="webkit|ie-comp|ie-stand">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
	<meta http-equiv="Cache-Control" content="no-siteapp" />

	<!-- bootstrap -->
<link rel="stylesheet" href="<%=path%>/util/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="<%=path%>/util/bootstrap-table/src/bootstrap-table.css">
<link rel="stylesheet" href="<%=path%>/util/bootstrap-table/src/extensions/filter-control/bootstrap-table-filter-control.css">
<link rel="stylesheet" href="<%=path%>/util/bootstrap/css/bootstrap-datetimepicker.css">

<script type="text/javascript" src="<%=path%>/lib/jquery/1.9.1/jquery.min.js"></script>

<!-- bootstrap-table -->
<script src="<%=path%>/static/util/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=path%>/static/util/bootstrap-table/src/bootstrap-table.js"></script>
<script src="<%=path%>/static/util/bootstrap-table/src/extensions/filter-control/bootstrap-table-filter-control.js"></script>
<script src="<%=path%>/static/util/bootstrap-table/src/locale/bootstrap-table-zh-CN.js"></script>
<script src="<%=path%>/static/util/bootstrap/js/bootstrap-datetimepicker.js"></script>
<script src="<%=path%>/static/util/bootstrap/js/bootstrap-datetimepicker.zh-CN.js"></script>
<!-- layer -->
<script type="text/javascript" src="<%=path%>/lib/layer/2.4/layer.js"></script>
<!-- select2 -->
<link rel="stylesheet" type="text/css" href="<%=path%>/static/css/select2.css" />
<script type="text/javascript" src="<%=path%>/static/js/select2.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/zh-CN.js"></script>
<!-- jquery.validation -->
<script type="text/javascript" src="<%=path%>/static/lib/jquery.validation/1.14.0/jquery.validate.js"></script> 
<script type="text/javascript" src="<%=path%>/lib/jquery.validation/1.14.0/validate-methods.js"></script> 
<script type="text/javascript" src="<%=path%>/lib/jquery.validation/1.14.0/messages_zh.js"></script> 
</head>
	<body>
		<div class="panel-body" style="padding-bottom: 0px;">
			<form class="form-horizontal">
			<input type="hidden" id="id" name="id" value="${menu.id}"/>
				<div class="form-group">
					<label for="inputEmail3" class="col-sm-2 control-label">名称</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="name" name="name" value="${menu.name}" placeholder="名称">
					</div>
				</div>
				<div class="form-group">
					<label for="inputPassword3" class="col-sm-2 control-label">url</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="url" name="url" value='${menu.url}' placeholder="url">
					</div>
				</div>
				<div class="form-group">
					<label for="inputPassword3" class="col-sm-2 control-label">是否启用</label>
					<div class="col-sm-10">
						<select class="form-control" id="status" name="status">
							<option value="1">启用</option>
							<option value="0">停用</option>
						</select>
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<button type="submit" class="btn btn-default" id="present">提交</button>
					</div>
				</div>
			</form>
		</div>
	</body>
	
<script type="text/javascript">
	$(function() {
		$(":radio[name='status'][value='${menu.status }']").prop("checked", "checked");
	    var value='${menu.url}';
		//value=value.replace(sed  -i, '()');
		$("#url").val(value);
	});
      
	$('#present').on( 'click', function () {
		$(".form-horizontal").validate({
			rules:{
				name:{
					required:true,
				},
				url:{
					required:true,
				},
			},
			onkeyup:false,
			focusCleanup:true,
			success:"valid",
			submitHandler:function(form){
				$.post('<%=path%>/menu/_save.do',{type:0,functionType:1,id:$("#id").val(),name:$("#name").val(),url:$("#url").val(),status:$("#status").val(),_parentId:'${parentId}'},function(data){
					if (data.code == 1) {
						parent.layer.msg(data.msg,{icon:1,time:5000});
						parent.$('#btable').bootstrapTable('refresh');
						var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
						parent.layer.close(index);  // 关闭layer
					}else{
						layer.msg(data.msg,{icon:2,time:5000});
					}
				});
			}
		});
	})

</script>

</html>