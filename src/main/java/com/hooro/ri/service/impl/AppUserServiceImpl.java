package com.hooro.ri.service.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.orm.hibernate.HibernateDao;
import com.hooro.ri.model.AppUser;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ty
 * @since 2018-06-28
 */
@Service
public class AppUserServiceImpl extends EntityManager<AppUser, Integer>{

	private HibernateDao<AppUser, Integer> appUserDao;

	
    @Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
    	appUserDao = new HibernateDao<AppUser, Integer>(sessionFactory, AppUser.class);
	}
    
	@Override
	protected HibernateDao<AppUser, Integer> getEntityDao() {
		// TODO Auto-generated method stub
		return appUserDao;
	}
}