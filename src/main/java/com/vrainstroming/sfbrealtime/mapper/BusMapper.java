package com.vrainstroming.sfbrealtime.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface BusMapper {
    int updateBusPosition(Map map);
    Map getBusPosition(Map map);
    int updateTempDemoBusPos(Map map);
    Map getTempDemoBusPos(Map map);
}
