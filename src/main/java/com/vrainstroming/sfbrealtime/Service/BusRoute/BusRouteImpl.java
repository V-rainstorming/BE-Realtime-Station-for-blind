package com.vrainstroming.sfbrealtime.Service.BusRoute;

import com.vrainstroming.sfbrealtime.mapper.BusMapper;
import com.vrainstroming.sfbrealtime.mapper.BusRouteMapper;
import com.vrainstroming.sfbrealtime.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BusRouteImpl implements BusRouteService {



    static final int update_portion = 90;

    int Cdist = 300;

    @Autowired
    BusRouteMapper busRouteMapper;

    @Autowired
    BusMapper busMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public Map setBusState(Map dto) {

        dto = busRouteMapper.getVehicleNoByUUID(dto);

        Map now_bus_pos = busRouteMapper.getBusPosByBusNo(dto);
        int now_station_no = (int) now_bus_pos.get("now_station_no");
        int next_station_no = (int) now_bus_pos.get("next_station_no");
        int step2_station_no = 0;


        double y_pos = (double) now_bus_pos.get("y_pos");
        double x_pos = (double) now_bus_pos.get("x_pos");


        double now_ypos = 0;
        double now_xpos = 0;

        double next_ypos = 0;
        double next_xpos = 0;

        double step2_ypos = 0;
        double step2_xpos = 0;

        String now_station_name = "";
        String next_station_name = "";
        String step2_station_name = "";


        Map RouteInfoMap = busRouteMapper.getBusRoute(dto);

        now_ypos = (double) RouteInfoMap.getOrDefault("now_ypos", 0);
        now_xpos = (double) RouteInfoMap.getOrDefault("now_xpos", 0);
        now_station_name = (String) RouteInfoMap.getOrDefault("now_station_name", "");


        next_ypos = (double) RouteInfoMap.getOrDefault("next_ypos", 0);
        next_xpos = (double) RouteInfoMap.getOrDefault("next_xpos", 0);
        next_station_name = (String) RouteInfoMap.getOrDefault("next_station_name", "");


        step2_ypos = (double) RouteInfoMap.getOrDefault("step2_ypos", 0);
        step2_xpos = (double) RouteInfoMap.getOrDefault("step2_xpos", 0);
        step2_station_name = (String) RouteInfoMap.getOrDefault("step2_station_name", "");
        step2_station_no = (int) RouteInfoMap.getOrDefault("step2_station_id", 0);


        double xIntersect = 0, yIntersect = 0;


        if (now_xpos == next_xpos) { // 수직선인 경우
            xIntersect = now_xpos; // x 좌표는 수직선의 x 값과 동일
            yIntersect = y_pos; // 수평선이므로 y 좌표는 세 번째 점의 y 값
        } else if (now_ypos == next_ypos) { // 수평선인 경우
            yIntersect = now_ypos; // y 좌표는 수평선의 y 값과 동일
            xIntersect = x_pos; // 수직선이므로 x 좌표는 세 번째 점의 x 값
        } else {
            double m1 = (next_ypos - now_ypos) / (next_xpos - now_xpos);
            double b1 = now_ypos - m1 * now_xpos;

            // 세 번째 점을 지나는 수직선의 방정식: y = -1/m1 * x + b2
            double m2 = -1 / m1;
            double b2 = y_pos - m2 * x_pos;

            // 두 직선의 교점을 찾습니다.
            xIntersect = (b2 - b1) / (m1 - m2);
            yIntersect = m1 * xIntersect + b1;
        }



        double distBothStation = Math.sqrt(Math.pow(next_ypos - now_ypos, 2) + Math.pow(next_xpos - now_xpos, 2));
        double distWithBusAndStation = Math.sqrt(Math.pow(yIntersect - now_ypos, 2) + Math.pow(xIntersect - now_xpos, 2));
        int portion = (int) ((distWithBusAndStation / distBothStation) * 100);
        double dist =  Math.sqrt(Math.pow(next_ypos - y_pos , 2) + Math.pow(next_xpos - x_pos, 2));




        portion = Math.min(portion, 100);
        portion = Math.max(portion, 0);



        dto.put("status", portion);
        dto.put("now_station_no", now_station_no);
        dto.put("next_station_no", next_station_no);
        dto.put("step2_station_no", step2_station_no);



        if (dist < Cdist) {
            busRouteMapper.updateBusNowStation(dto);
        } else {
            busRouteMapper.updateBusState(dto);
        }

        return null;
    }

    @Override
    public Map getBusState(Map dto) {


        return null;
    }

    @Override
    public Map getNearestBusStation(Map dto) {


        double user_y = (double) dto.get("user_ypos");
        double user_x = (double) dto.get("user_xpos");

        List<Map> allStationList = busMapper.getAllRoute(dto);


        double min_dist = 100000000;
        int candidate_bus_stop_id = 0;

        for (int i = 0; i < allStationList.size(); i++) {

            double temp_y = (double) allStationList.get(i).get("latitude");
            double temp_x = (double) allStationList.get(i).get("longitude");
            double user_station_dist = Math.sqrt(Math.pow(temp_y - user_y, 2) + Math.pow(temp_x - user_x, 2));
            if (min_dist > user_station_dist) {
                min_dist = user_station_dist;
                candidate_bus_stop_id = i;
            }
        }

        Map retBusMap = allStationList.get(candidate_bus_stop_id);

        return retBusMap;
    }

    @Override
    public List<Map> findBusRoute(Map dto) { //start_id dest_id


        int start_id = (int) dto.get("start_id");
        int dest_id = (int) dto.get("dest_id");


        List<Map> busList = busMapper.getAllBusList();
        List<Map> routeList = new ArrayList<>();


        for (int i = 0; i < busList.size(); i++) {

            Map temp = new HashMap<>();
            temp.put("route_num", (int) busList.get(i).get("id"));

            List<Map> tempRoute = busRouteMapper.getRouteByBusNum(temp);

            boolean isIncludeStart = false;
            boolean isIncludeDest = false;


            for (int j = 0; j < tempRoute.size(); j++) {

                int route_st_id = (int) tempRoute.get(j).get("bus_station_id");

                if (!isIncludeStart)
                    isIncludeStart = route_st_id == start_id;

                if (!isIncludeDest)
                    isIncludeDest = route_st_id == dest_id;
            }

            if (isIncludeDest && isIncludeStart) {
                Map targetBusInfo = busMapper.getBusInfo(temp);
                int now_station_no = (int) targetBusInfo.get("now_station_no");
                int bus_num = (int) targetBusInfo.get("bus_no");
                String bus_color = (String) targetBusInfo.get("bus_color");

                Map busInfo = new HashMap<>();

                busInfo.put("bus_no", bus_num);
                busInfo.put("bus_color", bus_color);
                busInfo.put("depature_station_name", busMapper.getStatoinNameByid(start_id));
                busInfo.put("destination_station_name", busMapper.getStatoinNameByid(dest_id));
                busInfo.put("depature_station_id", start_id);
                busInfo.put("destination_station_id", dest_id);

                busInfo.put("bus_id", busList.get(i).get("id"));


                temp.put("start_id", start_id);
                temp.put("dest_id", dest_id);


                Map leftInfo = getDistInfo(temp);


                busInfo.put("left_time", leftInfo.get("left_time"));
                busInfo.put("left_station", leftInfo.get("left_station"));

                if (now_station_no < start_id) {
                    routeList.add(busInfo);
                }
            }

        }

        return routeList;
    }


    @Override
    public Map getDistInfo(Map map) {
//        int bus_num = (int) map.getOrDefault("route_num",0);
//        int start_id = (int) map.getOrDefault("start_id",0);
//        int dest_id = (int) map.getOrDefault("dest_id",0);

        Map ret = new HashMap<>();
        int dist = busRouteMapper.getStationDist(map);
        ret.put("left_station", dist);
        ret.put("left_time", dist * 3);

        return ret;
    }

    @Override
    public Map getWaitingInfo(Map map) {

        return userMapper.getServiceInfo(map);
    }

    @Override
    public List<Map> getOffInfo(Map map) {

        List<Map> ret = userMapper.getRouteInfoAfterGetOnBus(map);

        return ret;
    }

    @Override
    public String getWaitingStatusInfo(Map map) {
        return userMapper.getWaitingStatus(map);
    }

    @Override
    public int RegisterService(Map map) {
        userMapper.registerBilndService(map);
        BigInteger t = (BigInteger) map.getOrDefault("service_id", 1);
        return t.intValue();
    }

    @Override
    public int ServieStatustoOnboard(Map map) {
        map.put("status","onBoard");
        int ret = userMapper.updateServieStatus(map);
        return ret;
    }

    @Override
    public int ServieStatustoWaiting(Map map) {
        map.put("status","waiting");
        int ret = userMapper.updateServieStatus(map);
        return ret;
    }

    @Override
    public double getDistWithUserAndStation(Map map) {
        return userMapper.getDistWithUserAndStation(map);
    }

    @Override
    public Map getBusDeviceInfo(Map map) { //map : 1 or 2


        Map retMap = new HashMap<>();
        List<Map> routeList = busRouteMapper.getBusRouteByBusId(map);
        Map nowBusPos = busRouteMapper.getNowBusStationByBusId(map);
        Map DistWithUserAndBus = userMapper.getDistWithUserAndBus(map);
        Map userOnboardInfo = busMapper.getBlindPassengerInfo(map);

        nowBusPos.put("user_bus_dist",DistWithUserAndBus.get("dist"));
        nowBusPos.put("is_user_infront",DistWithUserAndBus.get("is_user_infront"));
        retMap.put("route_list",routeList);
        retMap.put("bus_pos_info",nowBusPos);
        retMap.put("user_onboard_info",userOnboardInfo);


        return retMap;
    }


    @Override
    public Map findDestStation(Map dto) {
        dto = busMapper.findStationByName(dto);
        Map ret = busMapper.findStation(dto);

        return ret;
    }


}
