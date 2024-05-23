package com.vrainstroming.sfbrealtime.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface UwbMapper {


    Map getType(Map uuid);

}
