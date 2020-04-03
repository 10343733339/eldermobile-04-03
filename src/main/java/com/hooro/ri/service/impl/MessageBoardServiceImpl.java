package com.hooro.ri.service.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.orm.hibernate.HibernateDao;
import com.hooro.ri.model.MessageBoard;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ty
 * @since 2018-06-28
 */
@Service
public class MessageBoardServiceImpl extends EntityManager<MessageBoard, Integer>{

	private HibernateDao<MessageBoard, Integer> messageBoardDao;

	
    @Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
    	messageBoardDao = new HibernateDao<MessageBoard, Integer>(sessionFactory, MessageBoard.class);
	}
    
	@Override
	protected HibernateDao<MessageBoard, Integer> getEntityDao() {
		// TODO Auto-generated method stub
		return messageBoardDao;
	}
}