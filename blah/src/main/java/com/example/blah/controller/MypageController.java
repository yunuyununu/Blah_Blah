package com.example.blah.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.blah.common.util.GCSRequest;
import com.example.blah.common.util.GCSService;
import com.example.blah.service.MypageService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("mypage/*")
public class MypageController {
	
	@Autowired
	MypageService service;
	
	@Autowired
	GCSService gcsService;
	
	// 마이페이지 상세
	@GetMapping("info")
	public Map<String, Object> mypage(HttpSession session) {
	    return service.mypage((int)session.getAttribute("UserIdx"));
	}
	
	@PostMapping("companyChange")
	public void companyChange(@RequestParam(name = "u_file") MultipartFile u_file, HttpSession session) {
	
		try {
			// 회사 대표사진 파일 처리
			// 파일명 처리
	        String originalFilename = u_file.getOriginalFilename();
	        String uuid = UUID.randomUUID().toString();
	        String fileName = uuid + "_" + originalFilename;

	        // GCS에 업로드
	        GCSRequest gcsRequest = new GCSRequest();
	        gcsRequest.setName(fileName); // GCS에 저장될 파일명
	        gcsRequest.setFile(u_file); // 실제 파일
	        gcsService.uploadObject(gcsRequest); // 업로드 실행
	        System.out.println("구글클라우드 업로드=>>"+gcsRequest);
	        
	        Map<String, Object> map = new HashMap<>();
	        map.put("u_idx", (int)session.getAttribute("UserIdx"));
			map.put("u_file", fileName);
	        System.out.println("회사 변경 시 보내는 맵=>>"+map);
			service.companyChange(map);
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	// 휴대전화번호 수정
	@PostMapping("phoneUpdate")
	public void phoneUpdate(@RequestBody Map<String, String> request, HttpSession session) {
		Map<String, Object> map = new HashMap<>();
        map.put("u_idx", (int)session.getAttribute("UserIdx"));
		map.put("u_phone", request.get("u_phone").replaceAll("-", ""));
		service.phoneUpdate(map);
	}
	
	// 닉네임 수정
	@PostMapping("nicnameUpdate")
	public void nicnameUpdate(@RequestBody Map<String, String> request, HttpSession session) {
		Map<String, Object> map = new HashMap<>();
        map.put("u_idx", (int)session.getAttribute("UserIdx"));
		map.put("u_nicname", request.get("u_nicname"));
		service.nicnameUpdate(map);
	}
	
	// 회원 탈퇴
	@PostMapping("userWithdraw")
	public void userWithdraw(HttpSession session) {
		if(session.getAttribute("UserIdx") != null) {
			service.userWithdraw((int)session.getAttribute("UserIdx"));
			
			session.invalidate(); 
		}
	}
}
