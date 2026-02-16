package com.example.projekat2025.controller;

import com.transport.exporter.TxtTicketReader;
import com.transport.service.StatisticsService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML private Label lblTotalTickets;
    @FXML private Label lblTotalRevenue;
    @FXML private Button btnStart;

    private StatisticsService statisticsService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TxtTicketReader reader = new TxtTicketReader(Path.of("output/racuni"));
        statisticsService = new StatisticsService(reader);
        loadStatistics();
    }

    private void loadStatistics() {
        int totalTickets = statisticsService.getNumberOfTickets();
        double totalRevenue = statisticsService.getTotalPrice();

        lblTotalTickets.setText(String.valueOf(totalTickets));
        lblTotalRevenue.setText(String.format("%.2f KM", totalRevenue));
    }

    @FXML
    private void onStartApp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projekat2025/main-view.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 800);

            Stage stage = (Stage) btnStart.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Transport System");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}