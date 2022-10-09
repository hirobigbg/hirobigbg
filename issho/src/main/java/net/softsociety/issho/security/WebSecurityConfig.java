package net.softsociety.issho.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

	// 설정
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				.antMatchers("/", "/member/join", "/member/idCheck", "/img/**", "/css/**", "/js/**", "/vendor/**", "/domainCheck", "/wrongPath")
				.permitAll()
				/*
				.antMatchers("/project/**").hasAnyAuthority("ROLE_USER", "PM")
				*/
				/*
				 * .antMatchers("").hasAuthority("PM")
				 * 
				 */
				.anyRequest().authenticated().and().formLogin().loginPage("/loginForm")
				.loginProcessingUrl("/member/login_action").permitAll().usernameParameter("memb_mail")
				.passwordParameter("memb_pwd").and().logout().logoutUrl("/member/logout").logoutSuccessUrl("/")
				.permitAll().and().cors().and().httpBasic();

		return http.build();
	}

	// 단방향 비밀번호 암호화
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	
}
