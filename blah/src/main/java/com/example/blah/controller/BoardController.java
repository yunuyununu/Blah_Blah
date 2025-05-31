package com.example.blah.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.blah.common.util.GCSService;
import com.example.blah.domain.BoardDTO;
import com.example.blah.service.BoardService;
import com.example.blah.service.RedisService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("board/*")
public class BoardController {

	@Autowired
	BoardService service;
	
	@Autowired
	GCSService gcsService;
	
	@Autowired
	RedisService redisService;
	
	// 게시판 목록
	@GetMapping("boards")
	public List<BoardDTO> getBoardList(HttpSession session,@RequestParam(name = "lastBIdx", required = false) Long lastBIdx) {
		
		Integer userIdx = (Integer)session.getAttribute("UserIdx");
		
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
	public void hits(@RequestParam(name = "b_idx") int b_idx, HttpSession session) {
		Object userIdx = session.getAttribute("UserIdx");
		if (userIdx != null) {
			String boardKey = "board_hit_" + b_idx +"_"+ userIdx;
			boolean check = redisService.isFirstView(boardKey);
			if(check == true) { // 처음 조회하면
				service.incrementHit(b_idx); // 조회수 1 증가
			}
		}
	}
	
	// 좋아요 상태
	@GetMapping("likeStatus")
	@ResponseBody
	public Map<String, Object> likeStatus(
	    @RequestParam("b_idx") int h_b_idx, HttpSession session) {
		Object userIdx = session.getAttribute("UserIdx");
	    Map<String, Object> map = new HashMap<>();
	    map.put("h_b_idx", h_b_idx);
	    map.put("h_u_idx", userIdx);
	    
	    int liked = service.likePrevent(map); // 0 or 1
	    int totalLikes = service.heartCount(h_b_idx); // 게시글 총 좋아요 수

	    Map<String, Object> result = new HashMap<>();
	    result.put("liked", liked == 1); // true or false
	    result.put("likeCount", totalLikes);
	    return result;
	}
	
	// 비회원일 때 좋아요 갯수
	@GetMapping("likeCount")
	@ResponseBody
	public Map<String, Object> likeCount(@RequestParam("b_idx") int b_idx) {
	    int totalLikes = service.heartCount(b_idx);
	    Map<String, Object> result = new HashMap<>();
	    result.put("likeCount", totalLikes);
	    return result;
	}
	
	// 좋아요 입력
	@PostMapping("likeInsert")
	public void likeInsert(@RequestBody Map<String, Integer> request, HttpSession session) {
		int h_b_idx = request.get("b_idx");
		int h_u_idx = (int)session.getAttribute("UserIdx");
		Map<String, Object> map = new HashMap<>();
		map.put("h_b_idx", h_b_idx);
		map.put("h_u_idx", h_u_idx);
		int likeCount = service.likePrevent(map);
		System.out.println("등록시 likeCount"+likeCount);
		System.out.println("좋아요 입력 맵=>"+map);
		if (likeCount == 0) { // 첫 좋아요
			service.boardLike(map);
		}
	}
	
	// 좋아요 취소
	@PostMapping("likeDelete")
	public void likeDelete(@RequestBody Map<String, Integer> request, HttpSession session) {
		int h_b_idx = request.get("b_idx");
		int h_u_idx = (int)session.getAttribute("UserIdx");
		Map<String, Object> map = new HashMap<>();
		map.put("h_b_idx", h_b_idx);
		map.put("h_u_idx", h_u_idx);
		int likeCount = service.likePrevent(map);
		System.out.println("삭제시 likeCount"+likeCount);
		System.out.println("좋아요 삭제 맵=>"+map);
		if(likeCount == 1) { // 좋아요 내역 있을때
			service.likeDelete(map);
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
	
	// 게시글 삭제
	@PostMapping("boardDelete")
	public void boardDelete(@RequestBody Map<String, Integer> request) {
		int b_idx = request.get("b_idx");
		service.boardDelete(b_idx);
	}
}
