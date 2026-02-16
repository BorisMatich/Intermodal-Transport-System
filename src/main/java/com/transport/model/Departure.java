package com.transport.model;

import java.time.Duration;
import java.time.LocalTime;

public class Departure {

    private StationType type;
    private Station from;
    private Station to;
    private LocalTime departureTime;
    private Duration duration;
    private Double price;
    private Duration minTransferTime;

    public Departure(){}

    public Departure(StationType type, Station from,
                     Station to, LocalTime departureTime,
                     Duration duration, Double price,
                     Duration minTransferTime) {
        this.type = type;
        this.from = from;
        this.to = to;
        this.departureTime = departureTime;
        this.duration = duration;
        this.price = price;
        this.minTransferTime = minTransferTime;
    }

    public void setType(StationType type) {
        this.type = type;
    }

    public void setFrom(Station from) {
        this.from = from;
    }

    public void setTo(Station to) {
        this.to = to;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setMinTransferTime(Duration minTransferTime) {
        this.minTransferTime = minTransferTime;
    }

    public StationType getType() {
        return type;
    }

    public Station getFrom() {
        return from;
    }

    public Station getTo() {
        return to;
    }

    public Duration getMinTransferTime() {
        return minTransferTime;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public LocalTime getArrivalTime() {
        return departureTime.plus(duration);
    }

    public Duration getDuration() {
        return duration;
    }
    public Double getPrice() {
        return price;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.departureTime = arrivalTime.minus(duration);
    }

    @Override
    public String toString() {
        return "Departure{" +
                "type=" + type +
                ", from=" + from +
                ", to=" + to +
                ", departureTime=" + departureTime +
                ", duration=" + duration +
                ", price=" + price +
                ", minTransferTime=" + minTransferTime +
                '}';
    }
}