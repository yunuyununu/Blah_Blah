package com.example.blah.mapper;

import java.util.List;

import com.example.blah.domain.FileDTO;

public interface FileMapper {
	// 첨부파일 저장
	void saveAll(List<FileDTO> files);
}
