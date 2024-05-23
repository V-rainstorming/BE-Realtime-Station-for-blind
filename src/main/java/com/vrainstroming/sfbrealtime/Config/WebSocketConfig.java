package com.vrainstroming.sfbrealtime.Config;

import com.vrainstroming.sfbrealtime.Service.UWBPosUpdateHandler;
import com.vrainstroming.sfbrealtime.Service.MyWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final MyWebSocketHandler myWebSocketHandler;
    private final UWBPosUpdateHandler uwbPosUpdateHandler;

    public WebSocketConfig(MyWebSocketHandler myWebSocketHandler, UWBPosUpdateHandler uwbPosUpdateHandler) {
        this.myWebSocketHandler = myWebSocketHandler;
        this.uwbPosUpdateHandler = uwbPosUpdateHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler, "/busPosUpdateDemo").setAllowedOrigins("*");
        registry.addHandler(uwbPosUpdateHandler, "/busPosUpdate").setAllowedOrigins("*");




    }
}
