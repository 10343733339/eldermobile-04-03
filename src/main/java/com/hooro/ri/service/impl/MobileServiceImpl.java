package com.hooro.ri.service.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eryansky.common.orm.PropertyFilter;
import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.orm.hibernate.HibernateDao;
import com.hooro.ri.model.Mobile;

import jersey.repackaged.com.google.common.collect.Lists;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ty
 * @since 2018-06-28
 */
@Service("mobileService")
public class MobileServiceImpl extends EntityManager<Mobile, Integer>{

	private HibernateDao<Mobile, Integer> mobileDao;

	
    @Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
    	mobileDao = new HibernateDao<Mobile, Integer>(sessionFactory, Mobile.class);
	}
    
	@Override
	protected HibernateDao<Mobile, Integer> getEntityDao() {
		// TODO Auto-generated method stub
		return mobileDao;
	}
	
}