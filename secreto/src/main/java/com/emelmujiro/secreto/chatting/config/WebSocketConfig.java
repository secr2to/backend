package com.emelmujiro.secreto.chatting.config;

import com.emelmujiro.secreto.chatting.handler.ChatWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Slf4j
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;

    /*
     * WebSocket 연결을 위해서 Handler 구성
     * */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        log.info("[+] 최초 WebSocket 연결을 위한 등록 Handler");

        registry
                // 클라이언트에서 웹 소켓 연결을 위해 "ws-stomp"라는 엔드포인트로 연결을 시도하면 ChatWebSocketHandler 클래스에서 이를 처리
                .addHandler(chatWebSocketHandler, "ws-stomp")
                // 접속 시도하는 모든 도메인 또는 IP에서 WebSocket 연결을 허용
                .setAllowedOrigins("*");
    }
}