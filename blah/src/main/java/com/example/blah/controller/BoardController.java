package com.example.blah.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.blah.common.util.GCSRequest;
import com.example.blah.common.util.GCSService;
import com.example.blah.domain.BoardDTO;
import com.example.blah.domain.FileDTO;
import com.example.blah.domain.VoteDTO;
import com.example.blah.service.AlarmService;
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
	
	@Autowired
	AlarmService alarmservice;
	
	// 게시판 목록
	@GetMapping("boards")
	public Map<String, Object> getBoardList(
	    @RequestParam(name = "searchKeyword", defaultValue="") String searchKeyword,
	    @RequestParam(name = "page", defaultValue = "1") int page,
	    @RequestParam(name = "pageSize", defaultValue = "12") int pageSize) {

	    int offset = (page - 1) * pageSize;
	    List<Map<String, Object>> list = service.getBoard(searchKeyword, pageSize, offset);
	    int totalCount = service.getBoardTotalCount(searchKeyword);
	    int totalPages = (int) Math.ceil((double) totalCount / pageSize);

	    Map<String, Object> result = new HashMap<>();
	    result.put("list", list);
	    result.put("totalCount", totalCount);
	    result.put("totalPages", totalPages);
	    result.put("currentPage", page);

	    return result;
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
			System.out.println("조회수 증가 확인111==>"+boardKey);
			boolean check = redisService.isFirstView(boardKey);

			if(check == true) { // 처음 조회하면
				System.out.println("조회수 증가 확인222==>"+check);
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
	public void likeInsert(@RequestBody Map<String, Object> request, HttpSession session) {
		Integer h_b_idx = Integer.parseInt((String) request.get("b_idx"));
		Integer  h_u_idx = (Integer)session.getAttribute("UserIdx");
		Integer  b_u_idx = (Integer)request.get("b_u_idx");
		String b_title = String.valueOf(request.get("b_title"));
		Map<String, Object> map = new HashMap<>();
		map.put("h_b_idx", h_b_idx);
		map.put("h_u_idx", h_u_idx);
		map.put("b_u_idx", b_u_idx);
		map.put("b_title", b_title);
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
	public Map<String, Object> join(HttpSession session,@RequestParam(name="b_title") String b_title,
			@RequestParam(name="b_content") String b_content,
			@RequestParam(name="images", required = false) List<MultipartFile> images) {
		
		Map<String, Object> result = new HashMap<>();
		
		try {
			Integer userIdx = (Integer) session.getAttribute("UserIdx");
			System.out.println("게시판 등록 시 세션아이디=>>"+userIdx);
		    if (userIdx == null) {
		    	result.put("result", "unauthorized");
	            return result;
		    }
		    System.out.println("게시물 제목=>>"+b_title);
		    System.out.println("내용 답장 확인=>>"+b_content);
		    System.out.println("이미지들=>"+images);
	        int b_idx = service.boardInsert(userIdx,b_title,b_content,images);
	        result.put("result", "success");
	        result.put("b_idx", b_idx);
	    } catch (Exception e) {
	        e.printStackTrace();
	        result.put("result", "fail");
	    }
		return result;
	}
	
	// 게시물 수정
	@PostMapping("boardUpdate")
	public void boardUpdate(@RequestBody Map<String, Object> request) {
		Integer b_idx = Integer.parseInt((String)request.get("b_idx"));
		String b_title = String.valueOf(request.get("b_title"));
		String b_content = String.valueOf(request.get("b_content"));
		System.out.println("b_idx : "+b_idx);
		System.out.println("b_title : "+b_title);
		System.out.println("b_content : "+b_content);
		try {
			System.out.println("글/내용만 수정!!!!");
			service.boardUpdate(b_idx,b_title,b_content);
		} catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	// 게시물 수정 (첨부사진 변경)
	@PostMapping("boardImageUpdate")
	public void boardImageUpdate(@RequestParam(name="b_idx") int b_idx, @RequestParam(name="b_title") String b_title,
			@RequestParam(name="b_content") String b_content,
			@RequestParam(name="images", required = false) List<MultipartFile> images,
			@RequestParam(name="originalImages", required = false) List<String> originalImages) {
		try {
			if (originalImages != null && !originalImages.isEmpty()) {
			    for (String originalimage : originalImages) {
			    	 // 2-1. GCS에서 이미지 삭제
		            gcsService.deleteFile(originalimage);

		            // 2-2. DB에서 이미지 레코드 삭제
		            service.imageDelete(originalimage);
			    }
			}
			
			System.out.println("b_idx : "+b_idx);
			System.out.println("b_title : "+b_title);
			System.out.println("b_content : "+b_content);
			System.out.println("images : "+images);
			System.out.println("글/내용/사진까지 수정!!");
	        service.boardImageUpdate(b_idx,b_title,b_content,images);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	// 게시글 삭제
	@PostMapping("boardDelete")
	public void boardDelete(@RequestParam(name="b_idx") int b_idx,
			@RequestParam(name="originalImages", required = false) List<String> originalImages) {
		
		try {
			if (originalImages != null && !originalImages.isEmpty()) {
			    for (String originalimage : originalImages) {
			    	 // 2-1. GCS에서 이미지 삭제
		            gcsService.deleteFile(originalimage);

		            // 2-2. DB에서 이미지 레코드 삭제
		            service.imageDelete(originalimage);
			    }
			}
			service.boardDelete(b_idx);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@GetMapping("weeklyBest")
	public List<BoardDTO> weeklyBest() {
		return service.weeklyBest();
	}
	
	@GetMapping("monthlyBest")
	public List<BoardDTO> monthlyBest() {
		return service.monthlyBest();
	}
	
	// 게시글 투표 등록
	@PostMapping("voteInfoInsert")
	public void voteInfoInsert(HttpSession session,@RequestParam(name="v_b_idx") int v_b_idx,
			@RequestParam(name="v_title") String v_title,@RequestParam(name="options") List<String> options) {
		Object userIdx = session.getAttribute("UserIdx");
		try {
			System.out.println("투표 등록 시 idx=>>"+userIdx);
	        service.voteInfoInsert(v_b_idx,v_title, options);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	// 투표 정보
	@GetMapping("voteInfo")
	public List<Map<String, Object>> voteInfo(@RequestParam(name = "v_b_idx") int v_b_idx) {
		return service.voteInfo(v_b_idx);
	}
	
	// 투표한 항목 상태
	@GetMapping("voteCheck")
	public int voteCheck(HttpSession session,@RequestParam(name = "v_b_idx") int v_b_idx) {
		
		Object userIdx = session.getAttribute("UserIdx");
		
		Map<String, Object> map = new HashMap<>();
		map.put("vr_u_idx", userIdx);
		map.put("v_b_idx", v_b_idx);
		
		int hasVoted =service.voteCheck(map);
		System.out.println("hasVoted===>"+hasVoted);
		
		return hasVoted;
	}
	 
	// 회원 투표 선택
	@PostMapping("votePick")
	public void votePick(HttpSession session, @RequestParam(name = "vr_vo_idx") int vr_vo_idx) {
		
		int vr_u_idx = (int)session.getAttribute("UserIdx");
		
		try {
			System.out.println("회원 투표 선택=>>"+vr_vo_idx);
	        service.votePick(vr_u_idx, vr_vo_idx);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	// 투표베스트
	@GetMapping("voteBest")
	public List<VoteDTO> voteBest() {
		return service.voteBest();
	}
}
