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
			
			// 1. 요청받은 URL이 /free/detail.do일때 (상세보기를 요청했을 때)
			// 2. 주소창에 직접 URL을 입력해서 요청했을 때
			// 3. 사용자가 있던 URL이 http://localhost/free/list.do가 아닐때
			//    (다른 페이지에서 상세보기를 요청했을 때)
			if(requestURI.equals("/free/detail.do") &&
				(referer == null || !referer.startsWith(localServerAddress + "/free/list.do"))) {
				
				// 에러페이지를 이동
//				response.sendRedirect("/WEB-INF/views/board/common/error.jsp");
				response.sendRedirect("/error/accessDenied?referer=" + referer);
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


