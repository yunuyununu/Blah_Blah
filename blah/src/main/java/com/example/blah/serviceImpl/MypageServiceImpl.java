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
import com.example.blah.mapper.MypageMapper;
import com.example.blah.service.BoardService;
import com.example.blah.service.MypageService;

@Service
public class MypageServiceImpl implements MypageService {
	
	@Autowired
	MypageMapper mypageMapper;

	@Override
	public List<BoardDTO> getBoard(Long lastBIdx) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
