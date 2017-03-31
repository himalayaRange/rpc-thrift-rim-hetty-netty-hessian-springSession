package com.github.wangyi.springSession;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 
 * ClassName: SpringMvcFilter  
 * Function: SpringMvc拦截器+spring sesson + redis
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-20 上午10:29:58 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class SpringMvcFilter extends HandlerInterceptorAdapter {

	private static final String[] IGNORE_URI=
		{
			"/login","/logout",
			
		};
	
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception exception)
			throws Exception {
		
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView modelAndView)
			throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		boolean flag=false;
		
		String request_url = request.getRequestURI().toString();
		String conthPath = request.getContextPath();
		
		if(conthPath!=null){
			request_url=request_url.replace(conthPath, "");
		}
		if(Objects.equals(request_url, "/")||Objects.equals("", request_url)){
			return true;
		}
		
		for(String url:IGNORE_URI){ 
			if(request_url.contains(url)){ 
				flag=true;
				break;
			}
		}
		if(!flag){
			CurrUser currUser = (CurrUser)request.getSession().getAttribute("CurrUser"); //获取redis上的session对象
			if(currUser==null){
				String head_url=request.getHeader("Referer");
				String redirect=request.getParameter("redirect");
				request.getServletContext().setAttribute("redirect_url", head_url);
				String urlTemp = StringUtils.isNotEmpty(redirect)?redirect:request_url;
				response.sendRedirect(conthPath + "/login?redirect="+urlTemp);
				return false;
			}
			return true;
		}
		return flag;
	}
	
}


