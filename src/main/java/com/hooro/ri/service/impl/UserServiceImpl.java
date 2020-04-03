package com.hooro.ri.service.impl;


import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eryansky.common.exception.DaoException;
import com.eryansky.common.exception.ServiceException;
import com.eryansky.common.exception.SystemException;
import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.orm.hibernate.HibernateDao;
import com.eryansky.common.orm.hibernate.Parameter;
import com.eryansky.common.utils.IpUtils;
import com.hooro.ri.model.User;

/**
 * 用户管理  Service层实现类
 * @ClassName: UserServiceImpl
 * @author ty
 * @date 2018-3-16 下午02:12:55
 */
@Service("userService")
public class UserServiceImpl extends EntityManager<User, Integer> {
	
	private HibernateDao<User, Integer> userDao;
	
    @Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		userDao = new HibernateDao<User, Integer>(sessionFactory, User.class);
	}
	
	@Override
	protected HibernateDao<User, Integer> getEntityDao() {
		return userDao;
	}
	

	/** 
	* <p>Description:登录 </p> 
	* @param loginName  
	* @param password
	* @return
	* @throws DaoException
	* @throws SystemException
	* @throws ServiceException 
	 * @throws IOException 
	*/
	
    public User login(HttpServletRequest re,String loginName, String password) throws DaoException,SystemException,ServiceException {
        Parameter parameter = new Parameter(loginName, password, 1);
        List<User> list = getEntityDao().find(
                "from User u where u.account = :p1 and u.password = :p2 and u.status = :p3", parameter);
        User user = null;
        if(!list.isEmpty())
        {
        	user=list.get(0);
        	user.setLoginCount(user.getLoginCount()+1);
        	user.setLastIp(user.getIp());
        	user.setLastTime(user.getAtTime());
        	user.setAtTime(new Date());
        	user.setIp(IpUtils.getIpAddr(re));
        }
        return user;
    }
	/** 
	* <p>Title:根据登录名查找用户 </p> 
	* <p>Description: </p> 
	* @param account
	* @return  User
	*/
	public User getUserByLoginName(String account) {
        Parameter parameter = new Parameter(account);
        List<User> list = userDao.find("from User u where u.account = :p1 ",parameter);
		return list.isEmpty() ? null:list.get(0);
	}
	/** 
	* <p>Title:保存 </p> 
	* <p>Description: </p> 
	* @param user 
	*/
	public void saveUser(User user) {
         saveEntity(user);
	}

}
