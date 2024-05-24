package com.vrainstroming.sfbrealtime.Service;

import com.vrainstroming.sfbrealtime.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserPosImpl implements UWBPosService{

    @Autowired
    UserMapper userMapper;

    @Override
    public int updateUWBPos(Map dto) {

        return userMapper.updateUserPosition(dto);
    }

    @Override
    public Map getUwbPos(Map dto) {
        return userMapper.getUserPosition(dto);
    }

}
