package com.transport.service;

import com.transport.algorithm.RouteResult;
import com.transport.exporter.TicketExporter;

import java.nio.file.Path;

import static java.util.UUID.randomUUID;

public class TicketService {
    private final TicketExporter exporter;

    public TicketService(TicketExporter exporter){
        this.exporter = exporter;
    }

    public Path purchaseTicket(RouteResult routeResult){
        return exporter.export(routeResult);
    }
}
