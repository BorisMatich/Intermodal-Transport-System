package com.transport.dto;

public class DepartureDTO {
    private String type;
    private String from;
    private String to;
    private String departureTime;
    private String duration;
    private int price;
    private int minTransferTime;

    public DepartureDTO(){}
    public DepartureDTO(String type, String from,
                        String to, String departureTime,
                        String duration, int price,
                        int minTransferTime) {
        this.type = type;
        this.from = from;
        this.to = to;
        this.departureTime = departureTime;
        this.duration = duration;
        this.price = price;
        this.minTransferTime = minTransferTime;
    }

    public String getType() {
        return type;
    }
    public String getFrom() {
        return from;
    }
    public String getTo() {
        return to;
    }
    public String getDepartureTime() {
        return departureTime;
    }
    public String getDuration() {
        return duration;
    }
    public int getPrice() {
        return price;
    }
    public int getMinTransferTime() {
        return minTransferTime;
    }

    public void setType(String type){
        this.type = type;
    }
    public void setFrom(String from){
        this.from = from;
    }
    public void setTo(String to){
        this.to = to;
    }
    public void setDepartureTime(String departureTime){
        this.departureTime = departureTime;
    }
    public void setDuration(String duration){
        this.duration = duration;
    }
    public void setPrice(int price){
        this.price = price;
    }
    public void setMinTransferTime(int minTransferTime){
        this.minTransferTime = minTransferTime;
    }
}