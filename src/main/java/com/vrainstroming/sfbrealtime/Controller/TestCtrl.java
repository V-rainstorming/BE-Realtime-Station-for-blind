package com.vrainstroming.sfbrealtime.Controller;


import com.vrainstroming.sfbrealtime.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestCtrl {


    @Autowired
    TestMapper testMapper;

    @GetMapping("/test")
    ResponseEntity<?> test(){

        return ResponseEntity.ok().body(testMapper.test());
    }

}
