package com.emelmujiro.secreto.chatting.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final ConcurrentHashMap<String, WebSocketSession> CLIENT_SESSION = new ConcurrentHashMap<>();

    /**
     * [연결 성공] WebSocket 협상이 성공적으로 완료되고 WebSocket 연결이 열려 사용할 준비가 된 후 호출
     * - 성공을 하였을 경우 session 값을 추가
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        System.out.println("[+] afterConnectionEstablished :: " + session.getId());

        CLIENT_SESSION.put(session.getId(), session);
    }

    /**
     * [메시지 전달] 새로운 WebSocket 메시지가 도착했을 때 호출
     * - 전달 받은 메시지를 순회하면서 메시지를 전송
     * - message.getPayload()를 통해 메시지가 전달
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

        log.info("[+] handleTextMessage :: " + session);
        log.info("[+] handleTextMessage :: " + message.getPayload());

        CLIENT_SESSION.forEach((key, value) -> {

            log.info("key :: " + key + "  value :: " + value);

            if (!key.equals(session.getId())) {  //같은 아이디가 아니면 메시지를 전달합니다.
                try {
                    value.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * [소켓 종료 및 전송 오류] WebSocket 연결이 어느 쪽에서든 종료되거나 전송 오류가 발생한 후 호출
     * - 종료 및 실패하였을 경우 해당 세션을 제거
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException {

        CLIENT_SESSION.remove(session);

        log.info("[+] afterConnectionClosed - Session: " + session.getId() + ", CloseStatus: " + status);
    }

}
