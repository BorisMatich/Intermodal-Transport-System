package com.example.projekat2025.controller;

import com.example.projekat2025.view.GraphViewManager;
import com.example.projekat2025.view.ResultsViewManager;
import com.transport.algorithm.RouteResult;
import com.transport.algorithm.SearchCriteria;
import com.transport.exporter.TicketExporter;
import com.transport.exporter.TxtTicketExporter;
import com.transport.service.RouteService;
import com.transport.service.TicketService;
import com.transport.service.TransportDataLoader;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private Label lblStart;
    @FXML private Label lblEnd;
    @FXML private ComboBox<SearchCriteria> criteriaBox;
    @FXML private TextField timeField;
    @FXML private Button searchBtn;
    @FXML private Button resetBtn;
    @FXML private Pane graphPane;
    @FXML private VBox resultsBox;

    private RouteService routeService;
    private GraphViewManager graphManager;
    private ResultsViewManager resultsManager;
    private TicketService ticketService;

    private String selectedStartCity;
    private String selectedEndCity;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupRouteService();
        setupUI();
        setupGraph();
        setupResultsView();
    }

    private void setupRouteService() {
        TransportDataLoader loader = new TransportDataLoader("src/main/resources/data/transport_data.json");
        routeService = loader.getRouteService();
    }

    private void setupUI() {
        criteriaBox.getItems().addAll(SearchCriteria.values());
        criteriaBox.setValue(SearchCriteria.PRICE);
        timeField.setText("08:00");
        lblStart.setText("-");
        lblEnd.setText("-");
    }

    private void setupGraph() {
        graphManager = new GraphViewManager(graphPane, this::onCitiesSelected);
        graphManager.buildGraph(
                routeService.getAllStations(),
                routeService.getAllDepartures()
        );
    }

    private void setupResultsView() {
        TicketExporter exporter = new TxtTicketExporter(Path.of("output/racuni"));
        ticketService = new TicketService(exporter);
        resultsManager = new ResultsViewManager(resultsBox, ticketService);
    }

    private void onCitiesSelected(String startCity, String endCity) {
        this.selectedStartCity = startCity;
        this.selectedEndCity = endCity;
        lblStart.setText(startCity);
        lblEnd.setText(endCity);
    }

    @FXML
    private void onSearch() {
        if (selectedStartCity == null || selectedEndCity == null) {
            showAlert("Odaberi polazište i odredište na mapi!");
            return;
        }

        LocalTime time;
        try {
            time = LocalTime.parse(timeField.getText());
        } catch (DateTimeParseException e) {
            showAlert("Neispravan format vremena! Koristi HH:mm");
            return;
        }

        SearchCriteria criteria = criteriaBox.getValue();

        List<RouteResult> routes = routeService.findRoutesByCity(
                selectedStartCity,
                selectedEndCity,
                time,
                criteria,
                5
        );
        resultsManager.display(routes);

        // Highlight prvu rutu na mapi
        if (!routes.isEmpty()) {
            graphManager.highlightRoute(routes.get(0).getDepartures());
        }else{
            graphManager.resetSelection();
        }
    }

    @FXML
    private void onReset() {
        graphManager.resetSelection();
        resultsManager.clear();
        selectedStartCity = null;
        selectedEndCity = null;
        lblStart.setText("-");
        lblEnd.setText("-");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Upozorenje");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}