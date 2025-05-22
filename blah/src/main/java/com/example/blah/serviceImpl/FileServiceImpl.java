package com.example.blah.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.example.blah.domain.FileDTO;
import com.example.blah.mapper.FileMapper;
import com.example.blah.service.FileService;

@Service
public class FileServiceImpl implements FileService {
	
	@Autowired
	FileMapper fileMapper;

	@Override
	@Transactional
	public void saveFiles(int i_b_idx,List<FileDTO> files) {
		if (CollectionUtils.isEmpty(files)) {
	        return;
	    }
	    for (FileDTO file : files) {
	        file.setIbidx(i_b_idx);
	    }
	    fileMapper.saveAll(files);
	}
	
}
