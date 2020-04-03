package com.hooro.ri.service.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.orm.hibernate.HibernateDao;
import com.hooro.ri.model.Navigation;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ty
 * @since 2018-06-28
 */
@Service("navigationService")
public class NavigationServiceImpl extends EntityManager<Navigation, Integer>{

	private HibernateDao<Navigation, Integer> navigationDao;


	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		navigationDao = new HibernateDao<Navigation, Integer>(sessionFactory, Navigation.class);
	}

	@Override
	protected HibernateDao<Navigation, Integer> getEntityDao() {
		// TODO Auto-generated method stub
		return navigationDao;
	}




	@SuppressWarnings("unchecked")
	public List<Navigation> getNavigationSameDay() {
		// TODO Auto-generated method stub
		return navigationDao.createSqlQuery("select  * from  navigation where TO_DAYS(create_time) = TO_DAYS(NOW())", null).addEntity(Navigation.class).list();
	}
}