package com.github.wangyi.springSession;

import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * ClassName: LoginController  
 * Function: 分布式Session登录管理器
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-17 上午11:48:42 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
@Controller
public class LoginController {

	@ResponseBody
	@RequestMapping("/setRedisSession")
	public String setRedisSession(HttpServletRequest request,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		request.getSession().setAttribute("username", "admin");
		return "设置username1=admin1";
	}
	
	@ResponseBody
	@RequestMapping("/getRedisSession")
	public String getRedisSession(HttpServletRequest request,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		System.out.println(session.toString());
		String ret = (String)request.getSession().getAttribute("username");
		int maxInactiveInterva = request.getSession().getMaxInactiveInterval();
		System.out.println("redis失效时间："+maxInactiveInterva+"秒");
		return "查询Session：\"username\"=" + ret;
	}
	
	/**
	 * 可以通过springmvc的拦截器，通过spring session判断远程redis是否已经保存了登录信息
	 * @param request
	 * @param response
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping("/index")
	public String index(HttpServletRequest request,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		return "<h4 style='color:green'>用户已登录，进入首页！</h4>";
		/*CurrUser currUser = (CurrUser)request.getSession().getAttribute("CurrUser");
		if(currUser==null){
			return "<h4 style='color:red'>登录Session失效，请重新登录！</h4>";
		}else{
			return "<h4 style='color:green'>用户已登录，进入首页！</h4>";
		}*/
	}
	
	@RequestMapping("/login")
	public String login(HttpServletRequest request,HttpServletResponse response){
		
		return "login";
	}
	
	@ResponseBody
	@RequestMapping("/loginAuth")
	public String login(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Map<String,Object> params,Model model){
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
			return "<h4 style='color:red'>用户名或密码不能为空！</h4>";
		}
		//此处简单处理
		if(Objects.equals("admin", username) &&Objects.equals("123456", password)){
			CurrUser cu=new CurrUser();
			cu.setUsername("admin");
			cu.setPassword("123456");
			request.getSession().setAttribute("CurrUser", cu);
			return "<h4 style='color:orange'>用户名：admin，密码：123456，用户登录成功！</h4>";
		}else{
			return "<h4 style='color:orange'>用户名或密码错误，请重新登录！</h4>";
		}
	}
 
}
