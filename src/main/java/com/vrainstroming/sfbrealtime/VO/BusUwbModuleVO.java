package com.vrainstroming.sfbrealtime.VO;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusUwbModuleVO extends UwbModuleVO{

    int bus_id;
    int bus_no;
    String vehicle_no;
    int y_pos;
    int x_pos;
    String bus_color;

}
