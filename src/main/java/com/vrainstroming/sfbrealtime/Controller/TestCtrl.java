package com.vrainstroming.sfbrealtime.Controller;


import com.vrainstroming.sfbrealtime.Service.BusRoute.BusRouteService;
import com.vrainstroming.sfbrealtime.mapper.TestMapper;
import com.vrainstroming.sfbrealtime.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    @CrossOrigin(origins ="*")
    @GetMapping("/test")
    ResponseEntity<?> test(){

        return ResponseEntity.ok().body(testMapper.test());
    }

}
