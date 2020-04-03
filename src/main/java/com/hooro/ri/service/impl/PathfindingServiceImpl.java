package com.hooro.ri.service.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.orm.hibernate.HibernateDao;
import com.hooro.ri.model.Pathfinding;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ty
 * @since 2018-06-28
 */
@Service
public class PathfindingServiceImpl extends EntityManager<Pathfinding, Integer>{

	private HibernateDao<Pathfinding, Integer> PathfindingDao;


	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		PathfindingDao = new HibernateDao<Pathfinding, Integer>(sessionFactory, Pathfinding.class);
	}

	@Override
	protected HibernateDao<Pathfinding, Integer> getEntityDao() {
		// TODO Auto-generated method stub
		return PathfindingDao;
	}




}