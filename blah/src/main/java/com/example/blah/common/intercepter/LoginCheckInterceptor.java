package com.example.blah.common.intercepter;

import org.springframework.web.servlet.HandlerInterceptor;

import com.example.blah.domain.LoginDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginCheckInterceptor implements HandlerInterceptor {
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        // 1. 세션에서 회원 정보 조회
//        HttpSession session = request.getSession();
//        LoginDTO member = (LoginDTO) session.getAttribute("userId");;
//
//        // 2. 회원 정보 체크
//        // vue => 401 뜨면 로그인 화면으로 전환
//        if (member == null) {
//        	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
//        	System.out.println("401확인==>"+response);
//        	return false;
//        }
//
//        return HandlerInterceptor.super.preHandle(request, response, handler);
//		HttpSession session = request.getSession(false); // 세션이 없으면 null 반환
//
//	    LoginDTO user = (session != null) ? (LoginDTO) session.getAttribute("userId") : null;
//
//	    if (user == null) {
//	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
//	        return false;
//	    }
//
//	    return true;
//    }
}
