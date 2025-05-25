package com.example.blah.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
	private int cm_idx;
    private int cm_b_idx;
    private int cm_u_idx;
    private String cm_content;
    private String cm_date;
    private String cm_modify;
    private String cm_parent_idx;
    private String u_nicname;
    private String c_nicname;
}
