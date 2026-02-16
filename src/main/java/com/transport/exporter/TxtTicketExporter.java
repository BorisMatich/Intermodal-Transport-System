package com.transport.exporter;

import com.transport.algorithm.RouteResult;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.UUID.randomUUID;

public class TxtTicketExporter implements TicketExporter {
    private final Path folderPath;

    public TxtTicketExporter(Path folderPath) {
        this.folderPath = folderPath;
    }

    @Override
    public Path export(RouteResult routeResult) {
        if(Files.exists(folderPath)) {
            Path filePath = folderPath.resolve(ticketID() + ".txt");
            try (BufferedWriter bf = Files.newBufferedWriter(filePath, CREATE)) {
                bf.write(routeResult.toString());
                return filePath.getFileName();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("Neispravna putanja do foldera za račune!");
        }
    }

    @Override
    public String ticketID() {
        return randomUUID().toString().replaceAll("-", "");
    }
}
