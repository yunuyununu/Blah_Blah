package com.example.blah.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.SecurityFilterChain;
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
import com.example.blah.domain.JoinDTO;
import com.example.blah.service.BoardService;
import com.example.blah.service.FileService;
import com.example.blah.serviceImpl.BoardServiceImpl;
import com.example.blah.serviceImpl.CompanyServiceImpl;
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
	FileService fileservice;
	
	@Autowired
	GCSService gcsService;
	
	// 게시판 목록
	@GetMapping("boards")
	public List<BoardDTO> getBoardList(@RequestParam(name = "lastBIdx", required = false) Long lastBIdx,HttpSession session) {
		System.out.println("lastBIdx=>"+lastBIdx);
		System.out.println("게시판 목록=="+service.getBoard(lastBIdx));
		System.out.println("게시판에서 세션 확인=>"+session.getAttribute("UserIdx"));
		return service.getBoard(lastBIdx);
	}
	
	// 게시글 상세
	@GetMapping("details")
	public Map<String, Object> boardDetails(@RequestParam(name = "b_idx") int b_idx) {
		System.out.println("b_idx=>"+b_idx);
		System.out.println("게시판 상세=="+service.details(b_idx));
	    return service.details(b_idx);
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
	public void join(@RequestParam(name="userId", defaultValue="") int b_u_idx, 
			@RequestParam(name="title", defaultValue="") String b_title,
			@RequestParam(name="content", defaultValue="") String b_content,
			@RequestParam(name="files", defaultValue="") List<BoardDTO> files) {
		
		try {
	        
//			if(files != null) {
//				// 파일명 처리
//				String originalFilename = userFile.getOriginalFilename();
//				String uuid = UUID.randomUUID().toString();z
//				String fileName = uuid + "_" + originalFilename;
//				
//				// GCS에 업로드
//				GCSRequest gcsRequest = new GCSRequest();
//				gcsRequest.setName(fileName); // GCS에 저장될 파일명
//				gcsRequest.setFile(userFile); // 실제 파일
//				gcsService.uploadObject(gcsRequest); // 업로드 실행
//			}
	        
	        // dto에 담아 보내기
			BoardDTO dto = new BoardDTO();
	        dto.setB_u_idx(b_u_idx);
	        dto.setB_title(b_title);
	        dto.setB_content(b_content);
	        // 참부파일 추가

	        //ervice.boardInsert(dto);
	        //return result;

	    } catch (Exception e) {
	        e.printStackTrace();
	        //return "fail";
	    }
	
	}
	
	// 신규 게시글 생성
    @PostMapping("saveBoard")
    public String savePost(@RequestParam(name="userId", defaultValue="") int b_u_idx, 
			@RequestParam(name="title", defaultValue="") String b_title,
			@RequestParam(name="content", defaultValue="") String b_content,
			@RequestParam(name="files", defaultValue="") List<MultipartFile> files) {
    	
    	String result ="";
    	
    	try {
    		// dto에 담아 보내기
    		BoardDTO dto = new BoardDTO();
    		dto.setB_u_idx(b_u_idx);
    		dto.setB_title(b_title);
    		dto.setB_content(b_content);
    		dto.setFiles(files);
    		
    		int id = dto.getB_u_idx();
    		//service.boardInsert(dto);
    		
    		List<FileDTO> filelist = new ArrayList<>();
    		
    		if(files != null) {
    			// 파일명 처리
    			for(int i=0;i<((CharSequence) files).length();i++) {
    				String originalFilename = files.get(i).getOriginalFilename();
    				String uuid = UUID.randomUUID().toString();
    				String fileName = uuid + "_" + originalFilename;
    				
    				// GCS에 업로드
    				GCSRequest gcsRequest = new GCSRequest();
    				gcsRequest.setName(fileName); // GCS에 저장될 파일명
    				gcsRequest.setFile(files.get(i)); // 실제 파일
    				gcsService.uploadObject(gcsRequest); // 업로드 실행
    				//filelist.addAll(files.get(i).getName());
    			}
    			
    		}
    		
    		fileservice.saveFiles(b_u_idx, filelist);
    		result ="success";
    	} catch (Exception e) {
    		e.printStackTrace();
    		result = "fail";
    	}
    	
        return result;
    }
}
