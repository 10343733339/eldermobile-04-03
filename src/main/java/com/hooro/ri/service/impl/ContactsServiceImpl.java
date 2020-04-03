package com.hooro.ri.service.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.orm.hibernate.HibernateDao;
import com.hooro.ri.model.Contacts;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ty
 * @since 2018-06-28
 */
@Service
public class ContactsServiceImpl extends EntityManager<Contacts, Integer>{

	private HibernateDao<Contacts, Integer> contactsDao;

	
    @Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
    	contactsDao = new HibernateDao<Contacts, Integer>(sessionFactory, Contacts.class);
	}
    
	@Override
	protected HibernateDao<Contacts, Integer> getEntityDao() {
		// TODO Auto-generated method stub
		return contactsDao;
	}
}