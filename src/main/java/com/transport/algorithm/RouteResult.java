package com.transport.algorithm;

import com.transport.model.Departure;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public class RouteResult {
    private final List<Departure> departures;
    private final double totalPrice;
    private final Duration totalDuration;
    private final LocalTime requestedDepartureTime;

    public RouteResult(List<Departure> departures, LocalTime requestedDepartureTime) {
        this.departures = departures;
        this.requestedDepartureTime = requestedDepartureTime;

        this.totalPrice = departures.stream()
                .mapToDouble(Departure::getPrice)
                .sum();

        if (!departures.isEmpty()) {
            // Računaj ukupno trajanje prolazom kroz sve departure-e
            long totalMinutes = 0;
            LocalTime currentTime = requestedDepartureTime;

            for (Departure dep : departures) {
                // Vrijeme čekanja na ovaj departure
                long waitingMinutes = Duration.between(currentTime, dep.getDepartureTime()).toMinutes();
                if (waitingMinutes < 0) {
                    waitingMinutes += 24 * 60; // Prešli smo ponoć
                }
                totalMinutes += waitingMinutes;

                // Trajanje vožnje
                totalMinutes += dep.getDuration().toMinutes();

                // Ažuriraj trenutno vrijeme na arrival time
                currentTime = dep.getArrivalTime();
            }

            this.totalDuration = Duration.ofMinutes(totalMinutes);
        } else {
            this.totalDuration = Duration.ZERO;
        }
    }

    public List<Departure> getDepartures() {
        return departures;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Duration getTotalDuration() {
        return totalDuration;
    }

    public LocalTime getRequestedDepartureTime() {
        return requestedDepartureTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RUTA ===\n");
        for (Departure d : departures) {
            sb.append(d.getFrom().getName())
                    .append(" → ")
                    .append(d.getTo().getName())
                    .append(" [")
                    .append(d.getType())
                    .append("] ")
                    .append(d.getDepartureTime())
                    .append(" - ")
                    .append(d.getArrivalTime())
                    .append("\n");
        }
        sb.append("Ukupna cijena: ").append(totalPrice).append(" KM\n");
        sb.append("Ukupno trajanje: ").append(formatDuration(totalDuration)).append("\n");
        return sb.toString();
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        return hours > 0 ? hours + "h " + minutes + "min" : minutes + "min";
    }
}