package net.softsociety.issho.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.issho.interceptor.ProjectAuthorizationInterceptor;

@Slf4j
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(projectAuthorizationInterceptor()).excludePathPatterns("/", "/push/**", "/member/**", "/loginForm", "/img/**",
				"/savedImg/**", "/css/**", "/js/**", "/vendor/**", "/domainCheck", "/wrongPath", "/project/**", "/ws/multiRoom");
	}
	
	//인터셉터에서 서비스 레이어를 사용하기 위해 별도의 빈을 등록해준다!
	@Bean
	public ProjectAuthorizationInterceptor projectAuthorizationInterceptor() {
		return new ProjectAuthorizationInterceptor();
	}

	//EnableWebMvc을 사용할 경우 css, js, img 등의 정적 리소스까지 mapping 형식으로 읽어들이는 문제가 있었다.
	//정적 리소스 설정하여 해결! 리소스 등록과 핸들러를 관리하는 객체.
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		/* '/js/**'로 호출하는 자원은 '/static/js/' 폴더 아래에서 찾는다. */ 
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
		/* '/css/**'로 호출하는 자원은 '/static/css/' 폴더 아래에서 찾는다. */ 
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
		/* '/img/**'로 호출하는 자원은 '/static/vendor/' 폴더 아래에서 찾는다. */ 
        registry.addResourceHandler("/vendor/**").addResourceLocations("classpath:/static/vendor/");
		/* '/font/**'로 호출하는 자원은 '/static/img/' 폴더 아래에서 찾는다. */ 
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/img/");
        /* 간트 차트 라이브러리 */
        registry.addResourceHandler("/lib/**").addResourceLocations("classpath:/static/lib/");
        /* savedImg 라이브러리*/
        registry.addResourceHandler("/savedImg/**").addResourceLocations("classpath:/static/savedImg/");
	}

	
}
