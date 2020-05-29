package cn.youyu.logistics.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import cn.youyu.logistics.pojo.User;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter {

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		/*
		 * 清除掉Shiro记录的上一个请求路径，认证成功以后跳转到 <property name="successUrl" value="/index.do"/>
		 */

		// 方式一：开发者自己手动清除
		// 1.获取sesssion
		/*
		 * Session session = subject.getSession(false); if(session != null) {
		 * //清除shiro共享的上一次地址 ://shiroSavedRequest
		 * session.removeAttribute(WebUtils.SAVED_REQUEST_KEY); }
		 */

		// 方式二：调用WebUtils工具类中写好的清除的方法
		WebUtils.getAndClearSavedRequest(request);

		return super.onLoginSuccess(token, subject, request, response);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request;
		/*
		 * 从request请求对象中获取 验证码表单数据
		 * 
		 * 如果有：说明用户在做登录操作，要进行验证码判断
		 * 
		 * 判断思路 1，获取用户提交的验证码 2，从Session获取共享的写入图片 随机数 3，比对 相同：放行，继续调用父类方法
		 * 不相同：跳转到登录页，并且共享错误信息
		 * 
		 */
		String verifyCode = req.getParameter("verifyCode");
		System.out.println("verifyCode:"+verifyCode);
		// 获取Session中的随机数
		String rand = (String) req.getSession().getAttribute("rand");
		System.out.println("rand :" + rand);
		if (StringUtils.isNotBlank(verifyCode) && StringUtils.isNotBlank(rand)) {
			// 比对是否相等，不区分大小
			if (!verifyCode.toLowerCase().equals(rand.toLowerCase())) {
				// 跳转到登录页面
				System.out.println("------------------");
				req.setAttribute("errorMsg", "亲，验证码不正确");
				req.getRequestDispatcher("/login.jsp").forward(req, response);
				return false;
			} 
		}
		if(StringUtils.isBlank(verifyCode)) {
			req.setAttribute("errorMsg", "亲，验证码不能为空");
			req.getRequestDispatcher("/login.jsp").forward(req, response);
			return false;
		}
		return super.onAccessDenied(request, response);
	}
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		//从请求中获取shiro主体
		Subject subject = getSubject(request, response);
		Session session = subject.getSession();
		// 如果主体没有认证（Session中认证）并且 主体已经设置记住我了
		if(subject.isAuthenticated()&&subject.isRemembered()) {
			// 获取主体的身份（从记住我的Cookie中获取的）
			User user = (User) subject.getPrincipal();
			// 将身份认证信息共享到 Session中
			session.setAttribute("user", user);
		}
		return subject.isAuthenticated()||subject.isRemembered();
		
		
		//return super.isAccessAllowed(request, response, mappedValue);
	}
}
