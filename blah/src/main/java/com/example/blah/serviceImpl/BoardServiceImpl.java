package com.example.blah.serviceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.blah.domain.BoardDTO;
import com.example.blah.domain.FiletempDTO;
import com.example.blah.mapper.BoardMapper;
import com.example.blah.mapper.FileMapper;
import com.example.blah.service.BoardService;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	BoardMapper boardMapper;
	
	@Autowired
	FileMapper fileMapper;

	@Override
	public List<BoardDTO> getBoard(Long lastBIdx) {
	    if (lastBIdx == null) {
	        return boardMapper.firstList();
	    } else {
	        return boardMapper.plusList(lastBIdx);
	    }
	}
	
	@Override
	public Map<String, Object> details(int b_idx) {
		return boardMapper.boardDetails(b_idx);
	}
	
	@Override
	public void incrementHit(int b_idx) {
		boardMapper.updateHit(b_idx);
	}
	
	@Override
	public void boardInsert(BoardDTO dto) {
//		int idx = dto.getB_idx();
//		
//		try {
//			if (CollectionUtils.isEmpty(files)) {
				boardMapper.boardInsert(dto);
//			} else {
//				for (BoardDTO file : files) {
//					file.setI_idx(idx);
//				}
//				boardMapper.boardInsert(dto);
//				boardMapper.saveAll(files);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("게시글 저장 실패!!!");
//		}
		
	}
	
	@Override
	public void uploadImage(MultipartFile file, int u_idx) throws IOException {
		
		// 파일명 처리
		String originalFilename = file.getOriginalFilename();
		String uuid = UUID.randomUUID().toString();
		String fileName = uuid + "_" + originalFilename;
		//Files.copy(file.getInputStream(), fileName);
		
		FiletempDTO dto = new FiletempDTO();
		dto.setT_image(file);
		dto.setT_u_idx(u_idx);
		
		fileMapper.insertTempImage(dto);
	}
	
	@Override
	public void confirmImageUsage(List<Integer> imageIds) {
		fileMapper.confirmImages(imageIds);
	}
	
	@Override
	public void cleanupOldTempImages() {
		List<FiletempDTO> dto = fileMapper.selectOldTempImages(30);
		for (FiletempDTO img : dto) {
            fileMapper.deleteTempImage(img.getT_idx());
        }
	}
	
}
