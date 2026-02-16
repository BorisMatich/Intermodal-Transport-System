package com.transport.model;

public class Station {
    private String name;
    private String city;
    private StationType type;

    public Station() {
    }

    public Station(String name, String city, StationType type) {
        this.city = city;
        this.name = name;
        this.type = type;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStationType(StationType type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public StationType getType() {
        return type;
    }

    public String toString() {
        return String.format("%s (%s) - %s", name, city, type);
    }

}