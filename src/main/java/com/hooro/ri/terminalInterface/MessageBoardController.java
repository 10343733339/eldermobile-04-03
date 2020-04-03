package com.hooro.ri.terminalInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eryansky.common.model.Result;
import com.eryansky.common.orm.PropertyFilter;
import com.eryansky.common.utils.StringUtils;
import com.hooro.ri.config.SystemConfiguration;
import com.hooro.ri.model.Contacts;
import com.hooro.ri.model.MessageBoard;
import com.hooro.ri.model.Mobile;
import com.hooro.ri.service.impl.ContactsServiceImpl;
import com.hooro.ri.service.impl.MessageBoardServiceImpl;
import com.hooro.ri.service.impl.MobileServiceImpl;

/**
 * 留言
 *
 */
@RestController
public class MessageBoardController  {

	private final Logger log = LoggerFactory.getLogger(this.getClass());


	@Autowired   private ContactsServiceImpl contactsService;
	
	@Autowired   private MessageBoardServiceImpl boardService;
	
	@Autowired   private MobileServiceImpl mobileService;

	/**
	 * 获取电话本
	 * @return
	 */
	@RequestMapping(value="/uploadMessageInterface")
	public Result getContacts(String simMark,String phone,@RequestParam("file") MultipartFile file, HttpServletRequest request,@RequestParam(required=true) String suffix)
	{
		try {
			if(StringUtils.isBlank(simMark))
				return Result.warnResult().setMsg("请填写卡号");
			
			if(StringUtils.isBlank(simMark))
				return Result.warnResult().setMsg("请填写手机号");

			List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
			filters.add(new PropertyFilter("EQS_mobile.simMark",simMark));
			List<Contacts> contactsList=contactsService.findJoin(filters);
			return Result.successResult().setObj(contactsList);

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}

	
	/**
	 * 获取视频分享链接
	 * @return
	 */
	@RequestMapping("getShareVideoInterface")
	public Result getShareVideoInterface(String token)
	{
		try {
			if(StringUtils.isBlank(token))
				return Result.warnResult().setMsg("请填写token");

			Mobile mobile = mobileService.findUniqueBy("token", token);
			if(mobile == null) {
				return Result.warnResult().setMsg("登录过期请重新登录！");
			}
			
			List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
			filters.add(new PropertyFilter("EQI_mobile.id",mobile.getId()+""));
			filters.add(new PropertyFilter("EQI_status","0"));
			List<MessageBoard> messageBoards = boardService.find(filters);
			

			return Result.successResult().setObj(messageBoards);

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}
	
	/**
	 * 设置为已读
	 * @return
	 */
	@RequestMapping("setAlreadyReadInterface")
	public Result setAlreadyReadInterface(String token)
	{
		try {
			if(StringUtils.isBlank(token))
				return Result.warnResult().setMsg("请填写token");

			Mobile mobile = mobileService.findUniqueBy("token", token);
			if(mobile == null) {
				return Result.warnResult().setMsg("登录过期请重新登录！");
			}
			List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
			filters.add(new PropertyFilter("EQI_mobile.id",mobile.getId()+""));
			filters.add(new PropertyFilter("EQI_status","0"));
			List<MessageBoard> messageBoards = boardService.find(filters);
			for(MessageBoard messageBoard :messageBoards) {
				messageBoard.setStatus(1);
			}
			boardService.saveOrUpdate(messageBoards);
			return Result.successResult();

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}
}
