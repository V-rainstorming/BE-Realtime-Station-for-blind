package com.vrainstroming.sfbrealtime.Service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vrainstroming.sfbrealtime.mapper.BusPosMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;


@Component
@Slf4j
public class BusPosUpdateHandler extends TextWebSocketHandler {

    @Autowired
    BusPosMapper busPosMapper;


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Map<String,Object> dto = objectMapper.readValue(payload,Map.class);

            Integer xpos = Integer.parseInt(dto.getOrDefault("x_pos","0").toString());
            Integer ypos = Integer.parseInt(dto.getOrDefault("y_pos","0").toString());
            double dist = Math.sqrt(Math.pow(xpos,2) + Math.pow(ypos,2));
            dto.put("dist",dist);

            busPosMapper.updateBusPosition(dto);
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

}