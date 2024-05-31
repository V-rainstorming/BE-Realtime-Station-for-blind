package com.vrainstroming.sfbrealtime.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BusRouteMapper {
    Map getBusPosByBusNo(Map map);
    Map getBusRoute(Map map);

    int updateBusNowStation(Map dto);

    Map getStationPos(Map dto);

    Map getVehicleNoByUUID(Map dto);

    void updateBusState(Map dto);
    List<Map> getRouteByBusNum(Map dto);
    int getStationDist(Map dto);
}
