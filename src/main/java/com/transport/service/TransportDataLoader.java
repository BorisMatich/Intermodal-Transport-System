package com.transport.service;

import com.transport.parser.TransportParser;
import com.transport.repository.DepartureRepository;
import com.transport.repository.StationRepository;

public class TransportDataLoader {
    private final StationRepository stationRepo;
    private final DepartureRepository departureRepo;

    public TransportDataLoader(String filePath){
        TransportParser parser = new TransportParser(filePath);

        this.stationRepo = new StationRepository(parser.parseStations());
        this.departureRepo = new DepartureRepository(parser.parseDeparture(), stationRepo.getStations());
    }

    public StationRepository getStationRepo(){
        return stationRepo;
    }

    public DepartureRepository getDepartureRepo() {
        return departureRepo;
    }

    public RouteService getRouteService(){
        return new RouteService(stationRepo, departureRepo);
    }
}
