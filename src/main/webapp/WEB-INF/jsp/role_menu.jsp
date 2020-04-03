<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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


<script type="text/javascript" src="<%=path%>/lib/jquery/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="<%=path%>/lib/laypage/1.2/laypage.js"></script>
<script type="text/javascript" src="<%=path%>/lib/jquery.validation/1.14.0/jquery.validate.js"></script> 
<script type="text/javascript" src="<%=path%>/lib/jquery.validation/1.14.0/validate-methods.js"></script> 
<script type="text/javascript" src="<%=path%>/lib/jquery.validation/1.14.0/messages_zh.js"></script> 



<link rel="stylesheet" href="<%=path%>/static/util/bootstrap/css/bootstrap.min.css">
<script src="<%=path%>/static/util/bootstrap/js/bootstrap.min.js"></script>	
<link rel="stylesheet" href="<%=path%>/static/util/bootstrap/css/bootstrap-treeview.css">
<script src="<%=path%>/static/util/bootstrap/js/bootstrap-treeview.js"></script>

<style>
li{list-style:none;}
</style>

</head>
<script type="text/javascript">
    function present(){
	    var ids = $.map($('#menuIds').treeview('getChecked', this), function (row) {
	        return row.id;
	    });
		
        $.post('updateRoleMenu.do', {id:$('#id').val(),ids:JSON.stringify(ids)}, function (data) {
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

    function cancel(){
    	parent.layer.closeAll();  // 关闭layer
    }

</script>

<body>
    <form id="role_menu_form">
    	<div >
        	<input type="hidden"  name="id" id="id" value="${role.id }"/>
			<div id="menuIds" style="margin-bottom:50px"></div> 
		</div>
		<div style="position:fixed; bottom:0; background-color:aquamarine;height:50px;width:100%;float:left;margin-bottom: 0px" class="form-group">
			<input class="btn btn-primary radius" type="button" onclick="present()" style="height:35px;margin-left:75%;margin-top:7px;" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">&nbsp;&nbsp;&nbsp;&nbsp;
			<input class="btn btn-primary radius" type="button" onclick="cancel()" style="height:35px;margin-top:7px;" value="&nbsp;&nbsp;取消&nbsp;&nbsp;">
		</div>
	</form>
<script type="text/javascript">
	var $checkableTree = $('#menuIds').treeview({  
	    data: ${menuComboboxData},  
	    levels: 3,  
	    showIcon: true,  
	    showCheckbox: true,  
	    showBorder: true,  
	    /*multiSelect: true,*/  
	    selectedColor: 'back',  
	    selectedBackColor: 'white',  
	    onNodeChecked: function(event, node) {  
	        console.log(node.id);  
	        // parent-有子必有父  
	        function doCheckedNode(node) {  
	            // 初始化  
	            var thisDiv = $('#menuIds');  
	            var parentNode = thisDiv.treeview('getParent', node);  
	            if(parentNode && 0 <= parentNode.nodeId) {  
	                console.log(parentNode);  
	                // 选中  
	                thisDiv.treeview('checkNode', [ parentNode, { silent: true } ]);  
	                // 递归  
	                doCheckedNode(parentNode);  
	            }  
	        }  
	        doCheckedNode(node);  
	
	        // child-无父无子  
	        function doUnCheckedNode(node) {  
	            // 初始化  
	            var thisDiv = $('#menuIds');  
	            if(node && node.nodes && 0 < node.nodes.length) {  
	                var childNodes = node.nodes;  
	                for(var i = 0, len = childNodes.length; i < len; i++) {  
	                    // 取消选中  
	                    thisDiv.treeview('checkNode', [ childNodes[i].nodeId, { silent: true } ]);  
	                    // 递归  
	                    doUnCheckedNode(childNodes[i]);  
	                }  
	            }  
	        }  
	        doUnCheckedNode(node);  
	    },  
	    onNodeUnchecked: function (event, node) {  
	        // child-无父无子  
	        function doUnCheckedNode(node) {  
	            // 初始化  
	            var thisDiv = $('#menuIds');  
	            if(node && node.nodes && 0 < node.nodes.length) {  
	                var childNodes = node.nodes;  
	                for(var i = 0, len = childNodes.length; i < len; i++) {  
	                    // 取消选中  
	                    thisDiv.treeview('uncheckNode', [ childNodes[i].nodeId, { silent: true } ]);  
	                    // 递归  
	                    doUnCheckedNode(childNodes[i]);  
	                }  
	            }  
	        }  
	        doUnCheckedNode(node);  
	    }  
	});  
</script>
</body>
</html>