package com.transport.adapter;

import com.transport.dto.StationDTO;
import com.transport.model.Station;
import com.transport.model.StationType;

import java.util.List;

public class StationAdapter {

    public StationAdapter(){}

    public List<Station> adapt(StationDTO stationDTO){
         return List.of(
                 new Station(stationDTO.getBusStation(), stationDTO.getCity(), StationType.BUS),
                 new Station(stationDTO.getTrainStation(), stationDTO.getCity(), StationType.TRAIN)
         );
    }
}
