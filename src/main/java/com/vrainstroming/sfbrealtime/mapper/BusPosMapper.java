package com.vrainstroming.sfbrealtime.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface BusPosMapper {
    int updateBusPosition(Map map);
    Map getBusPosition(Map map);
}
