package com.vrainstroming.sfbrealtime.mapper;


import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface UserMapper {

    int updateUserPosition(Map map);
    Map getUserPosition(Map map);

}
