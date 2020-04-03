package com.hooro.ri.appInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eryansky.common.model.Result;
import com.eryansky.common.orm.PropertyFilter;
import com.eryansky.common.utils.StringUtils;
import com.hooro.ri.config.SystemConfiguration;
import com.hooro.ri.model.AppUser;
import com.hooro.ri.model.AppUserMobile;
import com.hooro.ri.model.LoginInfo;
import com.hooro.ri.model.MessageBoard;
import com.hooro.ri.model.Mobile;
import com.hooro.ri.service.impl.AppUserServiceImpl;
import com.hooro.ri.service.impl.LoginInfoServiceImpl;
import com.hooro.ri.service.impl.MessageBoardServiceImpl;
import com.hooro.ri.service.impl.MobileServiceImpl;
import com.hooro.ri.util.PushNotiUtil;
import com.sun.corba.se.impl.orbutil.RepositoryIdUtility;

import io.netty.util.internal.StringUtil;
import jersey.repackaged.com.google.common.collect.Lists;



/** 
* @Description: TODO(通用文件上传) 
* @author ty 
* @date 2018-4-2 上午9:05:25 
*  
*/
@Controller
@Scope("prototype")
public class AppUploadController {

	@Autowired private MessageBoardServiceImpl  messageBoardService;
	@Autowired private LoginInfoServiceImpl  loginInfoService;
	@Autowired private MobileServiceImpl mobileService;
	@Autowired private AppUserServiceImpl appUserService;
	
	
	/** 
	* <p>Title: </p>    终端上传
	* <p>Description:   上传留言 </p> 
	* @param file       文件流
	* @param fileDir    保存到哪个文件夹
	* @param suffix     文件后缀 
	* @return 
	*/
	@RequestMapping("/uploadMessage")
	public   @ResponseBody Result uploadFile(String simMark,String account,@RequestParam("file") MultipartFile file, HttpServletRequest request, String fileDir,@RequestParam(defaultValue=".png") String suffix)  
	{   
		try
		{  

			
			if(StringUtils.isBlank(simMark))
				return Result.warnResult().setMsg("请填写simMark");
			
			if(StringUtils.isBlank(account))
				return Result.warnResult().setMsg("请填写account");
			
			Mobile mobile=mobileService.findUniqueBy("simMark", simMark);
			if(mobile==null)
				return Result.warnResult().setMsg("simMark未注册");
			
			AppUser appUser=appUserService.findUniqueBy("account", account);
			if(appUser==null)
				return Result.warnResult().setMsg("account账号不存在");
			
			String dir ="";
			if (fileDir != null && !"".equals(fileDir))
			{
				dir = fileDir;
			}
			Date date = new Date();
			InputStream input = file.getInputStream();// 可替换为任何路径何和文件名
			request.setCharacterEncoding("UTF-8");
			long name = date.getTime();
			String dd=SystemConfiguration.PRITURE_WEBUPLOADER_TOMCAT_compression;
			String path = dd+ dir + "/" + name + suffix;//上传路径
			String web = SystemConfiguration.PRITURE_WEBUPLOADER_WEB + dir + "/" + name + suffix;//访问路径
			//检查文件是否存在，不存在则创建
	    	File  fi=new File(SystemConfiguration.PRITURE_WEBUPLOADER_TOMCAT_compression + dir);
	    	if(!fi.exists()&&!fi.isDirectory())
	    		fi.mkdirs();
			FileOutputStream output = new FileOutputStream(path);//文件流
			int in = input.read();
			while (in != -1)
			{
				output.write(in);
				in = input.read();
			}
			output.close();
			input.close();
			
			
			MessageBoard  messageBoard =new MessageBoard ();
			messageBoard.setUrl(web);
			messageBoard.setType(1);
			messageBoard.setStatus(0);
			messageBoard.setMobile(mobile);
			messageBoard.setAccount(account);
			messageBoardService.saveEntity(messageBoard);;
			LoginInfo loginfo=loginInfoService.getLoginInfoByaccount(appUser.getId());
			if(StringUtils.isNotBlank(loginfo.getXg_token()))
				PushNotiUtil.pushSingleMsg(loginfo.getXg_token(), "{\"cmd\":\"messageBoard\",\"url\":\""+messageBoard.getUrl()+"\",\"id\":\""+messageBoard.getId()+"\"}", null, 2, 2,loginfo.getXgType(),"收到新的留言");
			return Result.successResult().setObj(web).setMsg("上传成功");
			
			
		} catch (IOException e)
		{
			e.printStackTrace();
			return Result.errorResult().setMsg("文件流异常结束，请检查文件重新上传");
		} 
	}
	
	
	
	
	
	/** 
	* <p>Title: </p>    app上传
	* <p>Description:   上传留言 </p> 
	* @param file       文件流
	* @param fileDir    保存到哪个文件夹
	* @param suffix     文件后缀 
	* @return 
	*/
	@RequestMapping("/uploadMessageApp")
	public   @ResponseBody Result uploadFileApp(String token,@RequestParam("file") MultipartFile file, HttpServletRequest request, String fileDir,@RequestParam(defaultValue=".png") String suffix)  
	{   
		try
		{   
			if(StringUtils.isBlank(token))
				return Result.warnResult().setMsg("请填写token");
			
			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
			    return Result.warnResult().setMsg("token不存在或已过期");
			
			if(StringUtils.isBlank(appUser.getSimMark()))
				return Result.warnResult().setMsg("请先选中当前操作的终端");
			
			String dir ="";
			if (fileDir != null && !"".equals(fileDir))
			{
				dir = fileDir;
			}
			Date date = new Date();
			InputStream input = file.getInputStream();// 可替换为任何路径何和文件名
			request.setCharacterEncoding("UTF-8");
			long name = date.getTime();
			String dd=SystemConfiguration.PRITURE_WEBUPLOADER_TOMCAT_compression;
			String path = dd+ dir + "/" + name + suffix;//上传路径
			String web = SystemConfiguration.PRITURE_WEBUPLOADER_WEB + dir + "/" + name + suffix;//访问路径
			//检查文件是否存在，不存在则创建
	    	File  fi=new File(SystemConfiguration.PRITURE_WEBUPLOADER_TOMCAT_compression + dir);
	    	if(!fi.exists()&&!fi.isDirectory())
	    		fi.mkdirs();
			FileOutputStream output = new FileOutputStream(path);//文件流
			int in = input.read();
			while (in != -1)
			{
				output.write(in);
				in = input.read();
			}
			output.close();
			input.close();
			
	
			Mobile mobile=mobileService.findUniqueBy("simMark", appUser.getSimMark());
			MessageBoard  messageBoard =new MessageBoard ();
			messageBoard.setUrl(web);
			messageBoard.setType(2);
			messageBoard.setStatus(0);
			messageBoard.setMobile(mobile);
			messageBoard.setAccount(appUser.getAccount());
			messageBoardService.saveEntity(messageBoard);;
			if(StringUtils.isNotBlank(mobile.getXgToken()))
				PushNotiUtil.pushSingleMsg(mobile.getXgToken(), "{\"cmd\":\"messageBoard\",\"id\":\""+messageBoard.getId()+"\",\"url\":\""+web+"\",\"account\":\""+appUser.getAccount()+"\",\"createTime\":\""+messageBoard.getCreateTime().getTime()+"\"}", null, 2, 1,1,"");
			return Result.successResult().setObj(web).setMsg("上传成功");
			
			
		} catch (IOException e)
		{
			e.printStackTrace();
			return Result.errorResult().setMsg("文件流异常结束，请检查文件重新上传");
		} 
	}
	
	
	/**   app获取留言列表
	 * @param 
	 * @return
	 */
	@RequestMapping("/getMessageBoardAllByAppInterface")
	@ResponseBody
	public Result getMessageBoardAllByApp(String token)
	{
		
		if(StringUtils.isBlank(token))
			return Result.warnResult().setMsg("请填写token");
		
		AppUser appUser=loginInfoService.getAppUserByToken(token);
		if(appUser==null)
		    return Result.warnResult().setMsg("token不存在或已过期");
		
		if(StringUtils.isBlank(appUser.getSimMark()))
			return Result.warnResult().setMsg("请先选中当前操作的终端");
		
		Mobile mobile=mobileService.findUniqueBy("simMark", appUser.getSimMark());
		List<PropertyFilter> filters=Lists.newArrayList();
		filters.add(new PropertyFilter("EQI_status","0"));
		filters.add(new PropertyFilter("EQI_mobile.id",mobile.getId()+""));
		filters.add(new PropertyFilter("EQI_type","1"));
		List<MessageBoard> messageBoardList=messageBoardService.find(filters);
		return Result.successResult().setObj(messageBoardList);
	}

	
	
	/**  终端获取留言
	 * @param simMark
	 * @return
	 */
	@RequestMapping("/getMessageBoardAllByMobileInterface")
	@ResponseBody
	public Result getMessageBoardAllByMobile(String simMark)
	{
		if(StringUtils.isBlank(simMark))
			return Result.warnResult().setMsg("请填写simMark");
		
		Mobile mobile=mobileService.findUniqueBy("simMark", simMark);
		if(mobile==null)
			return Result.warnResult().setMsg("simMark未注册");
		
		List<PropertyFilter> filters=Lists.newArrayList();
		filters.add(new PropertyFilter("EQI_status","0"));
		filters.add(new PropertyFilter("EQI_mobile.id",mobile.getId()+""));
		filters.add(new PropertyFilter("EQI_type","2"));
		List<MessageBoard> messageBoardList=messageBoardService.find(filters);
		return Result.successResult().setObj(messageBoardList);
	}
	
	
	/**  设置成已读
	 * @param simMark
	 * @return
	 */
	@RequestMapping("/messageBoardByidInterface")
	@ResponseBody
	public Result getMessageBoardByid(Integer id)
	{
		if(id==null)
			return Result.warnResult().setMsg("请填写id");
		
		MessageBoard messageBoard=messageBoardService.getById(id);
		if(messageBoard==null)
			return Result.warnResult().setMsg("无此条数据");
		messageBoard.setStatus(1);
		messageBoardService.save(messageBoard);
		return Result.successResult();
	}
	
	
	
	@RequestMapping("/uploadMessage11")
	public   @ResponseBody Result uploadFile11()  
	{   
		try
		{  
			
		    PushNotiUtil.pushSingleMsg("0614f04ca37654b2464e924bc033cb2259d459db", "{\"cmd\":\"messageBoard\",\"url\":\"https://resource.jipaopao.cn//1531902573213.wav\"}", null, 2, 2,1,"");
			return Result.successResult().setMsg("上传成功");
			
			
		} catch (Exception e)
		{
			e.printStackTrace();
			return Result.errorResult().setMsg("文件流异常结束，请检查文件重新上传");
		} 
	}
	
}
