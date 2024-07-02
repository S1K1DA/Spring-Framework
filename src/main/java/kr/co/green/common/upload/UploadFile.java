package kr.co.green.common.upload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import kr.co.green.board.model.dto.FreeDto;
import kr.co.green.board.model.dto.NewsDto;

@Component
public class UploadFile {
	public static final String UPLOAD_PATH = "C:\\dev\\work-space\\Spring\\Project\\src\\main\\webapp\\resources\\uploads\\";
	
	public boolean delete(FreeDto free) {
		File file = new File(free.getUploadPath()+free.getUploadName());
		return file.delete();
	}
	
	public boolean newsDelete(NewsDto news) {
		File file = new File(news.getUploadPath()+news.getUploadName());
		return file.delete();
	}
	
	
	public void upload(FreeDto free,
					   MultipartFile upload, 
					   HttpSession session) {
		if(!upload.isEmpty()) {
			// 원본 파일 이름
			String originName = upload.getOriginalFilename();
			
			// 확장자
			String extension = originName.substring(originName.lastIndexOf('.'));
			
			// 현재 년-월-일-시-분-초
			// 2024-06-28-14-18-56
			LocalDateTime now = LocalDateTime.now();
			
			// 년월일시분초
			// 240628141856
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
			String output = now.format(formatter);
			
			// 랜덤문자열 생성
			int stringLength = 8;  // 생성할 랜덤 문자열의 길이
			String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
			
			Random random = new Random();
			String randomString = random.ints(stringLength, 0, characters.length()) // 길이가 stringLength인 난수 생성
										.mapToObj(characters::charAt) // 위에서 생성한 난수를 characters에 매핑
										.map(Object::toString) //char -> String으로 형변한(한글자)
										.collect(Collectors.joining()); // 문자열 결합
			
			// 파일명 : 날짜_랜덤문자열.확장자
			// 240628144013_KenrJKEk.jpg
			String fileName = output+"_"+randomString+extension;
			
			// 경로 + 파일명
			// C:\\dev\\work-space\\Spring\\Project\\src\\main\\webapp\\resources\\uploads\\
			String filePathName = UPLOAD_PATH + fileName;
			
			// 파일 저장하기 전에 Path타입으로 변환
			Path filePath = Paths.get(filePathName);
			
			// 실제로 서버에 업로드하는 코드
			try {
				upload.transferTo(filePath);
				
				free.setUploadPath(UPLOAD_PATH);
				free.setUploadName(fileName);
				free.setUploadOriginName(originName);
				
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	public void newsUpload(NewsDto news,
			MultipartFile upload, 
			HttpSession session) {
		if(!upload.isEmpty()) {
			// 원본 파일 이름
			String originName = upload.getOriginalFilename();
			
			// 확장자
			String extension = originName.substring(originName.lastIndexOf('.'));
			
			// 현재 년-월-일-시-분-초
			// 2024-06-28-14-18-56
			LocalDateTime now = LocalDateTime.now();
			
			// 년월일시분초
			// 240628141856
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
			String output = now.format(formatter);
			
			// 랜덤문자열 생성
			int stringLength = 8;  // 생성할 랜덤 문자열의 길이
			String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
			
			Random random = new Random();
			String randomString = random.ints(stringLength, 0, characters.length()) // 길이가 stringLength인 난수 생성
					.mapToObj(characters::charAt) // 위에서 생성한 난수를 characters에 매핑
					.map(Object::toString) //char -> String으로 형변한(한글자)
					.collect(Collectors.joining()); // 문자열 결합
			
			// 파일명 : 날짜_랜덤문자열.확장자
			// 240628144013_KenrJKEk.jpg
			String fileName = output+"_"+randomString+extension;
			
			// 경로 + 파일명
			// C:\\dev\\work-space\\Spring\\Project\\src\\main\\webapp\\resources\\uploads\\
			String filePathName = UPLOAD_PATH + fileName;
			
			// 파일 저장하기 전에 Path타입으로 변환
			Path filePath = Paths.get(filePathName);
			
			// 실제로 서버에 업로드하는 코드
			try {
				upload.transferTo(filePath);
				
				news.setUploadPath(UPLOAD_PATH);
				news.setUploadName(fileName);
				news.setUploadOriginName(originName);
				
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}
