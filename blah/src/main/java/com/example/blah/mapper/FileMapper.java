package com.example.blah.mapper;

import java.util.List;

import com.example.blah.domain.FileDTO;
import com.example.blah.domain.FiletempDTO;

public interface FileMapper {
	// 첨부파일 저장
	void saveAll(List<FileDTO> files);
	
	void insertTempImage(FiletempDTO image);
    void confirmImages(List<Integer> ids);
    List<FiletempDTO> selectOldTempImages(int minutes);
    void deleteTempImage(int idx);
}
