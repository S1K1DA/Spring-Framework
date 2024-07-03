<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
	<%@ include file="./common/head.jsp" %>
</head>
<body>
	<%@ include file="./common/header.jsp" %>
	<%@ include file="./common/nav.jsp" %>
	<section>
    	<h2>홈</h2>
    	<p>안녕하세요! 홈페이지에 오신 것을 환영합니다.</p>
    		<c:if test="${memberNo > 0}">
    			<button onclick="window.location.href = '/free/detail.do?boardNo=39'"
    					style="position: absolute; right: 4%;">상세보기 테스트</button>
    		</c:if>
  	</section>
	<%@ include file="./common/footer.jsp" %>
</body>
</html>
