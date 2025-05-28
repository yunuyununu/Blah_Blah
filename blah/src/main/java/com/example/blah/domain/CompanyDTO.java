package com.example.blah.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {
	private int c_idx;
	private String c_name;
	private String c_nicname;
	private String c_logo;
	private String c_industry;
	private String c_est;
	private String c_intro;
	private String c_number;
	private String c_business;
	private String c_register;
	private int r_idx;
	private int r_c_idx;
	private int r_u_idx;
	private int r_star;
	private String r_title;
	private String r_content;
	private String r_date;
	private String r_work;
	private String u_nicname;
}
