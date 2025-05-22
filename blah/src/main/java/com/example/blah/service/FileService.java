package com.example.blah.service;

import java.util.List;

import com.example.blah.domain.FileDTO;

public interface FileService {
	void saveFiles(int i_b_idx,List<FileDTO> files);
}
