package com.transport.transportData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transport.model.Departure;
import com.transport.model.Station;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class TransportData {
    private String[][] countryMap;
    private List<Station> stations;
    private List<Departure> departures;

    public TransportData() {
    }

    public static TransportData readData(){
        ObjectMapper mapper = new ObjectMapper();
        try(FileInputStream transportData = new FileInputStream("src/main/resources/data/transport_data.json")){
            System.out.println("File transport_data.json successfully opened");
            return mapper.readValue(transportData, TransportData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCountryMap(String[][] countryMap) {
        this.countryMap = countryMap;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }


    public void setDepartures(List<Departure> departures){
        this.departures = departures;
    }

    @Override
    public String toString() {
        return "TransportData{" +
                "countryMap=" + Arrays.deepToString(countryMap) +
                ", stations=" + stations +
                ", departures=" + departures +
                '}';
    }
}