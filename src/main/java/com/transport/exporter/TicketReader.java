package com.transport.exporter;

import java.util.List;

public interface TicketReader {
    List<Double> readAllPrices();

    int countTickets();
}
