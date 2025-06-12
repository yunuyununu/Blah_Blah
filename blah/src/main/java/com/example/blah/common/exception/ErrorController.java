package com.example.blah.common.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
public class ErrorController { // 현재 미사용
	

	 @RequestMapping("/errorpage")
    public String handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        if (statusCode == null) {
            return "error"; // 기본 error.html
        }
        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            return "error-404"; // 404.html
        } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return "error-500"; // 500.html
        }
        return "error"; // 기타 에러 페이지
    }
}
