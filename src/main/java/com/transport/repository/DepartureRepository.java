package com.transport.repository;

import com.transport.adapter.DepartureAdapter;
import com.transport.dto.DepartureDTO;
import com.transport.model.Departure;
import com.transport.model.Station;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DepartureRepository {
    private final List<Departure> departures;  // svi polasci
    private final Map<Station, List<Departure>> departuresByOrigin;  // ključno za graf

    public DepartureRepository(List<DepartureDTO> dtos, Map<String, Station> stations){
        DepartureAdapter adapter = new DepartureAdapter();

        departures = dtos.stream()
                .map(dto -> adapter.adapt(dto, stations))
                .toList();

        departuresByOrigin = departures.stream()
                .collect(Collectors.groupingBy(Departure::getFrom));
    }

    public List<Departure> getDeparturesFrom(Station station){
        return departuresByOrigin.getOrDefault(station, Collections.emptyList());
    }

//    public void printAllDepartures(){
//        departures.forEach(departure -> System.out.println(departure));
//    }
    public Map<Station, List<Departure>> getDeparturesMap(){
        return departuresByOrigin;
    }

    public List<Departure> getAll(){
        return departures;
    }
}
