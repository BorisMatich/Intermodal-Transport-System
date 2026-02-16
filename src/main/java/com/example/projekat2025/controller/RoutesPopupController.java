package com.example.projekat2025.controller;

import com.transport.algorithm.RouteResult;
import com.transport.model.Departure;
import com.transport.service.TicketService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

public class RoutesPopupController implements Initializable {

    @FXML private VBox routesContainer;
    @FXML private Button btnClose;

    private List<RouteResult> routes;
    private TicketService ticketService;

    public void setRoutes(List<RouteResult> routes) {
        this.routes = routes;
        displayRoutes();
    }

    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Čekamo da se postave routes kroz setRoutes()
    }

    private void displayRoutes() {
        routesContainer.getChildren().clear();

        for (int i = 0; i < routes.size(); i++) {
            RouteResult route = routes.get(i);
            VBox routeBox = createRouteBox(i + 1, route);
            routesContainer.getChildren().add(routeBox);
        }
    }

    private VBox createRouteBox(int index, RouteResult route) {
        VBox box = new VBox(10);
        box.setStyle("-fx-background-color: #e8e8e8; -fx-padding: 15; -fx-background-radius: 5;");

        // Naslov
        Label title = new Label("Ruta #" + index);
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        // Tabela za departure-e
        TableView<Departure> table = createDepartureTable(route.getDepartures());

        // Summary
        Label summary = new Label(String.format("Ukupna cijena: %.2f KM | Trajanje: %s",
                route.getTotalPrice(),
                formatDuration(route.getTotalDuration())));
        summary.setStyle("-fx-font-weight: bold;");

        // Dugme za kupovinu
        Button buyBtn = new Button("Kupi kartu");
        buyBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        buyBtn.setOnAction(e -> handleBuyTicket(route));

        box.getChildren().addAll(title, table, summary, buyBtn);
        return box;
    }

    private TableView<Departure> createDepartureTable(List<Departure> departures) {
        TableView<Departure> table = new TableView<>();
        table.setPrefHeight(150);

        TableColumn<Departure, String> typeCol = new TableColumn<>("Prevoz");
        typeCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getType().toString()));

        TableColumn<Departure, String> fromCol = new TableColumn<>("Polazište");
        fromCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFrom().getName()));

        TableColumn<Departure, String> toCol = new TableColumn<>("Destinacija");
        toCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTo().getName()));

        TableColumn<Departure, String> depTimeCol = new TableColumn<>("Polazak");
        depTimeCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDepartureTime().toString()));

        TableColumn<Departure, String> arrTimeCol = new TableColumn<>("Dolazak");
        arrTimeCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getArrivalTime().toString()));

        TableColumn<Departure, String> priceCol = new TableColumn<>("Cijena (KM)");
        priceCol.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("%.2f", data.getValue().getPrice())));

        table.getColumns().addAll(typeCol, fromCol, toCol, depTimeCol, arrTimeCol, priceCol);
        table.setItems(FXCollections.observableArrayList(departures));

        return table;
    }

    private void handleBuyTicket(RouteResult route) {
        if (ticketService == null) {
            showAlert("Greška", "TicketService nije dostupan.");
            return;
        }

        Path path = ticketService.purchaseTicket(route);
        showAlert("Uspješno", "Račun sačuvan: " + path.getFileName());
    }

    @FXML
    private void onClose() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    private String formatDuration(java.time.Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        return hours > 0 ? hours + "h " + minutes + "min" : minutes + "min";
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}