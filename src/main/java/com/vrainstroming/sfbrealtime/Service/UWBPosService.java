package com.vrainstroming.sfbrealtime.Service;


import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UWBPosService {
    int updateUWBPos(Map dto);
    Map getUwbPos(Map dto);

}
