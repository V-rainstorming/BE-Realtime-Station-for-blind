package com.vrainstroming.sfbrealtime.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BusMapper {
    int updateBusPosition(Map map);
    Map getBusPosition(Map map);
    int updateTempDemoBusPos(Map map);
    Map getTempDemoBusPos(Map map);
    List<Map> getAllRoute(Map map);
    Map findStation(Map map);
    Map findStationByName(Map map);
    List<Map> getAllBusList();
    Map getBusInfo(Map map);
    String getStatoinNameByid(int id);
}
