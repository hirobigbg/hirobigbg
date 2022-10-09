package net.softsociety.issho.sse;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/push")
public class SSEController {
	
	@Autowired
	SSEService sseService;
	
	@GetMapping(value="/subscribe", produces="text/event-stream;charset=utf-8")
	@ResponseBody
	public SseEmitter push(@AuthenticationPrincipal UserDetails user, @RequestHeader(value="Last-Event-ID", required = false, defaultValue="") String LastEventId) {
		
		String id = user.getUsername();
		
		log.debug("subscribe 시작");
		
		return sseService.subscribe(id, LastEventId);
		
	}

}

