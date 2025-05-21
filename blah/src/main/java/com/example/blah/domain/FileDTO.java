package com.example.blah.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {
	private int i_idx;
    private int i_b_idx;
    private String i_image;
    private String i_date;
    
    public void setI_b_id(int i_b_idx) {
        this.i_b_idx = i_b_idx;
    }
}
