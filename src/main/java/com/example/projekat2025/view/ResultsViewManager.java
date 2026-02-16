package com.example.projekat2025.view;

import com.example.projekat2025.controller.RoutesPopupController;
import com.example.projekat2025.view.model.RouteSegmentTableModel;
import com.transport.algorithm.RouteResult;

import com.transport.service.TicketService;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Menadžer za prikaz rezultata pretrage u obliku tabele.
 * Prikazuje najbolju rutu detaljno, sa opcijama za kupovinu karte
 * i pregled alternativnih ruta.
 */
public class ResultsViewManager {

    private final VBox container;
    private TableView<RouteSegmentTableModel> routeTable;
    private Label summaryLabel;
    private Button buyTicketButton;
    private Button showAlternativesButton;
    private final TicketService ticketService;

    // Čuvamo trenutno prikazanu rutu za kupovinu karte
    private RouteResult currentDisplayedRoute;
    // Čuvamo sve pronađene rute za prikaz alternativa
    private List<RouteResult> allRoutes;

    public ResultsViewManager(VBox container, TicketService ticketService) {
        this.container = container;
        this.container.setSpacing(15);
        this.container.setPadding(new Insets(15));
        this.ticketService = ticketService;
        initializeComponents();
    }

    /**
     * Inicijalizuje sve UI komponente koje će biti deo prikaza rezultata.
     */
    private void initializeComponents() {
        // Naslov sekcije
        Label titleLabel = new Label("Optimalna ruta");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Tabela za segmente rute
        routeTable = createRouteTable();

        // Label za sumarni prikaz (ukupna cena, trajanje)
        summaryLabel = new Label();
        summaryLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " +
                "-fx-padding: 10; -fx-background-color: #f0f0f0; " +
                "-fx-background-radius: 5;");

        // Dugmad za akcije
        HBox buttonBox = createButtonBox();

        // Dodajemo sve u kontejner (na početku je sve sakriveno)
        container.getChildren().addAll(titleLabel, routeTable, summaryLabel, buttonBox);

        // Inicijalno sakrivamo sve dok ne dobijemo rezultate
        setComponentsVisible(false);
    }

    /**
     * Kreira i konfigriše TableView sa svim potrebnim kolonama.
     */
    private TableView<RouteSegmentTableModel> createRouteTable() {
        TableView<RouteSegmentTableModel> table = new TableView<>();

        // Kolona za tip prevoza (Voz/Bus)
        TableColumn<RouteSegmentTableModel, String> typeCol = new TableColumn<>("Prevoz");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("transportType"));
        typeCol.setPrefWidth(80);

        // Kolona za polazište
        TableColumn<RouteSegmentTableModel, String> fromCol = new TableColumn<>("Polazište");
        fromCol.setCellValueFactory(new PropertyValueFactory<>("fromCity"));
        fromCol.setPrefWidth(120);

        // Kolona za destinaciju
        TableColumn<RouteSegmentTableModel, String> toCol = new TableColumn<>("Destinacija");
        toCol.setCellValueFactory(new PropertyValueFactory<>("toCity"));
        toCol.setPrefWidth(120);

        // Kolona za vreme polaska
        TableColumn<RouteSegmentTableModel, String> depTimeCol = new TableColumn<>("Polazak");
        depTimeCol.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        depTimeCol.setPrefWidth(80);

        // Kolona za vreme dolaska
        TableColumn<RouteSegmentTableModel, String> arrTimeCol = new TableColumn<>("Dolazak");
        arrTimeCol.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        arrTimeCol.setPrefWidth(80);

        // Kolona za trajanje segmenta
        TableColumn<RouteSegmentTableModel, String> durationCol = new TableColumn<>("Trajanje");
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationCol.setPrefWidth(90);

        // Kolona za cenu
        TableColumn<RouteSegmentTableModel, Double> priceCol = new TableColumn<>("Cijena (KM)");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(100);

        // Formatiramo prikaz cene na 2 decimale
        priceCol.setCellFactory(col -> new TableCell<RouteSegmentTableModel, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", price));
                }
            }
        });

        table.getColumns().addAll(typeCol, fromCol, toCol, depTimeCol,
                arrTimeCol, durationCol, priceCol);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setFixedCellSize(35); // Visina svakog reda

        VBox.setVgrow(table, Priority.ALWAYS);

        return table;
    }

    /**
     * Kreira HBox sa dugmadima za akcije (kupovina i alternative).
     */
    private HBox createButtonBox() {
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        buyTicketButton = new Button("Kupovina karte");
        buyTicketButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10 20;");
        buyTicketButton.setOnAction(e -> handleBuyTicket());

        showAlternativesButton = new Button("Prikaz dodatnih ruta");
        showAlternativesButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10 20;");
        showAlternativesButton.setOnAction(e -> handleShowAlternatives());

        buttonBox.getChildren().addAll(showAlternativesButton, buyTicketButton);

        return buttonBox;
    }

    /**
     * Prikazuje listu ruta u tabeli. Prikazuje se samo najbolja ruta,
     * ali čuvamo sve rute za alternativni prikaz.
     */
    public void display(List<RouteResult> routes) {
        this.allRoutes = routes;

        if (allRoutes == null || allRoutes.isEmpty()) {
            showNoResultsMessage();
            return;
        }

        // Prikazujemo najbolju rutu (prvu u listi)
        displayRoute(allRoutes.get(0));

        // Omogućavamo dugme za alternative samo ako ima više od jedne rute
        showAlternativesButton.setDisable(allRoutes.size() <= 1);

        setComponentsVisible(true);
    }

    /**
     * Prikazuje jednu rutu u tabeli sa svim njenim segmentima.
     */
    private void displayRoute(RouteResult route) {
        this.currentDisplayedRoute = route;

        // Konvertujemo Departure objekte u TableModel objekte
        ObservableList<RouteSegmentTableModel> segments = route.getDepartures()
                .stream()
                .map(RouteSegmentTableModel::new)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        routeTable.setItems(segments);
        routeTable.setPrefHeight(TableView.USE_COMPUTED_SIZE);
        routeTable.setMinHeight(150);
        // Ažuriramo sumarni prikaz
        updateSummary(route);
    }

    /**
     * Ažurira label sa sumarnim informacijama o ruti.
     */
    private void updateSummary(RouteResult route) {
        String summaryText = String.format(
                "Ukupno: %.2f KM  |  Trajanje: %s  |  Presjedanja: %d",
                route.getTotalPrice(),
                formatDuration(route.getTotalDuration()),
                route.getDepartures().size() - 1  // Broj presedanja = broj segmenata - 1
        );
        summaryLabel.setText(summaryText);
    }

    /**
     * Formatira trajanje u čitljiv oblik sa danima, satima i minutama.
     */
    private String formatDuration(Duration duration) {
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutesPart();

        StringBuilder result = new StringBuilder();

        if (days > 0) {
            result.append(days).append("d ");
        }
        if (hours > 0) {
            result.append(hours).append("h ");
        }
        if (minutes > 0 || result.length() == 0) {
            result.append(minutes).append("min");
        }

        return result.toString().trim();
    }

    /**
     * Handler za kupovinu karte - poziva TicketService.
     */
    private void handleBuyTicket() {
        if (currentDisplayedRoute == null) {
            return;
        }

        // TODO: Implementirati poziv ka TicketService-u
        // TicketService će generisati JSON fajl i sačuvati ga u /racuni folder

        Path path = ticketService.purchaseTicket(currentDisplayedRoute);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Kupovina karte");
        alert.setHeaderText("Karta uspješno kupljena!");
        alert.setContentText("Račun sačuvan: " + path.getFileName());
        alert.showAndWait();
    }

    /**
     * Handler za prikaz alternativnih ruta - otvara novi prozor.
     */
    private void handleShowAlternatives() {
        if (allRoutes == null || allRoutes.size() <= 1) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projekat2025/routes-popup.fxml"));
            VBox popup = loader.load();

            RoutesPopupController controller = loader.getController();
            controller.setRoutes(allRoutes);
            controller.setTicketService(ticketService);

            Stage popupStage = new Stage();
            popupStage.setTitle("Top " + allRoutes.size() + " ruta");
            popupStage.setScene(new Scene(popup, 900, 650));
            popupStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Prikazuje poruku kada nema pronađenih ruta.
     */
    private void showNoResultsMessage() {
        setComponentsVisible(false);

        Label noResultsLabel = new Label("Nema pronađenih ruta.");
        noResultsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");

//        container.getChildren().clear();
//        container.getChildren().add(noResultsLabel);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rezultati pretrage");
        alert.setHeaderText(null);
        alert.setContentText("Nema pronađenih ruta za zadate kriterijume.");
        alert.showAndWait();
    }

    /**
     * Pokazuje ili sakriva sve komponente prikaza rezultata.
     */
    private void setComponentsVisible(boolean visible) {
        container.getChildren().forEach(node -> node.setVisible(visible));
    }

    /**
     * Briše sve prikazane rezultate i resetuje stanje.
     */
    public void clear() {
        routeTable.getItems().clear();
        summaryLabel.setText("");
        currentDisplayedRoute = null;
        allRoutes = null;
        setComponentsVisible(false);
    }
}