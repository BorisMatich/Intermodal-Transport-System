package com.example.projekat2025;

import com.example.projekat2025.controller.HomeController;
import com.transport.exporter.TicketReader;
import com.transport.exporter.TxtTicketReader;
import com.transport.service.StatisticsService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.Path;
//TODO: Poraditi na izgledu aplikacije(poravnanje teksta, boje, font)
//TODO: Napraviti konfiguracioni fajl za podesavanja aplikacije
//TODO: Promijeniti matricu da je velicine n x m : n != m, TRANSPORT_DATA_GEN
public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Scene scene = new Scene(loader.load(), 600, 400);
        stage.setTitle("Transport System - Početna");
        stage.setScene(scene);
        stage.show();
    }
}