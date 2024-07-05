package kr.co.green.common.interceptor;

import java.io.IOException;
import java.util.HashMap;

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
			
//			// 1. 요청받은 URL이 /free/detail.do일때 (상세보기를 요청했을 때)
//			// 2. 주소창에 직접 URL을 입력해서 요청했을 때
//			// 3. 사용자가 있던 URL이 http://localhost/free/list.do가 아닐때
//			//    (다른 페이지에서 상세보기를 요청했을 때)
//			
//			// 게시글 등록 접근 제어
//			if(requestURI.equals("/news/detail.do") &&
//				(referer == null || !referer.startsWith(localServerAddress + "/news/list.do")) &&
//				(referer == null || !referer.startsWith(localServerAddress + "/news/editForm.do"))) {
//				// 에러페이지를 이동
////				response.sendRedirect("/WEB-INF/views/board/common/error.jsp");
//				response.sendRedirect("/error/accessDenied?referer=" + referer);
//				return false;
//				}
//			// 게시글 등록 접근 제어
//	        if (requestURI.equals("/news/enroll.do") &&
//	            (referer == null || !referer.startsWith(localServerAddress + "/news/enrollForm.do"))) {
//	        	System.out.println("enroll.do 접근제어");
//	            response.sendRedirect("/error/accessDenied?referer=" + referer);
//	            return false; // 게시글 등록은 enrollForm.do에서만 접근 가능
//	        }
//	        
//	        // 수정폼, 접근 제어
//	        if (requestURI.equals("/news/editForm.do") &&
//	            (referer == null || !referer.startsWith(localServerAddress + "/news/detail.do"))) {
//	        	System.out.println("editForm.do 접근제어");
//	        	response.sendRedirect("/error/accessDenied?referer=" + referer);
//	            return false; // 수정폼,접근은 detail.do에서만 접근 가능
//	        }
//	        
//	        // 수정 요청은 수정 폼에서만 접근 가능
//	        if (requestURI.equals("/news/edit.do") &&
//	            (referer == null || !referer.startsWith(localServerAddress + "/news/editForm.do"))) {
//	        	System.out.println("edit.do 접근제어");
//	        	response.sendRedirect("/error/accessDenied?referer=" + referer);
//	            return false;
//	        }
//	        
//	        // 삭제 요청은 수정 폼에서만 접근 가능
//	        if (requestURI.equals("/news/delete.do") &&
//	            (referer == null || !referer.startsWith(localServerAddress + "/news/detail.do"))) {
//	        	System.out.println("delete.do 접근제어");
//	        	response.sendRedirect("/error/accessDenied?referer=" + referer);
//	            return false; 
//	        }
//	        
//	        if (requestURI.equals("/member/register.do") &&
//		            (referer == null || !referer.startsWith(localServerAddress + "/member/registerForm.do"))) {
//		        	response.sendRedirect("/error/accessDenied?referer=" + referer);
//		            return false; 
//		        }
//			
//			if(requestURI.equals("/free/detail.do") &&
//				(referer == null || !referer.startsWith(localServerAddress + "/free/list.do"))) {
//				System.out.println("detail.do 접근제어");
//				// 에러페이지를 이동
////				response.sendRedirect("/WEB-INF/views/board/common/error.jsp");
//				response.sendRedirect("/error/accessDenied?referer=" + referer);
//				return false;
//			}
			
			HashMap<String, String[]> requestMap = new HashMap<>();
			requestMap.put("/news/detail.do", new String[]{"/news/list.do", "/news/editForm.do"});
			requestMap.put("/news/enroll.do",  new String[]{"/news/enrollForm.do"});
			requestMap.put("/news/editForm.do",  new String[]{"/news/detail.do"});
			requestMap.put("/news/edit.do",  new String[]{"/news/editForm.do"});
			requestMap.put("/news/delete.do",  new String[]{"/news/detail.do"});
			requestMap.put("/member/register.do",  new String[]{"/member/registerForm.do"});
			
			// 1. 컨트롤러를 호출할지(true), 안할지(false) 결정하는 변수
			boolean chackPathResult = true;
			
			// 2. requstMap에 있는 key를 하나씩 꺼내는 for문
			for(String key : requestMap.keySet()) {
				// 3. pathCheck 메서드 호출 -> 반환값을 result에 저장
				// result에 담기는 값이 false : 잘못된 접근
				// result에 담기는 값이 true : 정상적인 접근
				boolean result = pathCheck(requestURI, 
										   referer, 	
										   localServerAddress, 
										   key, 
										   requestMap.get(key)); 
				
				// 12. 반환받은 값이 false일 때 (잘못된 접근일 때)ㄴ
				if(!result) {
					response.sendRedirect("/error/accessDenied?referer=" + referer);
					chackPathResult = false;
					break;
				}
			}	
			
	        // 13. 검증이 종료된 후에 true로 남아있다면 정상적인 접근 -> 컨트롤러 호출
	        //     false라면 비정상적인 접근 -> 컨트롤러 호출 X

			return chackPathResult;
		}
		
		private boolean pathCheck(String requestURI,
								  String referer,
								  String localServerAddress,
								  String requestMain,
								  String[] requestSub) throws IOException {
			// 4. 검증된 결과값을 저장하는 변수
			// true : 정상적인 접근
			// false : 잘못된 접근
			boolean invalidReferer = true;
			
			// 5. 사용자가 요청한 URL과 꺼내온 key값이 일치하나 확인
			if(requestURI.equals(requestMain)) {
				
				// 6-1. 요청했을 당시의 URL이 null인지 확인
				//    * null인 경우 : 주소창에 직접 입력해서 접근하려고 할 때
				if(referer == null) {
					// 6-2. null인 경우 잘못된 접근이기 때문에 invalidReferer 변수에 false 값 저장
					invalidReferer = false;
				} else { // 7. 요청했을 당시의 URL이 null이 아닐 때
					
					// 8. HashMap value에 있는 값을 하나씩 꺼내서 value에 저장(반복)
					// requestSub : {"/news/lost.do","/news/editForm.do"}
					// 첫번째 반복할 때 value = /news/list.do
					// 두번째 반복할 때 value = /news/editForm.do
					for(String value : requestSub) {
						
						// 9. 요청했을 당시의 URL이 꺼낸 value의 주소가 아닐 때 true (잘못된 접근일 때)
						
						// 처리중인 페이지 : news/detail.do
						// 요청했을 당시의 URL(referer) : news/list.do
						
						// 첫번째 반복에 의하여 value = /news/list.do
						// referer(/news/list.do)가 value(/news/list.do)가 아닐 때
						// --> 조건식 결과 : false, 정상적인 접근일 때
						
						// 두번째 반복에 의하여 value = /news/editForm.do
						// referer(/news/list.do)가 value(/news/editForm.do)가 아닐 때
						// --> 조건식 결과 : true, 잘못된 접근
						if(!referer.startsWith(localServerAddress+value)) {
							invalidReferer = false;
						} else { // 10. 정상적인 접근일 때
							invalidReferer = true;
							break;  // 반복문 종료
						}
					}
				}
			}
			// 11. 검증된 결과를 반환
			return invalidReferer;
			
		}

		
		public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
				@Nullable ModelAndView modelAndView) throws Exception {
		}

		public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
				@Nullable Exception ex) throws Exception {
		}
		
	}


