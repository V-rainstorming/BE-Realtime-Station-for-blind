package com.vrainstroming.sfbrealtime.Service;


import com.vrainstroming.sfbrealtime.mapper.BusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BusPosImpl implements UWBPosService{


    @Autowired
    BusMapper busPosMapper;

    @Override
    public int updateUWBPos(Map dto) {
        return busPosMapper.updateBusPosition(dto);
    }

    @Override
    public Map getUwbPos(Map dto) {
        return busPosMapper.getBusPosition(dto);
    }
}
