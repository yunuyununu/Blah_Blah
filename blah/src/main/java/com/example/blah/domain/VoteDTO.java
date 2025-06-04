package com.example.blah.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoteDTO {
	private int v_idx;
    private int v_b_idx;
    private String v_title;
    private String v_date;
    private int vo_idx;
    private int vo_v_idx;
    private String vo_content;
    private int vo_count;
    private int vr_idx;
    private int vr_u_idx;
    private int vr_vo_idx;
    private String vr_date;
    
    public VoteDTO(int v_b_idx, String v_title) {
        this.v_b_idx = v_b_idx;
        this.v_title = v_title;
    }
    
    public VoteDTO(String vo_content,int vo_v_idx) {
        this.vo_content = vo_content;
        this.vo_v_idx = vo_v_idx;
    }
    
    public VoteDTO(int vr_u_idx, int vr_vo_idx) {
        this.vr_u_idx = vr_u_idx;
        this.vr_vo_idx = vr_vo_idx;
    }
}


