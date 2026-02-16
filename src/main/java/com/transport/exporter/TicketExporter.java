package com.transport.exporter;

import com.transport.algorithm.RouteResult;

import java.nio.file.Path;

public interface TicketExporter {

    Path export(RouteResult routeResult);

    String ticketID();
}
