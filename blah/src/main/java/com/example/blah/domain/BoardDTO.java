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
    private String u_nicname;
}
