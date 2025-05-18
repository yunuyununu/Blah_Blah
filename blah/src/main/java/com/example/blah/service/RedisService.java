package com.example.blah.service;


public interface RedisService {
	
	void saveVerificationCode(String email, String verificationCode);
	
	boolean verifyEmail(String email, String verificationCode);
	
	void setDataWithExpire(String key, String value, long seconds);

	String getData(String key);

    void deleteData(String key);
}
