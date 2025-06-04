package com.example.blah.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.example.blah.domain.AlarmDTO;
import com.example.blah.domain.BoardDTO;
import com.example.blah.domain.FileDTO;
import com.example.blah.domain.VoteDTO;

public interface BoardMapper {
	// 게시글 목록
	List<Map<String, Object>> boardList(@Param("searchKeyword") String searchKeyword,@Param("page") int page,@Param("offset") int offset);
	
	// 게시물 총 갯수
	int boardTotalCount(String searchKeyword);
	
	// 게시물 상세
	Map<String, Object> boardDetails(int b_idx);
	
	// 게시물 상세 - 이미지파일
	List<Map<String, Object>> boardImages(int b_idx);
	
	// 조회수 증가
	void updateHit(int b_idx);
	
	// 좋아요 중복체크
	int likePrevent (Map<String, Object> map);
	
	// 좋아요 입력
	void boardLike(Map<String, Object> map);
	
	// 좋아요 취소
	void likeDelete(Map<String, Object> map);
	
	// 알람
	void alarmInsert(AlarmDTO dto);
	
	// 게시물 등록
	void boardInsert(BoardDTO dto);
	
	// 게시물 등록 시 이미지 첨부 (다중)
	void imageInsert(FileDTO image);
	
	// 게시물 수정 시
	void boardUpdate(BoardDTO dto);
	
	// 게시물 이미지 삭제
	void imageDelete(String i_image);
	
	// 게시물 삭제 시
	void boardDelete(int b_idx);
	
	// 하트 갯수
	int heartCount(int b_idx);
	
	// 주간 토픽 베스트
	List<BoardDTO> weeklyBest();
	
	// 월간 토픽 베스트
	List<BoardDTO> monthlyBest();
	
	// 투표 정보
	void voteInfoInsert(VoteDTO dto);
	
	// 투표 항목
	void voteOptionInsert(VoteDTO dto);
	
	// 투표 포함 여부
	void voteYN(int v_b_idx);
	
	// 투표 정보
	List<Map<String, Object>> voteInfo(int v_b_idx);
	
	// 투표한 항목 상태
	int voteCheck(Map<String, Object> map);
	// 투표 선택
	void votePick(VoteDTO dto);
	
	// 투표 항목 카운트
	void voteCount(int vr_vo_idx);
	
}
