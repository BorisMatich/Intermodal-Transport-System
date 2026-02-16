package com.transport.repository;

import com.transport.adapter.StationAdapter;
import com.transport.dto.StationDTO;
import com.transport.model.Station;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// repository/StationRepository.java
public class StationRepository {
    private final Map<String, Station> stationsByName;

    public StationRepository(List<StationDTO> dtos) {
        StationAdapter adapter = new StationAdapter();

        this.stationsByName = dtos.stream()
                .flatMap(dto -> adapter.adapt(dto).stream())
                .collect(Collectors.toMap(Station::getName, Function.identity()));
    }

    public Station getByName(String stationName) {
        return stationsByName.get(stationName);
    }

    public Map<String, Station> getStations() {
        return stationsByName;
    }

    public void printAllStations() {
        stationsByName.values().forEach(station -> System.out.println(station));
    }
}