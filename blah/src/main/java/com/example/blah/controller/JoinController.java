package com.example.blah.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.blah.common.util.GCSRequest;
import com.example.blah.common.util.GCSService;
import com.example.blah.domain.JoinDTO;
import com.example.blah.service.EmailService;
import com.example.blah.service.JoinService;
import com.example.blah.service.RedisService;

import jakarta.mail.MessagingException;
@RestController
@RequestMapping("join/*")
public class JoinController {
	
	@Autowired
	JoinService service;
	
	@Autowired
	PasswordEncoder pwdEncoder;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	GCSService gcsService;
	
	// 이메일 인증번호 전송 
	@PostMapping("emailsend")
	public String sendEmail(@RequestBody Map<String, String> request) throws MessagingException {
		String result ="";
	    String sendEmail = request.get("email");
	    
	    try {
	    	String authCode = emailService.createMailsend(sendEmail);
	    	//System.out.println("이메일 전송 완료!!");
	    	redisService.saveVerificationCode(sendEmail, authCode); // 인증코드 redis에 저장
	    	result = "success";
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	result = "fail";
	    }
	    return result;
	}
	
	@PostMapping("emailCheck")
	public int emailCheck(@RequestBody Map<String, String> request) {
		
		String sendEmail = request.get("email");
		return service.emailCheck(sendEmail);
	}
	
	// 비밀번호찾기 이메일 전송
	@PostMapping("pwEmailsend")
	public String pwEmailSend(@RequestBody Map<String, String> request) throws MessagingException {
		String result ="";
	    String sendEmail = request.get("email");
	    //System.out.println("수신자 이메일주소 : " + sendEmail);
	    int emailCheck = service.emailCheck(sendEmail);
	    if(emailCheck == 1) {
	    	String authCode = emailService.createMailsend(sendEmail);
	    	//System.out.println("이메일 전송 완료!!");
	    	redisService.saveVerificationCode(sendEmail, authCode); // 인증코드 redis에 저장
	    	result = "success";
	    } else {
	    	result = "fail";
	    }
	    return result;
	}
	
	// 인증번호 검증
	@PostMapping("authCodeVerify")
	public String verifyEmail(@RequestBody Map<String, String> request) {
		String sendemail = request.get("email");
		String code = request.get("authcode");
		//System.out.println("수신자 이메일주소 : " + sendemail);
		//System.out.println("인증번호 : " + code);
		
		String result ="";
		if (redisService.verifyEmail(sendemail,code)) {
			result = "success";
        } else {
            result ="false";
        }
		return result;
		
	}
	
	// 아이디 중복확인
	@PostMapping("idCheck")
	public int idCheck(@RequestBody Map<String, String> request) {
		String userid = request.get("userId");
		int checkId = service.idCheck(userid);
		return checkId;
		
	}
	
	// 회원가입
	@PostMapping("userJoin")
	public String join(@RequestParam(name="userId", defaultValue="") String userId, 
			@RequestParam(name="userPw", defaultValue="") String userPw,@RequestParam(name="nickname", defaultValue="") String nickname,
			@RequestParam(name="userTel", defaultValue="") String userTel,@RequestParam(name="email", defaultValue="") String email,
			@RequestParam(name="userFile", defaultValue="") MultipartFile  userFile) {
		
		try {
	        // 비밀번호 암호화
	        String encodedPw = pwdEncoder.encode(userPw);

	        // 파일명 처리
	        String originalFilename = userFile.getOriginalFilename();
	        String uuid = UUID.randomUUID().toString();
	        String fileName = uuid + "_" + originalFilename;

	        // GCS에 업로드
	        GCSRequest gcsRequest = new GCSRequest();
	        gcsRequest.setName(fileName); // GCS에 저장될 파일명
	        gcsRequest.setFile(userFile); // 실제 파일
	        String publicUrl = gcsService.uploadJoinChange(gcsRequest); // 업로드 실행
	        
	        // dto에 담아 보내기
	        JoinDTO dto = new JoinDTO();
	        dto.setU_id(userId);
	        dto.setU_password(encodedPw);
	        dto.setU_nicname(nickname);
	        dto.setU_tel(userTel.replaceAll("-", ""));
	        dto.setU_email(email);
	        dto.setU_file(publicUrl);

	        String result = service.join(dto);
	        return result;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return "fail";
	    }
	}
	
//	public Map<String, Object> login(@RequestParam (name="u_id") String u_id, @RequestParam (name="u_password") String u_password) {
//		String passwd = dao.checkPw(u_id);
//		
//		Map<String, Object> map1 = dao.login(u_id, passwd);
//		Map<String, Object> map = new HashMap<>();
//		
//		String message = "";
//		
//		if(pwdEncoder.matches(u_password, passwd)){ // 로그인 성공
//			map.put("g_email", g_email);
//			map.put("g_name", map1.get("g_name"));
//			map.put("g_level", map1.get("g_level"));
//			map.put("g_idx", map1.get("g_idx"));
//			map.put("g_phone", map1.get("g_phone"));
//			map.put("g_profile", map1.get("g_profile"));
//			map.put("g_photo", map1.get("g_photo"));
//			map.put("message", "success");
//		} else {
//			message = "error";
//		}
//		return map;
//	}
	
	
}
