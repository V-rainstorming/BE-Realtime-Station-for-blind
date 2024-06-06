package com.vrainstroming.sfbrealtime.mapper;


import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    int updateUserPosition(Map map);
    Map getUserPosition(Map map);
    Map getServiceInfo(Map map);
    List<Map> getRouteInfoAfterGetOnBus(Map map);
    String getWaitingStatus(Map map);
    int registerBilndService(Map map);
    int updateServieStatus(Map map);
    double getDistWithUserAndStation(Map map);
    Double getDistWithUserAndBus(Map map);

}
