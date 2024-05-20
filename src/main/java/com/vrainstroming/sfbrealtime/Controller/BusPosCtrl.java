package com.vrainstroming.sfbrealtime.Controller;


import com.vrainstroming.sfbrealtime.mapper.BusPosMapper;
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
    BusPosMapper busPosMapper;


    @CrossOrigin(origins ="*")
    @GetMapping("/sse")
    public SseEmitter streamSse(
            @RequestParam(value = "uuid") String uuid) {

        SseEmitter emitter = new SseEmitter();
         ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

        executor.scheduleAtFixedRate(() -> {
            try {


                Map dto = new HashMap<>();
                dto.put("uuid", uuid);

                dto = busPosMapper.getBusPosition(dto);
                emitter.send(dto.toString());

            } catch (IOException e) {
                emitter.completeWithError(e);
                executor.shutdown();
            }
        }, 0, 100, TimeUnit.MILLISECONDS);


        emitter.onCompletion(executor::shutdown);
        emitter.onTimeout(executor::shutdown);
        emitter.onError((e) -> executor.shutdown());

        return emitter;
    }


}
