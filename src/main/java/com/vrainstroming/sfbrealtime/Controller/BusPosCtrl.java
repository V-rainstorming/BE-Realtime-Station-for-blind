package com.vrainstroming.sfbrealtime.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vrainstroming.sfbrealtime.Service.BusRoute.BusRouteService;
import com.vrainstroming.sfbrealtime.Service.UWBPosService;
import com.vrainstroming.sfbrealtime.Service.UwbModuleService;
import com.vrainstroming.sfbrealtime.mapper.BusMapper;
import com.vrainstroming.sfbrealtime.mapper.BusRouteMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class BusPosCtrl {

    final int CDist = 30;

    @Autowired
    BusMapper busPosMapper;

    @Autowired
    BusRouteMapper busRouteMapper;

    @Qualifier("busPosImpl")
    @Autowired
    UWBPosService busPosService;

    @Qualifier("userPosImpl")
    @Autowired
    UWBPosService userPosService;

    @Qualifier("UWBInfoImpl")
    @Autowired
    UwbModuleService uwbModuleService;


    @Qualifier("busRouteImpl")
    @Autowired
    BusRouteService busRoute;

    @CrossOrigin(origins ="*")
    @GetMapping("/getRealtimeBusInfoDemo")
    public SseEmitter streamSse(
            @RequestParam(value = "bus_id") String bus_id) {

        SseEmitter emitter = new SseEmitter(3600 * 1000L);
         ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

        executor.scheduleAtFixedRate(() -> {
            try {

                Map dto = new HashMap<>();
                dto.put("bus_id", bus_id);

                dto = busPosMapper.getTempDemoBusPos(dto);
                ObjectMapper objmapper = new ObjectMapper();

                emitter.send(objmapper.writeValueAsString(dto));

            } catch (IOException e) {
                emitter.completeWithError(e);
                executor.shutdown();
            }
        }, 0, 300, TimeUnit.MILLISECONDS);


        emitter.onCompletion(executor::shutdown);
        emitter.onTimeout(executor::shutdown);
        emitter.onError((e) -> executor.shutdown());

        return emitter;
    }


    @CrossOrigin(origins ="*")
    @GetMapping("/getRealtimeUWBPOS")
    public SseEmitter streamUWBPOS(
            @RequestParam(value = "uuid") String uuid) {

        SseEmitter emitter = new SseEmitter(3600 * 1000L);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

        executor.scheduleAtFixedRate(() -> {
            try {

                Map dto = new HashMap<>();
                dto.put("uuid", uuid);

                String UwbType = uwbModuleService.getUwbType(dto);

                if (UwbType.equals("BUS")){
                   dto =  busPosService.getUwbPos(dto);
                }
                else if(UwbType.equals("USER")) {
                   dto =  userPosService.getUwbPos(dto);
                }

                ObjectMapper objmapper = new ObjectMapper();
                emitter.send(objmapper.writeValueAsString(dto));

            } catch (IOException e) {
                emitter.completeWithError(e);
                executor.shutdown();
            }
        }, 0, 300, TimeUnit.MILLISECONDS);


        emitter.onCompletion(executor::shutdown);
        emitter.onTimeout(executor::shutdown);
        emitter.onError((e) -> executor.shutdown());

        return emitter;
    }

    @CrossOrigin(origins ="*")
    @PostMapping("/RegisterService")
    public ResponseEntity<?> RegisterService(@RequestBody Map map){
        //bus_id depature_station_id destination_station_id


        Map ret = new HashMap<>();
        ret.put("status","Y");
        ret.put("Code",200);

       int t = busRoute.RegisterService(map);

       ret.put("service_id",t);



        return ResponseEntity.ok().body(ret);

    }


    @CrossOrigin(origins ="*")
    @GetMapping("/MobileBusBlindControl")
    public SseEmitter getBusNowRoute(
            @RequestParam(value = "service_id") String service_id) {

        SseEmitter emitter = new SseEmitter(3600 * 1000L);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

        executor.scheduleAtFixedRate(() -> {
            try {

                Map dto = new HashMap<>();
                dto.put("service_id",service_id);
                Map ret = new HashMap<>();
                String userState = busRoute.getWaitingStatusInfo(dto);
                if(userState.equals("findStation")){
                    if(busRoute.getDistWithUserAndStation(dto) < CDist){
                        busRoute.ServieStatustoWaiting(dto);
                    }
                    ret.put("bus_data",busRoute.getWaitingInfo(dto));
                }
                else if(userState.equals("waiting")){
                    ret.put("bus_data",busRoute.getWaitingInfo(dto));
                }
                else
                {
                    ret.put("bus_data",busRoute.getOffInfo(dto));
                }

                ////////////////////////////


                ret.put("user_status",userState);

                ObjectMapper objmapper = new ObjectMapper();
                emitter.send(objmapper.writeValueAsString(ret));

            } catch (IOException e) {
                emitter.completeWithError(e);
                executor.shutdown();
            }
        }, 0, 300, TimeUnit.MILLISECONDS);


        emitter.onCompletion(executor::shutdown);
        emitter.onTimeout(executor::shutdown);
        emitter.onError((e) -> executor.shutdown());

        return emitter;
    }

//
//    @CrossOrigin(origins ="*")
//    @GetMapping("/getOffInfo")
//    public SseEmitter getOffInfoSSE(
//            @RequestParam(value = "service_id") String service_id) {
//
//        SseEmitter emitter = new SseEmitter(3600 * 1000L);
//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
//
//        executor.scheduleAtFixedRate(() -> {
//            try {
//
//                Map dto = new HashMap<>();
//                dto.put("service_id",service_id);
//
//                List<Map> ret = busRoute.getOffInfo(dto);
//
//                ObjectMapper objmapper = new ObjectMapper();
//                emitter.send(objmapper.writeValueAsString(ret));
//
//            } catch (IOException e) {
//                emitter.completeWithError(e);
//                executor.shutdown();
//            }
//        }, 0, 300, TimeUnit.MILLISECONDS);
//
//
//        emitter.onCompletion(executor::shutdown);
//        emitter.onTimeout(executor::shutdown);
//        emitter.onError((e) -> executor.shutdown());
//
//        return emitter;
//    }


    @PostMapping("/BlindBusRoute")
    ResponseEntity<?> BlindBusRoute(@RequestBody Map map){ // uuid , station_name

        Map dto = new HashMap<>();
        Map ret = new HashMap<>();

        ret.put("status","Y");
        ret.put("code",200);



        Map userPosMap = userPosService.getUwbPos(map); // uuid 필요

        double user_xpos = (double) userPosMap.getOrDefault("x_pos",0);
        double user_ypos = (double) userPosMap.getOrDefault("y_pos",0);

        dto.put("user_xpos",user_xpos);
        dto.put("user_ypos",user_ypos);

        //목적지 찾기
        Map dest_bs_info = busRoute.findDestStation(map);
        //가장 가까운정류장 찾기
        Map nearst_bs_info = busRoute.getNearestBusStation(dto);


        log.info("가까운 정류장 정보 {} 도착지 정류장 정보 {}",nearst_bs_info,dest_bs_info);


        if(dest_bs_info == null){
            ret.put("status","N"); // 버스정보
            ret.put("code",513);
            return ResponseEntity.ok().body(ret);
        }

        int start_id = (int) nearst_bs_info.get("id");
        int dest_id = (int) dest_bs_info.get("id");

        dto.put("start_id",start_id);
        dto.put("dest_id",dest_id);


        List<Map> routeList = busRoute.findBusRoute(dto);
        ret.put("data",routeList);

        return ResponseEntity.ok().body(ret);
    }

    @PostMapping("/onboardPassenger")
    public ResponseEntity<?> onboardPassenger(@RequestBody Map res){

        Map updateInfoMap = new HashMap<>();

        try {
            updateInfoMap.put("update_code",busRoute.ServieStatustoOnboard(res));
            updateInfoMap.put("status","Y"); // 버스정보
            updateInfoMap.put("code",200);
        }
        catch (Exception e){
            updateInfoMap.put("status","N"); // 버스정보
            updateInfoMap.put("code",500);
        }

        return  ResponseEntity.ok().body(updateInfoMap);
    }


}
