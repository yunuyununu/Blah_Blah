package com.example.blah.controller;

import java.util.HashMap;
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
import com.example.blah.domain.CompanyDTO;
import com.example.blah.service.CompanyService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("company/*")
public class CompanyController {
	
	@Autowired
	CompanyService service;
	
	@Autowired
	GCSService gcsService;
	
	// 회사 목록
	@GetMapping("list")
	public Map<String, Object> list(
			@RequestParam(name = "searchKeyword", defaultValue="") String searchKeyword,
		    @RequestParam(name = "page", defaultValue = "1") int page,
		    @RequestParam(name = "pageSize", defaultValue = "15") int pageSize) {
		//List<CompanyDTO> list = service.list(limit, offset,c_name);
		int offset = (page - 1) * pageSize;
	    List<Map<String, Object>> list = service.list(searchKeyword, pageSize, offset);
	    int totalCount = service.companyTotalCount(searchKeyword);
	    int totalPages = (int) Math.ceil((double) totalCount / pageSize);

	    Map<String, Object> result = new HashMap<>();
	    result.put("list", list);
	    result.put("totalCount", totalCount);
	    result.put("totalPages", totalPages);
	    result.put("currentPage", page);
	    System.out.println("여기여기==>"+result);
	    return result;
	}
	
	// 회사 상세페이지
	@GetMapping("details")
	public Map<String, Object> companyDetails(@RequestParam(name = "c_idx") int c_idx) {
	    return service.details(c_idx);
	}
		
	// 회사 목록
	@GetMapping("reviewList")
	public Map<String, Object> reviewList(@RequestParam(name = "r_c_idx") int r_c_idx,@RequestParam(name = "limit") int limit, @RequestParam(name = "offset") int offset) {
		List<CompanyDTO> reviewlist = service.reviewList(r_c_idx,limit, offset);
		int totalCount = service.reviewCount(r_c_idx);
		double companyStar = service.companyStar(r_c_idx);
		
		Map<String, Object> map = new HashMap<>();
		map.put("reviewlist", reviewlist);
		map.put("totalCount", totalCount);
		map.put("companyStar", companyStar);
		  
		return map;
	}
	
	// 회사 신청
	@PostMapping("companyInsert")
	public void companyInsert(@RequestParam(name = "cr_name") String cr_name, 
	@RequestParam(name = "cr_intro") String cr_intro,@RequestParam(name = "cr_business") String cr_business,
	@RequestParam(name = "cr_logo") MultipartFile cr_logo,@RequestParam(name = "cr_est") String cr_est, HttpSession session) {
		int cr_u_idx = (int)session.getAttribute("UserIdx");
		System.out.println("회사 신청 시 세션아이디=>>"+cr_u_idx);
		try {
			// 회사 대표사진 파일 처리
			// 파일명 처리
	        String originalFilename = cr_logo.getOriginalFilename();
	        String uuid = UUID.randomUUID().toString();
	        String fileName = uuid + "_" + originalFilename;

	        // GCS에 업로드
	        GCSRequest gcsRequest = new GCSRequest();
	        gcsRequest.setName(fileName); // GCS에 저장될 파일명
	        gcsRequest.setFile(cr_logo); // 실제 파일
	        String publicUrl = gcsService.uploadCompany(gcsRequest); // 업로드 실행
	        
	        String businessNum = cr_business.replaceAll("-", "");
	        
	        Map<String, Object> map = new HashMap<>();
	        map.put("cr_u_idx", cr_u_idx);
	        map.put("cr_name", cr_name);
	        map.put("cr_intro", cr_intro);
	        map.put("cr_business", businessNum.replaceAll("-", ""));
	        map.put("cr_logo", publicUrl);
	        map.put("cr_est", cr_est.replaceAll("-", ""));
	        System.out.println("회사 신청 시 보내는 데이터=>>"+map);
			service.companyInsert(map);
		} catch (Exception e) {
			System.out.println("회사신청할때 에러남?");
			e.getStackTrace();
		}
		
	}
	
	// 회사 목록
	@GetMapping("score")
	public List<CompanyDTO> companyScore() {
		return service.companyScore();
	}
	
	// 리뷰 존재 유무
	@GetMapping("reviewYN")
	public int reviewYN(HttpSession session) {
		return service.reviewYN((int)session.getAttribute("UserIdx"));
	}
}