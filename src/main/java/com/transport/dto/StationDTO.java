package com.transport.dto;

public class StationDTO {
    private String city;
    private String busStation;
    private String trainStation;

    public StationDTO() {
    }

    public StationDTO(String city, String busStation, String trainStation) {
        this.city = city;
        this.busStation = busStation;
        this.trainStation = trainStation;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setBusStation(String busStation) {
        this.busStation = busStation;
    }

    public void setTrainStation(String trainStation) {
        this.trainStation = trainStation;
    }

    public void setStation(String station) {
        this.city = station;
    }

    public String getBusStation() {
        return busStation;
    }

    public String getTrainStation() {
        return trainStation;
    }

    public String getCity() {
        return city;
    }
}