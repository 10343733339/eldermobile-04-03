package com.hooro.ri.service.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.orm.hibernate.HibernateDao;
import com.hooro.ri.model.Enclosure;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ty
 * @since 2018-06-28
 */
@Service
public class EnclosureServiceImpl extends EntityManager<Enclosure, Integer>{

	private HibernateDao<Enclosure, Integer> EnclosureDao;

	
    @Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
    	EnclosureDao = new HibernateDao<Enclosure, Integer>(sessionFactory, Enclosure.class);
	}
    
	@Override
	protected HibernateDao<Enclosure, Integer> getEntityDao() {
		// TODO Auto-generated method stub
		return EnclosureDao;
	}
}