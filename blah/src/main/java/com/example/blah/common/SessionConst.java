package com.example.blah.common;

public class SessionConst {
	public static final String LOGIN_USERID = "UserId";
}
//String userId = request.get("userId");
//String userPw = request.get("userPw");
//System.out.println("사용자 아이디 : " + userId);
//System.out.println("사용자 비밀번호 : " + userPw);
//
//LoginDTO login = service.userLogin(userId,userPw);
//
//if (login != null) { // 로그인 성공
//	session.setAttribute(SessionConst.LOGIN_USERID, login.getU_id());
//	System.out.println("세션 아이디=="+SessionConst.LOGIN_USERID);
//	return "success";
//} else {
//	return "fail";
//}