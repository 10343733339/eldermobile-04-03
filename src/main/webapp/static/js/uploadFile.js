//上传 File
/*
id           文件标签ID
fileName     保存到哪个目录下
path         上传路径    
apkdata      上传成功返回url保存到
*/
function uploadApk(id,fileName,path,apkdata)
{   
var index = layer.load(1, {shade: [0.5,'#494848'] //0.1透明度的白色背景
});
var file = $('#'+id).val();

if (file == '') 
{   
	layer.closeAll('loading'); 
	return false;
}

var point = file.lastIndexOf("."); 
var suffix = file.substr(point);
var fileObj = document.getElementById(id).files[0]; // 获取文件对象
var fileSize=0;
var size = fileObj.size / 1024 * 1024;
if (size > (1024 * 30000)) {
	layer.msg("文件大小不能超过30M");
	$('#'+id).val('');
	layer.closeAll('loading'); 
	return false;
}
/*if(!/.(apk)$/.test(file))
{
	$('#'+id).val('');
	layer.closeAll('loading');        
	layer.msg("文件类型必须是.apk",{icon:2,time:3000});
	return false;
}*/

var FileController = path+"/uploadFile.do?fileDir="+fileName+"&suffix="+suffix; 
// FormData 对象
var form = new FormData();
form.append("author", "hooyes");      // 可以增加表单数据
form.append("file", fileObj);         // 文件对象
// XMLHttpRequest 对象
var xhr = new XMLHttpRequest();
xhr.onreadystatechange=callback;
xhr.open("post", FileController, true);
xhr.onload = function (data) 
{
	//alert("上传成功!");
};
function callback() 
{ 
	if(xhr.readyState == 4)   
	{ 
		if(xhr.status == 200)  
		{  
			var data = eval("data =" + xhr.responseText);
			layer.msg(data.msg,{icon:1,time:2000});
			layer.closeAll('loading'); 
			      $("#"+apkdata).val(data.obj); 
		}
	}
}
xhr.send(form);
} 



//通用文件上传(返回访问路径、MD5值、大小)
/*
id           文件标签ID
fileName     保存到哪个目录下
path         上传路径    
apkdata      上传成功返回url保存到
*/
function uploadApk2(id,fileName,path,apkdata)
{   
var index = layer.load(1, {shade: [0.5,'#494848'] //0.1透明度的白色背景
});
var file = $('#'+id).val();

if (file == '') 
{   
	layer.closeAll('loading'); 
	return false;
}

var point = file.lastIndexOf("."); 
var suffix = file.substr(point);
var fileObj = document.getElementById(id).files[0]; // 获取文件对象
var fileSize=0;
var size = fileObj.size / 1024 * 1024;
if (size > (1024 * 30000)) {
	layer.msg("文件大小不能超过30M");
	$('#'+id).val('');
	layer.closeAll('loading'); 
	return false;
}
if(!/.(apk)$/.test(file))
{
	$('#'+id).val('');
	layer.closeAll('loading');        
	layer.msg("文件类型必须是.apk",{icon:2,time:3000});
	return false;
}

var FileController = path+"/uploadFile2.do?fileDir="+fileName+"&suffix="+suffix; 
// FormData 对象
var form = new FormData();
form.append("author", "hooyes");      // 可以增加表单数据
form.append("file", fileObj);         // 文件对象
// XMLHttpRequest 对象
var xhr = new XMLHttpRequest();
xhr.onreadystatechange=callback;
xhr.open("post", FileController, true);
xhr.onload = function (data) 
{
	//alert("上传成功!");
};
function callback() 
{ 
	if(xhr.readyState == 4)   
	{ 
		if(xhr.status == 200)  
		{   debugger;
			var data = eval("data =" + xhr.responseText);
			layer.msg(data.msg,{icon:1,time:2000});
			layer.closeAll('loading'); 
			$("#"+apkdata).val(data.obj.web); 
			$("#md_5").val(data.obj.md5); 
			$("#apkSize").val(data.obj.fileSize); 
		}
	}
}
xhr.send(form);
} 



