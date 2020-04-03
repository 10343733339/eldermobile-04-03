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
import com.hooro.ri.service.impl.AlarmClockServiceImpl;

/**
 * 闹钟
 *
 */
@RestController
public class AlarmClockController  {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired   private AlarmClockServiceImpl alarmClockService;

	/**
	 * 获取闹钟
	 * @return
	 */
	@RequestMapping(value="/getAlarmClockInterface")
	public Result getAlarmClock(String simMark)
	{
		try {
			if(StringUtils.isBlank(simMark))
				return Result.warnResult().setMsg("请填写卡号");

			List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
			filters.add(new PropertyFilter("EQS_mobile.simMark",simMark));
			List<AlarmClock> contactsList=alarmClockService.findJoin(filters);
			return Result.successResult().setObj(contactsList);

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return Result.errorResult();
		}

	}

}
