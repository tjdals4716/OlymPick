package com.example.SoftwareEngineering_Project.Config;

import com.example.SoftwareEngineering_Project.Entity.UserEntity;
import com.example.SoftwareEngineering_Project.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final UserRepository userRepository;

    @Autowired
    public WebSocketMessageHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = searchUserName(session);
        sessionMap.put(username, session);
        session.sendMessage(new TextMessage(username + "님이 문의 채팅에 참여하였습니다."));
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
        Long userId = Long.parseLong(uriComponents.getQueryParams().getFirst("userId"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. userId: " + userId));
        return user.getNickname();
    }
}