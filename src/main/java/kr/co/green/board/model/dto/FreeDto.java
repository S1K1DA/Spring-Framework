package kr.co.green.board.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter // 게터 생성
@Setter // 세터 생성
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 변수가 있는 생성자
public class FreeDto {
	private int boardNo;
	private String boardTitle;
	private String boardContent;
	private int boardViews;
	private String boardIndate;
	private String boardUpdate;
	private String boardDelete;
	private int memberNo;
	private String memberName;
	
	private String category = "";
	private String searchText = "";
}
