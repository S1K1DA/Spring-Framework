<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
	<%@ include file="../../common/head.jsp" %>
</head>
<body>
	<%@ include file="../../common/header.jsp" %>
	<%@ include file="../../common/nav.jsp" %>
	
	   <section style="height: 70vh">
      <h1>자유 게시판</h1>
      <hr>
      <div class="input-group mb-3" style="width: 100%; margin-top: 40px">

         <form action="/freeBoard/list.do" method="GET">
            <input type="hidden" name="cpage" value="1"> <select
               class="form-select" id="inputGroupSelect02" name="category"
               style="width: 100px; height: 46px; flex: 0 0 auto;">
               <option value="fb_title" selected>제목</option>
               <option value="fb_content">내용</option>
               <option value="fb_name">작성자</option>

            </select> <input type="text" style="width: 500px; flex: 0 0 auto;"
               class="form-control" name="search-text" placeholder="검색어를 입력해주세요."
               aria-label="Recipient's username" aria-describedby="button-addon2">
            <button class="btn btn-outline-secondary" type="submit"
               id="button-addon2" style="height: 46px;">검색</button>
         </form>

         <button onclick="window.location.href = '/form/enrollForm.do'"
            style="position: absolute; right: 4%;">등록</button>

      </div>
      <div class="table-container">
         <table class="table table-hover" style="margin-top: 10px;">
            <thead>
               <tr>
                  <th scope="col">번호</th>
                  <th scope="col">제목</th>
                  <th scope="col">작성자</th>
                  <th scope="col">작성일</th>
                  <th scope="col">조회수</th>
               </tr>
            </thead>
            <tbody>

               <tr onclick="location.href=''">
                  <td scope="row">0</td>
                  <td>안녕하세요</td>
                  <td>홍길동</td>
                  <td>2024-06-24</td>
                  <td>1</td>
               </tr>
            </tbody>
         </table>
      </div>

      <nav aria-label="Page navigation example"
         style="background-color: white;">

         <ul class="pagination" style="justify-content: center;">
            <li class="page-item" style="margin-right: 0px"><a
               class="page-link" href="#" aria-label="Previous"> <span
                  aria-hidden="true">&laquo;</span>
            </a></li>

            <li class="page-item" style="margin-right: 0px"><a
               class="page-link" href="/freeBoard/list.do?cpage=1">1</a></li>

            <li class="page-item" style="margin-right: 0px"><a
               class="page-link" href="#" aria-label="Next"> <span
                  aria-hidden="true">&raquo;</span>
            </a></li>

         </ul>
      </nav>
   </section>
   
	<%@ include file="../../common/footer.jsp" %>
</body>
</html>
