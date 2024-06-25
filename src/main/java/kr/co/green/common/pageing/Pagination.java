package kr.co.green.common.pageing;

public class Pagination {
	   public static PageInfo getPageInfo(int listCount, int cpage, int pageLimit, int boardLimit) {
		   // ex) listCount : 101, boardLimit : 5
		   // double은 형변환해서 소수점까지 구할 수 있도록 하기 위해서 사용
		   // Math.ceil은 소수점이 있을 때 올림 처리하기 위해서 사용
		   int maxPage = (int)(Math.ceil((double)listCount/boardLimit));

		   // ex) cpage : 16, pageLimit : 10
		   // 1. (cpage-1) : 15
		   // 2. (cpage-1)/pageLimit  => (16-1)/10  =>  15/10  =>  1
		   // 3. (cpage-1)/pageLimit*pageLimit     => 1*10 => 10
		   // 4. (cpage-1)/pageLimit*pageLimit+1   =>  10+1
		   int startPage = (cpage-1) / pageLimit * pageLimit + 1;

		   // ex) startPage : 11, pageLimit : 10
		   // 1. 11+10   =>  21
		   // 2. 21-1    =>  20
		   int endPage = startPage + pageLimit - 1;

		   if(endPage > maxPage) {
			   endPage = maxPage;
		   }

		   // ex) cpage : 16, boardLimit : 10
		   // 1. (cpage-1) => 15
		   // 2. 15 * 10 => 150
		   // DB에서 몇번째 데이터부터 가져올지
		   int offset = (cpage-1) * boardLimit;

		   return new PageInfo(listCount, cpage, pageLimit, boardLimit, 
				   maxPage, startPage, endPage, offset);
	   }
	}
