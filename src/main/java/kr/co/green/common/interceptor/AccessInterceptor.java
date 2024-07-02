package kr.co.green.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class AccessInterceptor implements HandlerInterceptor{

		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
				throws Exception {
			String referer = request.getHeader("referer");
			String requestURI = request.getRequestURI();
			
			String serverAddress = request.getRequestURL().toString();
			String localServerAddress = serverAddress.replace(requestURI, "");
			
			System.out.println("referer : " + referer);
			System.out.println("requestURI : " + requestURI);
			System.out.println("serverAddress: " + serverAddress);
			System.out.println("localServerAddress : " + localServerAddress);
			
			if(requestURI.equals("/free/detail.do") &&
				(referer == null || !referer.startsWith(localServerAddress + "/free/list.do"))) {
				response.sendRedirect("/WEB-INF/views/board/common/error.jsp");
				return false;
			}

			return true;
		}

		
		public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
				@Nullable ModelAndView modelAndView) throws Exception {
		}

		public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
				@Nullable Exception ex) throws Exception {
		}

	}


