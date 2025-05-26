package com.example.blah.domain;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FiletempDTO {
	private int t_idx;
	private int t_u_idx;
    private MultipartFile t_image;
    private String t_date;
    private String t_status;

}
