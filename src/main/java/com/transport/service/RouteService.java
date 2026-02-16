package com.transport.service;

import com.transport.algorithm.RouteResult;
import com.transport.algorithm.SearchCriteria;
import com.transport.algorithm.Yen;
import com.transport.model.Departure;
import com.transport.model.Station;
import com.transport.repository.DepartureRepository;
import com.transport.repository.StationRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RouteService {
    private final StationRepository stationRepo;
    private final DepartureRepository departureRepo;

    public RouteService(StationRepository stationRepo, DepartureRepository departureRepo) {
        this.stationRepo = stationRepo;
        this.departureRepo = departureRepo;
    }

    public List<RouteResult> findRoutes(String fromStation, String toStation,
                                        LocalTime time, SearchCriteria criteria, int k) {
        Station start = stationRepo.getByName(fromStation);
        Station end = stationRepo.getByName(toStation);

        if (start == null || end == null) {
            return Collections.emptyList();
        }

        Yen yen = new Yen(departureRepo.getDeparturesMap(), criteria, k, time);
        return yen.findKShortestPaths(start, end, time);
    }
    public List<Station> getAllStations(){
        return new ArrayList<>(stationRepo.getStations().values());
    }

    public List<Departure> getAllDepartures(){
        return departureRepo.getAll();
    }

    public List<RouteResult> findRoutesByCity(String fromCity, String toCity,
                                              LocalTime time, SearchCriteria criteria, int k) {
        Station start = stationRepo.getStations().values().stream()
                .filter(s -> s.getCity().equals(fromCity))
                .findFirst()
                .orElse(null);

        Station end = stationRepo.getStations().values().stream()
                .filter(s -> s.getCity().equals(toCity))
                .findFirst()
                .orElse(null);

        if (start == null || end == null) {
            return Collections.emptyList();
        }

        Yen yen = new Yen(departureRepo.getDeparturesMap(), criteria, k, time);
        return yen.findKShortestPaths(start, end, time);
    }
}
