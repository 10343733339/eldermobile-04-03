//上传 图片
/*
id           文件标签ID
fileName     保存到哪个目录下
path         上传路径    
imgdiv       显示当前div
imgdata      添加图片url
callurl      上传成功返回url保存到
*/
function uploadImg(id,fileName,path,imgdiv,imgdata,callurl)
{   
var index = layer.load(1, { shade: [0.5,'#494848'] });//0.1透明度的白色背景
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
if (size > (1024 * 600)) {
	layer.msg("文件大小不能超过"+600+"kb");
	$('#'+id).val('');
	layer.closeAll('loading'); 
	return false;
}
if(!/.(gif|jpg|jpeg|png|GIF|JPG|bmp)$/.test(file))
{
	$('#'+id).val('');
	layer.closeAll('loading');        
	layer.msg("文件类型必须是.gif,jpeg,jpg,png,bmp中的一种",{icon:2,time:3000});
	return false;
}

var FileController = path+"/uploadFile.do?fileDir="+fileName+"&suffix="+suffix; 
// FormData 对象
var form = new FormData();
//form.append("author", "ty");      // 可以增加表单数据
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
			      $("#"+imgdata).attr('src',data.obj); 
				  $("#"+imgdiv).show();//显示图片div
			      $("#"+callurl).val(data.obj);//
		}
	}
}
xhr.send(form);
} 





