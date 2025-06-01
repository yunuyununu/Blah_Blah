const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  filenameHashing: true,
  transpileDependencies: true,
  // npm run build 타겟 디렉토리 (백엔드쪽)
  outputDir: 'C:/Users/cyw99/Documents/workspace-spring-tool-suite-4-4.29.1.RELEASE/blah/src/main/resources/static', // Build Directory
  publicPath: '/',
  // npm run serve 개발 진행시 포트가 다르기 때문에 프록시 설정해줘야 함
  // target : 백엔드 port가 들어감
  // changeOrigin -> true로 해야 cros 문제 해결 가능
	devServer: {
		proxy: {
      '/': {
        target: 'http://localhost', // Spring Boot Server
        changeOrigin: true,
        //ws: false
      }
    }
	}
})
