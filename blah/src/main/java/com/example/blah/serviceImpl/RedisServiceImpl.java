package com.example.blah.serviceImpl;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.blah.service.RedisService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
	
	 private final RedisTemplate<String, String> redisTemplate;
	 
	 private static final String VERIFICATION_CODE_KEY_PREFIX = "verification-code:";

	 @Override
    public void setDataWithExpire(String key, String value, long seconds) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(seconds));
    }

	 @Override
    public String getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

	 @Override
    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
	 
	// redis에 인증번호 저장
	 @Override
	public void saveVerificationCode(String email, String verificationCode) {
		 // redis에 저장할 키 생성
		 String key = VERIFICATION_CODE_KEY_PREFIX + email;
		 // .opsForValue() = 단순한 String 값(value)을 저장할 때 사용하는 메서드
		 // 유효기간 : 5분 -> 지나면 자동 삭제됨
		 redisTemplate.opsForValue().set(key, verificationCode, 3, TimeUnit.MINUTES);
	 }
	 
	 // 인증번호 검증 -> 일치하면 true, 아니면 false 반환
	 @Override
	 public boolean verifyEmail(String email, String verificationCode) {
		 // redis에 저장된 인증번호를 꺼내기 위해 key 생성
        String key = VERIFICATION_CODE_KEY_PREFIX + email;
        System.out.println("검증할때 키 값: " + key);
        // redis에 저장한 키 값 넣고 검증
        String savedCode = redisTemplate.opsForValue().get(key);
        System.out.println("저장된 인증번호: " + savedCode);

        // saveCode가 널이 아니고, 사용자 입력값과 일치하면
        if (savedCode != null && savedCode.equals(verificationCode)) {
        	System.out.println("인증번호 2개: " + savedCode + "/" + verificationCode);
            redisTemplate.delete(key); // 해당 key값 삭제
            System.out.println("삭제까지 완료");
            return true; // 인증 성공
        }
        return false; // 인증 실패
    }
}
