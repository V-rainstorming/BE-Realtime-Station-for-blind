package com.vrainstroming.sfbrealtime.Service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vrainstroming.sfbrealtime.mapper.BusMapper;
import com.vrainstroming.sfbrealtime.mapper.UwbMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;


@Component
@Slf4j
public class UWBPosUpdateHandler extends TextWebSocketHandler {

    @Qualifier("busPosImpl")
    @Autowired
    UWBPosService busPosService;

    @Qualifier("userPosImpl")
    @Autowired
    UWBPosService userPosService;

    @Qualifier("UWBInfoImpl")
    @Autowired
    UwbModuleService uwbModuleService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Map dto = objectMapper.readValue(payload,Map.class);
            System.out.println(dto.toString());

            String uuid = dto.getOrDefault("uuid","").toString();

            String UwbType = uwbModuleService.getUwbType(dto);

            if (UwbType.equals("BUS")){
                busPosService.updateUWBPos(dto);
            }
            else if(UwbType.equals("USER")) {
                userPosService.updateUWBPos(dto);
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

}