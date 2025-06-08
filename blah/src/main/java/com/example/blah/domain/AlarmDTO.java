package com.example.blah.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmDTO {
	private int a_idx;
    private int a_b_idx;
    private int a_receiver_idx;
    private int a_sender_idx;
    private String a_type;
    private String a_url;
    private String a_date;
    private String a_read;
    private String u_nicname;
    private String b_title;
    private String r_u_name;
    private String s_u_name;
}
