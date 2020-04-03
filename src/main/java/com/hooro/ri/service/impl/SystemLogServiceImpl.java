package com.hooro.ri.service.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.orm.hibernate.HibernateDao;
import com.hooro.ri.model.SystemLog;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ty
 * @since 2018-06-28
 */
@Service
public class SystemLogServiceImpl  extends EntityManager<SystemLog, Integer>{

	private HibernateDao<SystemLog, Integer> systemLogDao;

	
    @Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
    	systemLogDao = new HibernateDao<SystemLog, Integer>(sessionFactory, SystemLog.class);
	}
    
	@Override
	protected HibernateDao<SystemLog, Integer> getEntityDao() {
		// TODO Auto-generated method stub
		return systemLogDao;
	}
}
