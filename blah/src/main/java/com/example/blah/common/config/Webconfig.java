package com.example.blah.common.config;


import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class Webconfig implements WebMvcConfigurer {
	public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,OPTIONS,PATCH";

	@Override
	public void addCorsMappings(final CorsRegistry registry) {
		
		// 모든 경로에 대해 CORS 허용
        registry.addMapping("/**") // 모든 URL 패턴에 대해 CORS 정책 적용
                .allowedOrigins("http://localhost:3000") // Vue.js가 3000 포트에서 실행될 경우
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD")
                .allowCredentials(true) // 쿠키를 허용하려면 추가
                .maxAge(3600); // CORS 요청을 캐시할 수 있는 시간(초)
	}
	
	/*
	 * @Override public void addInterceptors(InterceptorRegistry registry) {
	 * registry.addInterceptor(new LoginCheckInterceptor())
	 * .addPathPatterns("/mypage/**") // 로그인 체크할 경로 .excludePathPatterns("/login",
	 * "/join"); // 로그인, 회원가입은 예외 }
	 */
	
	/* vue에서 새로고침 시 에러 해결
	요청을 보내면 서버에서 설정한 값에 의헤 index.html을 반환
	브라우저는 요청을 받아 index.html을 읽으면서 속에 있는 script와 css 요청을 서버에 다시 보냄
	해당 js, css 파일을 받아 렌더링하면서 vue-router에 의해 routes로 설정한 페이지로 이동 */
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource resource = location.createRelative(resourcePath);
                        // 요청한 리소스가 존재하고 읽을 수 있으면 반환, 아니면 index.html 반환
                        return resource.exists() && resource.isReadable() ? resource : new ClassPathResource("/static/index.html");
                    }
                });
    }
}
