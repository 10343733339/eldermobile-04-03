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
<script src="<%=path%>/util/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=path%>/util/bootstrap-table/src/bootstrap-table.js"></script>
<script src="<%=path%>/util/bootstrap-table/src/extensions/filter-control/bootstrap-table-filter-control.js"></script>
<script src="<%=path%>/util/bootstrap-table/src/locale/bootstrap-table-zh-CN.js"></script>
<script src="<%=path%>/util/bootstrap/js/bootstrap-datetimepicker.js"></script>
<script src="<%=path%>/util/bootstrap/js/bootstrap-datetimepicker.zh-CN.js"></script>

<!-- layer -->
<script type="text/javascript" src="<%=path%>/lib/layer/2.4/layer.js"></script>

<!-- select2 -->
<link rel="stylesheet" type="text/css" href="<%=path%>/static/css/select2.css" />
<script type="text/javascript" src="<%=path%>/static/js/select2.min.js"></script>
<script type="text/javascript" src="<%=path%>/static//js/zh-CN.js"></script>
</head>

<script type="text/javascript">
	$(function() {
		$('select').select2();
		//初始化Table
		var oTable = new TableInit();
		oTable.Init();
		$('#btable').on('click-row.bs.table', function (e, row, $element) {
            $('.success').removeClass('success');
            $($element).addClass('success');
        });
	});

	var TableInit = function() {
		var oTableInit = new Object();
		//初始化Table
		oTableInit.Init = function() {
			$('#btable').bootstrapTable( {
				url : '<%=path%>/menu/searchForBootstraptable.do', //请求后台的URL（*）
				method : 'post', //请求方式（*）
				contentType:"application/x-www-form-urlencoded; charset=UTF-8",
				toolbar : '#toolbar', //工具按钮用哪个容器
				striped : true, //是否显示行间隔色
				cache : false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
				pagination : false, //是否显示分页（*）
				sortable : false, //是否启用排序
				queryParams : oTableInit.queryParams,//传递参数（*）
				sidePagination : "server", //分页方式：client客户端分页，server服务端分页（*）
				search : false, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
				strictSearch : true,
				singleSelect : true, // 单选
				showColumns : true, //是否显示所有的列
				showRefresh : true, //是否显示刷新按钮
				minimumCountColumns : 2, //最少允许的列数
				clickToSelect : true, //是否启用点击选中行
				height:tableHeight(),
				uniqueId : "id", //每一行的唯一标识，一般为主键列
				showToggle : true, //是否显示详细视图和列表视图的切换按钮
				cardView : false, //是否显示详细视图
				columns : [ {checkbox : true}, 
							{field: 'name',title: '名称',sortable: false,align: 'center'},
							{field: 'url',title: 'url',sortable: false,align: 'center',
								formatter: function (value, row, index) {
								return    value.replace(/&/g, '&amp').replace(/</g, '&lt').replace(/>/g, '&gt');
								}},
							{field: 'status',title: '状态',sortable: true,align: 'center', formatter: function (value, row, index) {
								if(value==1) return "启用"; if(value==0) return "停用";}},
							 ]
			});
		};

		//得到查询的参数
		oTableInit.queryParams = function(params) {
			var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
				//rows : params.limit, //页面大小
				//page : params.offset / params.limit + 1, //页码
	            //sort : params.sort,
	            //order : params.order,
	            'filter_EQI_parentMenu__id' : '${menu.id}',
	            'filter_EQI_type': 0,
	            'filter_EQI_functionType' : 1
			};
			return temp;
		};
		return oTableInit;
	};

	function tableHeight() {
		var window_height = $(window).height();
		var obj_off_y = $(".fit-body").offset().top;
		var result_height = window_height - obj_off_y;
		return result_height;
	}

	// 新增
	function add(){
		var url = '<%=path%>/menu/function_input.do?parentId=${menu.id}';
		layer.open({
			type: 2,
			area: ['60%','70%'],
			fix: false, //不固定
			maxmin: true,
			shade:0.4,
			title: "功能",
			content: url
		});
	}

	// 编辑
	function edit(){
		var ids = $('#btable').bootstrapTable('getSelections');
	    if(ids.length>0){
		    if(ids.length==1){
				var url = '<%=path%>/menu/function_input.do?parentId=${menu.id}&id='+ids[0].id;
				layer.open({
					type: 2,
					area: ['60%','70%'],
					fix: false, //不固定
					maxmin: true,
					shade:0.4,
					title: "功能",
					content: url
				});
		    }else{
		    	layer.msg("请选择一条数据编辑",{icon:2,time:5000});
		    }
	    }else{
	    	layer.msg("请选择数据",{icon:2,time:5000});
	    }
	}

	// 删除
	function remove(){
	    var ids = $.map($('#btable').bootstrapTable('getSelections'), function (row) {
	        return row.id;
	    });
	    if(ids.length>0){
	    	layer.confirm('您确定要删除选中的行？', {icon: 3, title:'提示'}, function(index){
	            $.ajax({
	                url: '_remove.do',
	                type: 'post',
	                data: {ids: ids},
	                traditional: true,
	                dataType: 'json',
	                success: function (data) {
	                    if (data.code == 1) {
	                    	layer.msg(data.msg,{icon:1,time:5000});
							$('#btable').bootstrapTable('refresh');
	                    } else {
	                    	layer.msg(data.msg,{icon:2,time:5000});
	                    }
	                }
	            });
	    		layer.close(index);
	    	});
	    }else{
	    	layer.msg("请选择数据",{icon:2,time:5000});
	    }
	}

	// 查询
	function search(){
		$('#btable').bootstrapTable('refresh');
	}
</script>

<body>
	<div class="panel-body" style="padding-bottom: 0px;">

		<div id="toolbar" class="btn-group">
			<button id="btn_add" type="button" class="btn btn-default"
				onclick="add()">
				<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>&nbsp;&nbsp;新增
			</button>
			<button id="btn_edit" type="button" class="btn btn-default"
				onclick="edit()">
				<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>&nbsp;&nbsp;编辑
			</button>
			<button id="btn_remove" type="button" class="btn btn-default"
				onclick="remove()">
				<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>&nbsp;&nbsp;删除
			</button>
		</div>
		<div class="fit-body">
			<table id="btable" class="table-repositive"></table>
		</div>
	</div>
</body>

</html>