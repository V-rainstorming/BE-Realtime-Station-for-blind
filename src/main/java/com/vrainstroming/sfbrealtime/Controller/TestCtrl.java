package com.vrainstroming.sfbrealtime.Controller;


import com.vrainstroming.sfbrealtime.Service.BusRoute.BusRouteService;
import com.vrainstroming.sfbrealtime.mapper.TestMapper;
import com.vrainstroming.sfbrealtime.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> test(){
        return ResponseEntity.ok().body(testMapper.test());
    }



    @PostMapping("/azimuth")
    public ResponseEntity<?> setAzimuth(@RequestBody Map<String,Object> dto){

        //int updatecnt = testMapper.setAzimuth(dto);
        int updatecnt = 1;

        Map ret = new HashMap<>();
        ret.put("code",updatecnt);


        if (updatecnt==1){
            ret.put("status","Y");
        }
        else{
            ret.put("status","N");
        }
        return ResponseEntity.ok().body(ret);
    }


    @CrossOrigin(origins ="*")
    @GetMapping("/azimuth")
    public ResponseEntity<?> getAzimuth(){
        return ResponseEntity.ok().body(testMapper.getAzimuth());
    }




}
