package com.hooro.ri.terminalInterface;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eryansky.common.model.Result;
import com.eryansky.common.orm.PropertyFilter;
import com.eryansky.common.utils.StringUtils;
import com.hooro.ri.model.AlarmClock;
import com.hooro.ri.model.Mobile;
import com.hooro.ri.model.Pathfinding;
import com.hooro.ri.service.impl.MobileServiceImpl;
import com.hooro.ri.service.impl.PathfindingServiceImpl;

/**
 * 
 *
 */
@RestController
public class PathfindingController  {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired   private PathfindingServiceImpl pathfindingService;
	@Autowired   private  MobileServiceImpl   mobileService;
	/**
	 * 获取导航地址
	 * @return
	 */
	@RequestMapping(value="/getPathfindingInterface")
	public Result getPathfinding(String simMark)
	{
		try {
			if(StringUtils.isBlank(simMark))
				return Result.warnResult().setMsg("请填写卡号");

			Mobile mobile=mobileService.findUniqueBy("simMark", simMark);
			if(mobile==null)
				return Result.warnResult().setMsg("卡号不存在");
			
			List<Pathfinding> pathfindingList=pathfindingService.findBy("mobileId", mobile.getId());
			return Result.successResult().setObj(pathfindingList);

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}
		
}
