package com.vrainstroming.sfbrealtime.Service.BusRoute;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface BusRouteService {

    Map setBusState(Map bus_no);
    Map getBusState(Map dto);

    Map getNearestBusStation(Map dto);
    Map findDestStation(Map dto);
    List<Map> findBusRoute(Map map);
    Map getDistInfo(Map map);
    Map getWaitingInfo(Map map);
    Map getOffInfo(Map map);



}
