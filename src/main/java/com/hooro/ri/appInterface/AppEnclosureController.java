package com.hooro.ri.appInterface;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eryansky.common.model.Result;
import com.eryansky.common.orm.PropertyFilter;
import com.eryansky.common.utils.StringUtils;
import com.google.common.collect.Lists;
import com.hooro.ri.model.AppUser;
import com.hooro.ri.model.Enclosure;
import com.hooro.ri.model.Mobile;
import com.hooro.ri.service.impl.EnclosureServiceImpl;
import com.hooro.ri.service.impl.LoginInfoServiceImpl;
import com.hooro.ri.service.impl.MobileServiceImpl;

/**
 * 设置围栏
 *
 */
@RestController
public class AppEnclosureController  {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired   private EnclosureServiceImpl enclosureService;

	@Autowired   private LoginInfoServiceImpl loginInfoService;

	@Autowired   private MobileServiceImpl mobileService;



	/**
	 * 添加围栏
	 * @return
	 */
	@RequestMapping(value="/addEnclosureInterface")
	public Result addEnclosure(String token,Double longitude,Double latitude,Double radius,String enclosureName)
	{
		try {
			Result result=new Result();

			if(StringUtils.isBlank(token))
				return result.setCode(2).setMsg("token不能为空");

			if(null==longitude)
				return result.setCode(2).setMsg("longitude");

			if(null==latitude)
				return result.setCode(2).setMsg("latitude不能为空");

			if(radius==null)
				return result.setCode(2).setMsg("radius不能为空");
			
			if(StringUtils.isBlank(enclosureName))
				return result.setCode(2).setMsg("enclosureName不能为空");

			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
				return Result.warnResult().setMsg("token不存在或已过期");

			if(StringUtils.isBlank(appUser.getSimMark()))
				return Result.warnResult().setMsg("请先选中当前操作的终端");

			Mobile mobile=mobileService.findUniqueBy("simMark", appUser.getSimMark());
			List<PropertyFilter> filters=Lists.newArrayList();
			filters.add(new PropertyFilter("EQI_appUser.id",appUser.getId()+""));
			filters.add(new PropertyFilter("EQI_mobile.id",mobile.getId()+""));
			List<Enclosure> enclosurelist =enclosureService.find(filters);
			Enclosure enclosure=enclosurelist==null||enclosurelist.size()==0?new Enclosure():enclosurelist.get(0);
			enclosure.setAppUser(appUser);
			enclosure.setLatitude(latitude);
			enclosure.setLongitude(longitude);
			enclosure.setRadius(radius);
			enclosure.setMobile(mobile);
			enclosure.setEnclosureName(enclosureName);
			enclosureService.saveEntity(enclosure);
			return Result.successResult().setObj("success");

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}
	
	
	/**
	 * 获取围栏
	 * @return
	 */
	@RequestMapping(value="/getEnclosureInterface")
	public Result getEnclosure(String token)
	{
		try {
			Result result=new Result();

			if(StringUtils.isBlank(token))
				return result.setCode(2).setMsg("token不能为空");


			AppUser appUser=loginInfoService.getAppUserByToken(token);
			if(appUser==null)
				return Result.warnResult().setMsg("token不存在或已过期");

			if(StringUtils.isBlank(appUser.getSimMark()))
				return Result.warnResult().setMsg("请先选中当前操作的终端");

			Mobile mobile=mobileService.findUniqueBy("simMark", appUser.getSimMark());

			List<PropertyFilter> filters=Lists.newArrayList();
			filters.add(new PropertyFilter("EQI_appUser.id",appUser.getId()+""));
			filters.add(new PropertyFilter("EQI_mobile.id",mobile.getId()+""));
			List<Enclosure> enclosurelist =enclosureService.find(filters);
			Enclosure enclosure=enclosurelist==null||enclosurelist.size()==0?new Enclosure():enclosurelist.get(0);
			return Result.successResult().setObj("success").setObj(enclosure);

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}

}
