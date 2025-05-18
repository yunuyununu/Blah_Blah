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
	private String c_logo;
	private String c_industry;
	private String c_est;
	private String c_intro;
	private String c_number;
	private String c_business;
	private String c_register;
}
