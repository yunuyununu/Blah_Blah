package com.example.blah.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JoinDTO {
	private int u_idx;
	private String u_id;
	private int u_c_idx;
    private String u_password;
    private String u_nicname;
    private String u_email;
    private String u_tel;
    private String u_file;
    private String u_joindate;
    private String u_withdraw;
    private String u_review;
    private String u_status;
}
