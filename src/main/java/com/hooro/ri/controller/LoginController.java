package com.hooro.ri.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eryansky.common.model.Result;
import com.eryansky.common.utils.StringUtils;
import com.eryansky.common.utils.encode.Encrypt;
import com.eryansky.common.web.springmvc.SimpleController;
import com.eryansky.common.web.utils.WebUtils;
import com.google.common.collect.Lists;
//import com.hooro.ri.WebrtcServer;
import com.hooro.ri.model.User;
import com.hooro.ri.service.impl.MenuServiceImpl;
import com.hooro.ri.service.impl.UserServiceImpl;
import com.hooro.ri.util.MenuTreeNode;
import com.hooro.ri.util.OnLlineUtil;


/** 
* @ClassName: LoginController 
* @Description: TODO(登录Controller) 
* @author ty 
* @date 2018-3-16 下午02:12:55
*  
*/
@Controller
public class LoginController extends SimpleController {

	
	@Autowired   private MenuServiceImpl menuService;
	@Autowired   private UserServiceImpl userService;
	/**
	 * 登录验证
	 */ 
	@ResponseBody
	@RequestMapping(value = { "loginJson" })
	public Result loginJson(String loginName, String password,String code, HttpServletRequest request,HttpSession session) {
		Result result = null;
		String msg = null;
		if(StringUtils.isBlank(loginName)){
			msg = "请输入用户名!";
			result = new Result(Result.ERROR, msg, null);
			return result;
		}
		if(StringUtils.isBlank(password)){
			msg = "请输入密码!";
			result = new Result(Result.ERROR, msg, null);
			return result;
		}
		
		// 获取用户信息
		User user = userService.login(request,loginName, Encrypt.e(password));
		if (user == null) {
			msg = "用户名或密码不正确!";
			result = new Result(Result.ERROR, msg, "codeError");
			return result;
		}
		// 将用户信息放入session中
		request.getSession().setAttribute("userSession",user);
		request.getSession().setAttribute("userId", user.getId());
		request.getSession().setAttribute("account", user.getAccount());
		request.getSession().setAttribute("nickname", user.getNickname());
		result = new Result(Result.SUCCESS, "用户验证通过!", "index");
		return result;
	}
	
	
	/**
	 * 登录页面
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping(value = { "login" })
	public void login(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		 request.getRequestDispatcher("/login.jsp").forward(request, response);
	}
	
	
    /** 
     * 退出登录
     */  
    @RequestMapping(value={"logout"})  
    public String logout( HttpServletRequest request) throws Exception{  
		//Result result = null;
    	request.getSession().invalidate();  
    	//result = new Result(Result.SUCCESS,null,null);
        //return result;  
    	return "forward:login";
    }  
	/**
	 * 后台主页
	 */
	@RequestMapping(value = { "index" })
	public String Index(HttpServletRequest request,HttpServletResponse response,Model model) {
		//根据登录用户获取权限菜单
		WebUtils.setNoCacheHeader(response);
		List<MenuTreeNode> treeNodes = Lists.newArrayList();
		Object userId = request.getSession().getAttribute("userId");
		treeNodes = menuService.getNavMenuTreeByUserId(Integer.valueOf(userId.toString()));
		model.addAttribute("menuTreeNodes", treeNodes);
		return "index";
	}
	/**
	 * 修改密码 页面
	 */
    @RequestMapping(value = {"password"})
    public String password() throws Exception {
        return "user_password";

    }
    
	/**
	 * 查看在线设备
	 */
    @RequestMapping(value = {"showOnlineModel"})
    @ResponseBody
    public Object showOnlineModel() {
        Map<String,Long> online=OnLlineUtil.onLine;
        return   online;
    }

}
