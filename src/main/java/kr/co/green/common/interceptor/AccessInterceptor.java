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
			
			// 게시글 등록 접근 제어
			if(requestURI.equals("/news/detail.do") &&
				(referer == null || !referer.startsWith(localServerAddress + "/news/list.do")) &&
				(referer == null || !referer.startsWith(localServerAddress + "/news/editForm.do"))) {
				// 에러페이지를 이동
//				response.sendRedirect("/WEB-INF/views/board/common/error.jsp");
				response.sendRedirect("/error/accessDenied?referer=" + referer);
				return false;
				}
			// 게시글 등록 접근 제어
	        if (requestURI.equals("/news/enroll.do") &&
	            (referer == null || !referer.startsWith(localServerAddress + "/news/enrollForm.do"))) {
	        	System.out.println("enroll.do 접근제어");
	            response.sendRedirect("/error/accessDenied?referer=" + referer);
	            return false; // 게시글 등록은 enrollForm.do에서만 접근 가능
	        }
	        
	        // 수정폼, 접근 제어
	        if (requestURI.equals("/news/editForm.do") &&
	            (referer == null || !referer.startsWith(localServerAddress + "/news/detail.do"))) {
	        	System.out.println("editForm.do 접근제어");
	        	response.sendRedirect("/error/accessDenied?referer=" + referer);
	            return false; // 수정폼,접근은 detail.do에서만 접근 가능
	        }
	        
	        // 수정 요청은 수정 폼에서만 접근 가능
	        if (requestURI.equals("/news/edit.do") &&
	            (referer == null || !referer.startsWith(localServerAddress + "/news/editForm.do"))) {
	        	System.out.println("edit.do 접근제어");
	        	response.sendRedirect("/error/accessDenied?referer=" + referer);
	            return false;
	        }
	        
	        // 삭제 요청은 수정 폼에서만 접근 가능
	        if (requestURI.equals("/news/delete.do") &&
	            (referer == null || !referer.startsWith(localServerAddress + "/news/detail.do"))) {
	        	System.out.println("delete.do 접근제어");
	        	response.sendRedirect("/error/accessDenied?referer=" + referer);
	            return false; 
	        }
	        
	        if (requestURI.equals("/member/register.do") &&
		            (referer == null || !referer.startsWith(localServerAddress + "/member/registerForm.do"))) {
		        	response.sendRedirect("/error/accessDenied?referer=" + referer);
		            return false; 
		        }
			
			if(requestURI.equals("/free/detail.do") &&
				(referer == null || !referer.startsWith(localServerAddress + "/free/list.do"))) {
				System.out.println("detail.do 접근제어");
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


