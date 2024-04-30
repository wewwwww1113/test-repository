package com.kh.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.oreilly.servlet.multipart.FileRenamePolicy;

public class MyFileRenamePolicy implements FileRenamePolicy{

	//파일명 변경처리하는 객체 
	@Override
	public File rename(File origin) {
		//원본 파일명 추출
		String originName = origin.getName();
		//원본파일명 : hello.txt / 귀여운사진.jpg / 미니.png 
		//원본파일명에서 확장자를 추출하기
		//현재시간 + 랜덤수 + 확장자
		//1.업로드시간 추출 20240423120943 
		String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		//2.5자리 랜덤값 추출하기 
		int ranNum = (int)(Math.random()*90000)+10000; 
		
		//3.원본파일명에서 확장자만 추출하기
		//확장자는 파일명에서 뒤에서부터 처음 만나는 . 을 기준으로 뒤에 있는 문자열이된다.
		//뒤에서부터 .의 위치를 찾아내어 substring으로 잘라주기 
		String ext = originName.substring(originName.lastIndexOf(".")); 
		
		//변경할 이름 하나로 합쳐주기 
		String changeName = time+ranNum+ext;
		//원본파일이 있던곳에 새로운 이름으로 파일객체를 만들어 전달해야한다.
		//원본파일 있던 위치 추출 : 파일객체.getParent()
		return new File(origin.getParent(),changeName);
	}

}
