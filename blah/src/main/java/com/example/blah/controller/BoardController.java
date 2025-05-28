package com.example.blah.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.blah.common.util.GCSRequest;
import com.example.blah.common.util.GCSService;
import com.example.blah.domain.BoardDTO;
import com.example.blah.domain.FileDTO;
import com.example.blah.service.BoardService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("board/*")
public class BoardController {

	@Autowired
	BoardService service;
	
	@Autowired
	GCSService gcsService;
	
	// 게시판 목록
	@GetMapping("boards")
	public List<BoardDTO> getBoardList( HttpSession session,@RequestParam(name = "lastBIdx", required = false) Long lastBIdx) {
		System.out.println("lastBIdx=>"+lastBIdx);
		System.out.println("게시판 목록=="+service.getBoard(lastBIdx));
		
		Integer userIdx = (Integer) session.getAttribute("UserIdx");
		System.out.println("게시판 조회 시 세션아이디=>>"+userIdx);
		
		return service.getBoard(lastBIdx);
	}
	
	// 게시글 상세
	@GetMapping("details")
	public Map<String, Object> boardDetails(@RequestParam(name = "b_idx") int b_idx) {
	    return service.details(b_idx);
	}
	
	// 게시글 상세 - 이미지 파일
	@GetMapping("boardImages")
	public List<Map<String, Object>> boardImages(@RequestParam(name = "b_idx") int b_idx) {
		return service.boardImages(b_idx);
    }
	
	// 게시글 조회수 증가
	@PostMapping("hits")
	public void hits(@RequestParam(name = "b_idx") int b_idx, HttpServletRequest request, HttpServletResponse response) {
		String cookieName = "board_hit_" + b_idx;
		Cookie[] cookies = request.getCookies(); // 클라이언트 측 모든 쿠키 가져옴
		boolean alreadyHit = false;
		
		// 쿠키값이 null이 아닌 경우
		if(cookies != null) {
			for(Cookie cookie : cookies) { // 모든 쿠키 검사
				if(cookie.getName().equals(cookieName)) { // 이미 조회한 경우
					alreadyHit = true;
					break;
				}
			}
		}
		
		// 쿠키값 X -> 새로 생성
		if(alreadyHit == false) {
			service.incrementHit(b_idx); // 쿠키값 1 증가
			
			Cookie hitCookie = new Cookie(cookieName, "true");
			hitCookie.setMaxAge(30 * 60); // 30분
			hitCookie.setPath("/");
			response.addCookie(hitCookie); // 쿠키값 클라이언트측으로 전달
		}
		
	}
	
	// 게시글 등록
	@PostMapping("boardInsert")
	public String join(HttpSession session,@RequestParam(name="b_title") String b_title,
			@RequestParam(name="b_content") String b_content,
			@RequestParam(name="images", required = false) List<MultipartFile> images) {
		
		String result = "";
		try {
			Integer userIdx = (Integer) session.getAttribute("UserIdx");
			System.out.println("게시판 등록 시 세션아이디=>>"+userIdx);
		    if (userIdx == null) {
		        return "fail"; // 또는 "unauthorized"
		    }
	        service.boardInsert(userIdx,b_title,b_content,images);
	        result = "success";

	    } catch (Exception e) {
	        e.printStackTrace();
	        result = "fail";
	    }
		return result;
	}
	
}
