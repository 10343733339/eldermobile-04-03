<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="resource.jsp"%>
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
<link rel="stylesheet" type="text/css" href="${path}/util/jquery-ui-1.12.1.custom/jquery-ui.min.css" />

<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="${path}/lib/jquery/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="${path}/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${path}/util/h-ui/js/H-ui.min.js"></script> 
<script type="text/javascript" src="${path}/util/h-ui.admin/js/H-ui.admin.js"></script> 
<!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${path}/lib/My97DatePicker/4.8/WdatePicker.js"></script> 
<script type="text/javascript" src="${path}/lib/datatables/1.10.0/jquery.dataTables.min.js"></script> 
<script type="text/javascript" src="${path}/lib/laypage/1.2/laypage.js"></script>
<script type="text/javascript" src="${path}/util/jquery-ui-1.12.1.custom/jquery-ui.min.js"></script>

<title>角色</title>
</head>
<body>
 <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a>
<div class="page-container">
	<div class="text-c"> 名称：
		<input type="text" class="input-text" style="width:250px" placeholder="名称" id="name" name="name">
		<button type="submit" class="btn btn-success" id="" name="" onclick="search()"><i class="Hui-iconfont">&#xe665;</i> 搜索</button>
	</div>
	${menus}
	
	<div class="mt-20">
		<table class="table table-border table-bordered table-hover table-bg table-sort">
			<thead>
				<tr>
					<th>序号</th>
	  				<th>角色名</th>
	  				<th>备注</th>
	  				<th>状态</th>
	  			</tr>
	  		</thead>
		</table>
	</div>
</div>

<script type="text/javascript">
$(document).ready(function() {
table=$('.table-sort').DataTable({
	"aLengthMenu":[10, 15, 20], //动态指定分页后每页显示的记录数。
    "processing": true,//代码没加载完成时 会显示加载中…
    "searching": false,//关闭datatables自带搜索功能（没什么用）
    "bPaginate": true, //翻页功能
    "bLengthChange": true, //改变每页显示数据数量
    "bFilter": false, //过滤功能
    "bSort": false, //排序功能
    "serverSide": true,//服务器端处理数据
    "sPaginationType": "full_numbers",//用这个参数显示的页码工具比较全 除了"full_numbers"还有‘two_button’
    "oLanguage": {   //DataTable中文化 把提示语之类的换成中文
		"sProcessing" : "正在获取数据，请稍后...",
		"sLengthMenu" : "显示 _MENU_ 条",
		"sZeroRecords" : "没有找到数据",
		"sInfo" : "从 _START_ 到  _END_ 条记录 总记录数为 _TOTAL_ 条",
		"sInfoEmpty" : "记录数为0",
		"sInfoFiltered" : "(全部记录数 _MAX_ 条)",
		"sInfoPostFix" : "",
		"sSearch" : "搜索",
		"sUrl" : "",
		"oPaginate" : {
			"sFirst" : "第一页",
			"sPrevious" : "上一页",
			"sNext" : "下一页",
			"sLast" : "最后一页"
		}
    },
    "ajax": {
        "type": "POST",
        "url": 'role/search.do',
        "data": function (data) {//在此处对data（datatables传给服务器端的数据）进行处理 data.start是从哪个数据开始，data.length是页面长度 通过这两个参数可以分页 
            data.page = data.start / data.length + 1;
            data.rows = data.length;
            data.filter_LIKES_name = $("#name").val();
        },
    },
	"aoColumns" : [ //aoColumns设置列
	                {"data": null,"targets": 0,"width":"50px"},
	                {"data": "name", 'sClass': "text-center", "orderable": false,"sClass": "text-c odd"},//data 数据 如果复杂逻辑可以写在回调fnRowCallback中否则可以直接返回aoData的属性
	                {"data": "remark", 'sClass': "text-center", "orderable": false,"sClass": "text-c odd"},
	                {"data": function(data){if(data.status=='1') return "启用";else return "停用";}, 'sClass': "text-center", "orderable": false,"sClass": "text-c odd"}
	],
	"fnDrawCallback": function(){
	var api = this.api();
	var startIndex= api.context[0]._iDisplayStart;//获取到本页开始的条数
	api.column(0).nodes().each(function(cell, i) {
	//此处 startIndex + i + 1;会出现翻页序号不连续，主要是因为startIndex 的原因,去掉即可。
	cell.innerHTML = startIndex + i + 1;
	//cell.innerHTML =  i + 1;
	});
  	}
	//drawCallback: function( settings ) {
        //$('input[name=allChecked]')[0].checked=false;//取消全选状态
    //}
});
$('.table-sort tbody').on( 'click', 'tr', function () {
    if ($(this).hasClass('selected')) {
        $(this).removeClass('selected');
    }
    else {
        table.$('tr.selected').removeClass('selected');
        $(this).addClass('selected');
    }
});
});

// 新增
function add(){
	var url = 'role/input.do';
	layer_show('添加角色',url,'800','580');
}

// 编辑
function edit(){
	if(table.rows('.selected').data().length>0){
		var id = table.rows('.selected').data()[0].id;
		var url = 'role/input.do?id='+id;
		layer_show('编辑角色',url,'800','580');
	}else{
		layer.msg('请选择一行',{icon:2,time:5000});
	}
}

// 设置菜单
function editRoleMenu(){
	if(table.rows('.selected').data().length>0){
		var id = table.rows('.selected').data()[0].id;
		var url = 'role/menu.do?id='+id;
		layer_show('设置菜单',url,'800','580');
	}else{
		layer.msg('请选择一行',{icon:2,time:5000});
	}
}

// 查询
function search(){
	table.ajax.reload();
}
</script>
</body>
</html>