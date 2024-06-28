package kr.co.green.common.validation;

public class DataValidation {
	
	public boolean lengthCheck(String data, int dataLength) {
//		int byteLength = 0;
//		
//		for(char c : data.toCharArray()) {
//			if(Character.toString(c).matches("[a-zA-Z0-9]")) {
//				byteLength += 1;
//			} else if(Character.toString(c).matches("[ㄱ-ㅎㅏ-ㅣ가-힣]")) {
//				byteLength += 3;
//				
//			}
//		}
		
		int byteLength = data.getBytes().length;
		
		if(byteLength > dataLength) {
			return false;
		} else {
			return true;
		}
	}
}
