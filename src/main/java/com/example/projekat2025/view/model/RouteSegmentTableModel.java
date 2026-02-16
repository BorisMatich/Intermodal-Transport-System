package com.example.projekat2025.view.model;

import com.transport.model.Departure;
import javafx.beans.property.*;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

/**
 * Model klasa koja predstavlja jedan red u tabeli - jedan segment putovanja.
 * Koristi JavaFX Property objekte kako bi tabela mogla automatski
 * da prati promene (reactive pattern).
 */
public class RouteSegmentTableModel {

    private final StringProperty transportType;
    private final StringProperty fromCity;
    private final StringProperty toCity;
    private final StringProperty departureTime;
    private final StringProperty arrivalTime;
    private final StringProperty duration;
    private final DoubleProperty price;

    // Čuvamo original Departure objekat za potrebe kupovine karte
    private final Departure originalDeparture;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public RouteSegmentTableModel(Departure departure) {
        this.originalDeparture = departure;

        // Inicijalizujemo Property objekte sa podacima iz Departure-a
        this.transportType = new SimpleStringProperty(departure.getType().toString());
        this.fromCity = new SimpleStringProperty(departure.getFrom().getName());
        this.toCity = new SimpleStringProperty(departure.getTo().getName());
        this.departureTime = new SimpleStringProperty(
                departure.getDepartureTime().format(TIME_FORMATTER)
        );
        this.arrivalTime = new SimpleStringProperty(
                departure.getArrivalTime().format(TIME_FORMATTER)
        );
        this.price = new SimpleDoubleProperty(departure.getPrice());

        // Izračunavamo trajanje segmenta
        this.duration = new SimpleStringProperty(formatDuration(departure.getDuration()));
    }

    /**
     * Formatira trajanje u čitljiv oblik (npr. "2h 30min").
     */
    private String formatDuration(Duration d) {
        long hours = d.toHours();
        long minutes = d.toMinutesPart();

        if (hours > 0 && minutes > 0) {
            return hours + "h " + minutes + "min";
        } else if (hours > 0) {
            return hours + "h";
        } else {
            return minutes + "min";
        }
    }

    // Getteri za Property objekte (TableView koristi ove metode)
    public StringProperty transportTypeProperty() { return transportType; }
    public StringProperty fromCityProperty() { return fromCity; }
    public StringProperty toCityProperty() { return toCity; }
    public StringProperty departureTimeProperty() { return departureTime; }
    public StringProperty arrivalTimeProperty() { return arrivalTime; }
    public StringProperty durationProperty() { return duration; }
    public DoubleProperty priceProperty() { return price; }

    // Standardni getteri za vrednosti
    public String getTransportType() { return transportType.get(); }
    public String getFromCity() { return fromCity.get(); }
    public String getToCity() { return toCity.get(); }
    public String getDepartureTime() { return departureTime.get(); }
    public String getArrivalTime() { return arrivalTime.get(); }
    public String getDuration() { return duration.get(); }
    public double getPrice() { return price.get(); }

    public Departure getOriginalDeparture() { return originalDeparture; }
}