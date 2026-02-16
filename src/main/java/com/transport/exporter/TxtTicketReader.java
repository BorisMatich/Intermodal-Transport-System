package com.transport.exporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class TxtTicketReader implements TicketReader {
    private final Path targetFolder;

    public TxtTicketReader(Path targetFolder) {
        this.targetFolder = targetFolder;
    }

    @Override
    public List<Double> readAllPrices() {
        try (Stream<Path> files = Files.list(targetFolder)) {
            return files
                    .filter(path -> path.toString().endsWith(".txt"))
                    .map(this::extractPrice)
                    .filter(price -> price > 0)
                    .toList();
        } catch (IOException e) {
            return List.of();
        }
    }

    private double extractPrice(Path file) {
        try {
            return Files.lines(file)
                    .filter(line -> line.startsWith("Ukupna cijena:"))
                    .findFirst()
                    .map(line -> line.replace("Ukupna cijena:", "")
                            .replace("KM", "")
                            .trim())
                    .map(Double::parseDouble)
                    .orElse(0.0);
        } catch (IOException e) {
            return 0.0;
        }
    }

    @Override
    public int countTickets() {
        try (Stream<Path> files = Files.list(targetFolder)) {
            return (int) files
                    .filter(path -> path.toString().endsWith(".txt"))
                    .count();
        } catch (IOException e) {
            return 0;
        }
    }
}
