package com.vrainstroming.sfbrealtime.Config;

import com.vrainstroming.sfbrealtime.Service.BusPosUpdateHandler;
import com.vrainstroming.sfbrealtime.Service.MyWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final MyWebSocketHandler myWebSocketHandler;
    private final BusPosUpdateHandler busPosUpdateHandler;

    public WebSocketConfig(MyWebSocketHandler myWebSocketHandler,BusPosUpdateHandler busPosUpdateHandler) {
        this.myWebSocketHandler = myWebSocketHandler;
        this.busPosUpdateHandler = busPosUpdateHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler, "/busPosUpdateDemo").setAllowedOrigins("*");
        registry.addHandler(myWebSocketHandler, "/busPosUpdate").setAllowedOrigins("*");




    }
}
