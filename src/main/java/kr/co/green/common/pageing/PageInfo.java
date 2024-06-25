package kr.co.green.common.pageing;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// setter가 없기 때문에 VO에 가깝다
// readOnly 특징을 가짐 = setter가 없어서
public class PageInfo {
	private int listCount;
	private int cpage;
	private int pageLimit;
	private int boardLimit;
	
	private int maxPage;
	private int startPage;
	private int endPage;
	private int offset;
	
	
}
