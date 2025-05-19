package com.example.blah.common.util;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/image")
public class GCSController { // 현재 미사용
	
	private final GCSService gcsService;

    @PostMapping("/upload")
    public ResponseEntity<Void> objectUpload(GCSRequest gcsRequest) throws IOException {

        gcsService.uploadObject(gcsRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
