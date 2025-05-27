package com.example.blah.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
	private int u_idx;
	private String u_id;
    private String u_password;
    private String u_nicname;
    private String u_email;
    private String u_phone;
    private String u_withdraw;
}
