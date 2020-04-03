package com.hooro.ri.service.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.orm.hibernate.HibernateDao;
import com.hooro.ri.model.AlarmClock;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ty
 * @since 2018-06-28
 */
@Service
public class AlarmClockServiceImpl extends EntityManager<AlarmClock, Integer>{

	private HibernateDao<AlarmClock, Integer> alarmClockDao;

	
    @Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
    	alarmClockDao = new HibernateDao<AlarmClock, Integer>(sessionFactory, AlarmClock.class);
	}
    
	@Override
	protected HibernateDao<AlarmClock, Integer> getEntityDao() {
		// TODO Auto-generated method stub
		return alarmClockDao;
	}
}