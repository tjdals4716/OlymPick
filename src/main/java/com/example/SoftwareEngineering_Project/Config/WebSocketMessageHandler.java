package com.example.SoftwareEngineering_Project.Config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;

@Component
public class WebSocketMessageHandler extends TextWebSocketHandler {
    private final HashMap<String, WebSocketSession> sessionMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = searchUserName(session);
        sessionMap.put(username, session);
        session.sendMessage(new TextMessage("웹소켓 연결 성공: " + username));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String username = searchUserName(session);
        sessionMap.remove(username);
    }

    public void sendNotification(String username, String message) throws Exception {
        WebSocketSession session = sessionMap.get(username);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }

    private String searchUserName(WebSocketSession session) {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(session.getUri().toString()).build();
        return uriComponents.getQueryParams().getFirst("uid");
    }
}