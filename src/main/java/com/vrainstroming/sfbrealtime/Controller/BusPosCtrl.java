package com.vrainstroming.sfbrealtime.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vrainstroming.sfbrealtime.mapper.BusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
public class BusPosCtrl {

    @Autowired
    BusMapper busPosMapper;

    @CrossOrigin(origins ="*")
    @GetMapping("/getRealtimeBusInfoDemo")
    public SseEmitter streamSse(
            @RequestParam(value = "bus_id") String bus_id) {

        SseEmitter emitter = new SseEmitter(3600 * 1000L);
         ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

        executor.scheduleAtFixedRate(() -> {
            try {

                Map dto = new HashMap<>();
                dto.put("bus_id", bus_id);

                dto = busPosMapper.getTempDemoBusPos(dto);
                ObjectMapper objmapper = new ObjectMapper();

                emitter.send(objmapper.writeValueAsString(dto));

            } catch (IOException e) {
                emitter.completeWithError(e);
                executor.shutdown();
            }
        }, 0, 300, TimeUnit.MILLISECONDS);


        emitter.onCompletion(executor::shutdown);
        emitter.onTimeout(executor::shutdown);
        emitter.onError((e) -> executor.shutdown());

        return emitter;
    }



}
