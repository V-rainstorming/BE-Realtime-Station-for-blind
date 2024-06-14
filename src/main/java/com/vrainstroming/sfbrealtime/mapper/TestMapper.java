package com.vrainstroming.sfbrealtime.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TestMapper {

    List<Map> test();
    Map getAzimuth();
    int setAzimuth(Map dto);
    int busMoveForce();
    int resetBusStation();
    int resetService();

}
