package net.softsociety.issho.manager.service;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Service
public class MailSenderService  {

	private final JavaMailSender mailSender;

	// properties 파일내용을 하기 변수에 주입
	@Value(value = "${spring.mail.username}")
	public String from;

	public void mailSend(String memb_mail,String prj_domain) throws Exception {
		MimeMessage m = mailSender.createMimeMessage();
		MimeMessageHelper h = new MimeMessageHelper(m, "UTF-8");
		h.setFrom(from);
		h.setTo(memb_mail);
		h.setSubject(prj_domain +" 프로젝트에 초대합니다.");
		h.setText("안녕하십니까! "+prj_domain+" 프로젝트에 초대되었습니다.\n 진심으로 환영합니다.\n"
				+ "하기에 제공된 사이트로 접속하시면 간단한 회원가입 후 프로젝트의 일원이 될수있습니다.\n\n"
				+"URL:"
				+ "http://localhost:/9990/issho/"+prj_domain+"/main" 
				+"\n\n감사합니다!");
		
		mailSender.send(m);
	}

	

}