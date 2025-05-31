package com.example.blah.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
	private int b_idx;
    private int b_u_idx;
    private String b_title;
    private String b_content;
    private String b_hits;
    private String b_date;
    private String b_modify;
    private String b_vote;
    private String b_delete;
    private String u_nicname;
    private long lastBIdx;
    private String c_name;
    private int i_idx;
    private int i_b_idx;
    private String i_image;
    private int h_b_idx;
    private int h_u_idx;
    private int heart_count;
    
    public BoardDTO(int userId, String title, String content) {
        this.b_u_idx = userId;
        this.b_title = title;
        this.b_content = content;
    }
}
