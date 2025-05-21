package com.example.blah.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blah.domain.FileDTO;
import com.example.blah.mapper.FileMapper;
import com.example.blah.service.FileService;

@Service
public class FileServiceImpl implements FileService {
	
	@Autowired
	FileMapper fileMapper;

	@Override
	@Transactional
	public void saveFiles(int i_b_idx,FileDTO files) {
//		int idx = dto.getB_idx();
//		
//		try {
//			if (CollectionUtils.isEmpty(files)) {
//				boardMapper.boardInsert(dto);
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
	
}
