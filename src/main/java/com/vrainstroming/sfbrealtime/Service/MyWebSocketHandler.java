package com.vrainstroming.sfbrealtime.Service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vrainstroming.sfbrealtime.mapper.BusPosMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;


@Component
@Slf4j
public class MyWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    BusPosMapper busPosMapper;


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
        Map<String,Object> dto = objectMapper.readValue(payload,Map.class);
            busPosMapper.updateTempDemoBusPos(dto);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}