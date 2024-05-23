package com.vrainstroming.sfbrealtime.Service;


import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UwbModuleService {

    String getUwbType(Map dto);


}
