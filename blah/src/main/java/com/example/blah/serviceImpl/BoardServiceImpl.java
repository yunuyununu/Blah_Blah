package com.example.blah.serviceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.blah.common.util.GCSRequest;
import com.example.blah.common.util.GCSService;
import com.example.blah.domain.AlarmDTO;
import com.example.blah.domain.BoardDTO;
import com.example.blah.domain.FileDTO;
import com.example.blah.mapper.BoardMapper;
import com.example.blah.service.AlarmService;
import com.example.blah.service.BoardService;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	BoardMapper boardMapper;
	
	@Autowired
	GCSService gcsService;
	
	@Autowired
	AlarmService alarmservice;
	
	// 게시글 목록
	@Override
	public List<BoardDTO> getBoard(Long lastBIdx) {
	    if (lastBIdx == null) {
	        return boardMapper.firstList();
	    } else {
	        return boardMapper.plusList(lastBIdx);
	    }
	}
	
	// 게시글 상세
	@Override
	public Map<String, Object> details(int b_idx) {
		return boardMapper.boardDetails(b_idx);
	}
	
	// 게시물 상세 - 이미지파일
	@Override
	public List<Map<String, Object>> boardImages(int b_idx) {
		return boardMapper.boardImages(b_idx);
	}
	
	// 게시글 조회수
	@Override
	public void incrementHit(int b_idx) {
		boardMapper.updateHit(b_idx);
	}
	
	// 좋아요 중복체크
	public int likePrevent (Map<String, Object> map) {
		return boardMapper.likePrevent(map);
	}
	
	// 좋아요 입력
	public void boardLike(Map<String, Object> map) {
		boardMapper.boardLike(map);
		
		int b_idx = (int) map.get("h_b_idx");
		int h_u_idx = (int) map.get("h_u_idx");
		int b_u_idx = (int) map.get("b_u_idx");
		String b_title = (String) map.get("b_title");
		
		if(h_u_idx != b_u_idx) {
			AlarmDTO dto = new AlarmDTO();
			dto.setA_b_idx(b_idx);
			dto.setA_u_idx(b_u_idx);
			dto.setA_type("Like");
			dto.setA_url("/boarddetails/"+b_idx);
			boardMapper.alarmInsert(dto);

			String message = "게시글 \""+b_title + "\"에 좋아요가 추가되었습니다.";
			alarmservice.sendLikeNotification(b_u_idx, message);
			System.out.println("여기 확인해->"+message+"/게시판작성자="+b_u_idx);
		}
	}
	
	// 좋아요 취소
	public void likeDelete(Map<String, Object> map) {
		boardMapper.likeDelete(map);
	}
	
	// 게시글 등록
	@Override
	@Transactional
	public void boardInsert(int u_idx, String b_title, String b_content, List<MultipartFile> images) throws IOException {
		
		BoardDTO dto = new BoardDTO(u_idx,b_title, b_content);
		boardMapper.boardInsert(dto);
		
		if (images != null && !images.isEmpty()) {
		    for (MultipartFile image : images) {
		        // 파일명 처리
		        String originalFilename = image.getOriginalFilename();
		        String uuid = UUID.randomUUID().toString();
		        String fileName = uuid + "_" + originalFilename;

		        // GCS에 업로드
		        GCSRequest gcsRequest = new GCSRequest();
		        gcsRequest.setName(fileName); // GCS에 저장될 파일명
		        gcsRequest.setFile(image); // 실제 파일
		        String publicUrl = gcsService.uploadObject(gcsRequest); // 업로드 실행 후 URL 반환

		        // DB 저장
		        FileDTO imageFile = new FileDTO(dto.getB_idx(), publicUrl);
		        boardMapper.imageInsert(imageFile);
		    }
		}
	}
	
	// 게시물 수정 시
	@Override
	public void boardUpdate(int b_idx) {
		boardMapper.boardUpdate(b_idx);
	}
	
	// 게시물 삭제 시
	@Override
	public void boardDelete(int b_idx) {
		boardMapper.boardDelete(b_idx);
	}
	
	// 하트 갯수
	@Override
	public int heartCount(int b_idx) {
		return boardMapper.heartCount(b_idx);
	}
	
	@Override
	public List<BoardDTO> weeklyBest() {
		return boardMapper.weeklyBest();
	}
	
	@Override
	public List<BoardDTO> monthlyBest() {
		return boardMapper.monthlyBest();
	}
}
