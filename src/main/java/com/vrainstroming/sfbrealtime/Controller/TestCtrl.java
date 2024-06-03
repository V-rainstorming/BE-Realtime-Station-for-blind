package com.vrainstroming.sfbrealtime.Controller;


import com.vrainstroming.sfbrealtime.Service.BusRoute.BusRouteService;
import com.vrainstroming.sfbrealtime.mapper.TestMapper;
import com.vrainstroming.sfbrealtime.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestCtrl {


    @Autowired
    TestMapper testMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    BusRouteService busRoute;

    @GetMapping("/test")
    ResponseEntity<?> test(){

        Map dto = new HashMap<>();
        dto.put("service_id",1);
        return ResponseEntity.ok().body(busRoute.getWaitingInfo(dto));
    }

}
