package com.example.SoftwareEngineering_Project.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    WebSocketMessageHandler webSocketMessageHandler;

    public WebSocketConfig(WebSocketMessageHandler webSocketMessageHandler) {
        this.webSocketMessageHandler = webSocketMessageHandler;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketMessageHandler, "/test").setAllowedOrigins("*");
    }
}