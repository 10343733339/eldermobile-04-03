package com.hooro.ri.service.impl;

import java.util.List;
import java.util.UUID;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eryansky.common.orm.PropertyFilter;
import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.orm.hibernate.HibernateDao;
import com.eryansky.common.utils.StringUtils;
import com.google.common.collect.Lists;
import com.hooro.ri.model.AppUser;
import com.hooro.ri.model.LoginInfo;
import com.hooro.ri.vo.Xg;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ty
 * @since 2018-06-28
 */
@Service
public class LoginInfoServiceImpl extends EntityManager<LoginInfo, Integer>{

	private HibernateDao<LoginInfo, Integer> loginInfoDao;
	
	@Autowired   private AppUserServiceImpl  appUserService;

	
    @Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
    	loginInfoDao = new HibernateDao<LoginInfo, Integer>(sessionFactory, LoginInfo.class);
	}
    
	@Override
	protected HibernateDao<LoginInfo, Integer> getEntityDao() {
		// TODO Auto-generated method stub
		return loginInfoDao;
	}

	/**
	 * 生成新的Token  并使老的token过期
	 * @param xgToken 
	 */
	public String saveToken(AppUser appUser, String xgToken,String xgType) {
		// TODO Auto-generated method stub
		loginInfoDao.updateBySql("update login_info  set `status`=0 where app_user_id="+appUser.getId(),null);
		LoginInfo loginInfo=new LoginInfo();
		String token=UUID.randomUUID().toString();
		loginInfo.setAppUser(appUser);
		loginInfo.setStatus(1);
		loginInfo.setXg_token(xgToken);
		loginInfo.setToken(token);
		if(xgType!=null&&xgType.equals("IOS"))
		    loginInfo.setXgType(2);
		else
			loginInfo.setXgType(1);
		save(loginInfo);
		return token;
	}
	
	/**  根据token获取登录账号
	 * @param token
	 * @return  
	 */
	public AppUser  getAppUserByToken(String token)
	{
		List<PropertyFilter> filters=Lists.newArrayList();
		filters.add(new PropertyFilter("EQI_status","1"));
		filters.add(new PropertyFilter("EQS_token",token));
		List<LoginInfo> list=find(filters);
		return list==null||list.size()==0?null:list.get(0).getAppUser();
	}
	
	/**  根据token获取xgToken
	 *   @param token
	 *   @return  
	 */
	public  String  getXgTokenByToken(String token)
	{
		List<PropertyFilter> filters=Lists.newArrayList();
		filters.add(new PropertyFilter("EQI_status","1"));
		filters.add(new PropertyFilter("EQS_token",token));
		List<LoginInfo> list=find(filters);
		return list==null||list.size()==0?null:list.get(0).getXg_token();
	}
	
	/**  根据token获取当前终端卡号
	 *   @param token
	 *   @return  
	 */
	public  String  getSimMarkBytoken(String token)
	{
		List<PropertyFilter> filters=Lists.newArrayList();
		filters.add(new PropertyFilter("EQI_status","1"));
		filters.add(new PropertyFilter("EQS_token",token));
		List<LoginInfo> list=find(filters);
		if(list==null||list.size()==0)
			return null;
		
		String simMark=list.get(0).getAppUser().getSimMark();
		return StringUtils.isBlank(simMark)?null:simMark;
	}
	
	/**  根据账号获取token
	 *   @param token
	 *   @return  
	 */
	public  LoginInfo  getLoginInfoByaccount(Integer id)
	{
		
		List<PropertyFilter> filters=Lists.newArrayList();
		filters.add(new PropertyFilter("EQI_status","1"));
		filters.add(new PropertyFilter("EQI_appUser.id",id+""));
		List<LoginInfo> list=find(filters);
		if(list==null||list.size()==0)
			return null;
		
		return list.get(0);
	}
	
	
	/**   返回与之绑定token列表
	 *   @param token
	 *   @return  
	 */
	@SuppressWarnings("unchecked")
	public  List<Object>  getXgTokenByMobileId(Integer mobileId)
	{
		List<Object> xgList=loginInfoDao.createSqlQuery("select  cc.xg_token,cc.xg_type  from  app_user_mobile  a join  app_user b  on a.app_user_id=b.id  join login_info cc"
				+ " on cc.app_user_id=b.id  where mobile_id="+mobileId+" and xg_token is not null  and cc.status=1",null).list();
		return xgList;
	}
	
	/**
	 * 根据当前登录账号返回登录的推送注册id
	 * @param account
	 * @return
	 */
	public  LoginInfo  getLogInfoByAccount(String account) {
		AppUser appUser =  appUserService.findUniqueBy("account", account);
		if (appUser == null) {
			return  null;
		}
		
		return getLoginInfoByaccount(appUser.getId());
	}
	//  select  cc.xg_token  from  app_user_mobile  a join  app_user b  on a.app_user_id=b.id  join login_info cc on cc.app_user_id=b.id  where mobile_id=4  and xg_token is not null  and cc.status=1
}