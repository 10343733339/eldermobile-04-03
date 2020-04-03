package com.hooro.ri.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eryansky.common.orm.PropertyFilter;
import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.orm.hibernate.HibernateDao;
import com.hooro.ri.model.AppUserMobile;
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
@Service
public class AppUserMobileServiceImpl extends EntityManager<AppUserMobile, Integer>{

	public HibernateDao<AppUserMobile, Integer> appUserMobileDao;

	@Autowired private  MobileServiceImpl  mobileService;
	
    @Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
    	appUserMobileDao = new HibernateDao<AppUserMobile, Integer>(sessionFactory, AppUserMobile.class);
	}
    
	@Override
	public HibernateDao<AppUserMobile, Integer> getEntityDao() {
		// TODO Auto-generated method stub
		return appUserMobileDao;
	}
	
	/**
	 * 判断simMark是否合法
	 * @param appUserId
	 * @param simMark
	 * @return
	 */
	public boolean reviewedOrNot(Integer appUserId,String simMark) {
		
		Mobile mobile = mobileService.findUniqueBy("simMark", simMark);
		if(mobile == null) {
			return false;
		}
		
		List<PropertyFilter> filters = Lists.newArrayList();
		filters.add(new PropertyFilter("EQI_isExamine","1"));
		filters.add(new PropertyFilter("EQI_appUser.id",appUserId+""));
		filters.add(new PropertyFilter("EQI_mobile.id",mobile.getId()+""));
		List<AppUserMobile> appUserMobiles= appUserMobileDao.find(filters );
		if(CollectionUtils.isEmpty(appUserMobiles)) {
			return false;
		}
		return true;
		
	}
}