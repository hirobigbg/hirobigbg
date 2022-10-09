package net.softsociety.issho.chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @brief WebSocket 관련 설정을 정의한 클래스. 
 * @author 윤영혜
 *
 */

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	ChatHandler chatHandler = new ChatHandler();

	//chatHandler를 웹소켓 핸들러로 정함.
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(chatHandler, "/ws/multiRoom").setAllowedOrigins("http://localhost:9990").withSockJS();
	}

}