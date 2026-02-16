package com.transport.adapter;

import com.transport.dto.DepartureDTO;
import com.transport.model.Departure;
import com.transport.model.Station;
import com.transport.model.StationType;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class DepartureAdapter {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public Departure adapt(DepartureDTO dto, Map<String, Station> stations) {
        Departure departure = new Departure();

        String destinationCity = dto.getTo();
        StationType type = parseStationType(dto.getType());
        departure.setType(type);

        String stationName = type == StationType.BUS
                ? "A" + destinationCity.substring(1)
                : "Z" + destinationCity.substring(1);

        dto.setTo(stationName);
        // Pretraga stanica
        Station fromStation = stations.get(dto.getFrom());
        Station toStation = stations.get(dto.getTo());

        // Ako neka od stanica ne postoji baca se izuzetak. Departure nije validan
        if (fromStation == null || toStation == null) {
            throw new IllegalArgumentException(
                    "Stanica nije pronadjena: " + (fromStation == null ? dto.getFrom() : dto.getTo())
            );
        }

        departure.setFrom(fromStation);
        departure.setTo(toStation);

        // Parsiranje vremena polazka
        LocalTime departureTime = LocalTime.parse(dto.getDepartureTime(), TIME_FORMATTER);
        departure.setDepartureTime(departureTime);

        // Parsiranje trajanja puta
        Duration duration = Duration.ofMinutes(Long.parseLong(dto.getDuration()));
        departure.setDuration(duration);

        // Cijena
        departure.setPrice((double) dto.getPrice());

        // Parsiranje minimalnog vremena presjedanja
        Duration minTransfer = Duration.ofMinutes(dto.getMinTransferTime());
        departure.setMinTransferTime(minTransfer);

        return departure;
    }

    private StationType parseStationType(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Stanica ne moze biti null");
        }

        return switch (type.toLowerCase()) {
            case "autobus" -> StationType.BUS;
            case "voz" -> StationType.TRAIN;
            default -> throw new IllegalArgumentException("Nepoznat tip stanice: " + type);
        };
    }
}