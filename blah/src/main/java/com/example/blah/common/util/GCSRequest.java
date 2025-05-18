package com.example.blah.common.util;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GCSRequest {
	private String name;
    private MultipartFile file;
}
