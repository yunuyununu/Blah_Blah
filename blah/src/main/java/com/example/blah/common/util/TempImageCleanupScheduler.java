package com.example.blah.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.blah.service.BoardService;

public class TempImageCleanupScheduler {
	@Autowired
    BoardService boardService;

    @Scheduled(fixedRate = 1000 * 60 * 10) // 10분마다 실행
    public void cleanup() {
    	//boardService.cleanupOldTempImages();
    }
}
