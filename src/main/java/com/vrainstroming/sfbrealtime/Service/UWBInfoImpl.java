package com.vrainstroming.sfbrealtime.Service;

import com.vrainstroming.sfbrealtime.mapper.UwbMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UWBInfoImpl implements UwbModuleService{

    @Autowired
    UwbMapper uwbMapper;

    @Override
    public String getUwbType(Map dto) {

        Map retMap = uwbMapper.getType(dto);

        return retMap.getOrDefault("TYPE","").toString();
    }




}
