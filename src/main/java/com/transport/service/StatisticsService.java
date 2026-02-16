package com.transport.service;

import com.transport.exporter.TicketReader;
import com.transport.exporter.TxtTicketReader;

public class StatisticsService {
    private final TicketReader reader;

    public StatisticsService(TicketReader reader) {
        this.reader = reader;
    }

    public Double getTotalPrice() {
        double sum = reader.readAllPrices()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();
        return Double.valueOf(sum);
    }

    public Integer getNumberOfTickets(){
        return Integer.valueOf(reader.countTickets());
    }
}
