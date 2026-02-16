package com.transport.algorithm;
import com.transport.model.Station;

import java.time.LocalTime;
import java.util.Objects;

public record Node(Station station, LocalTime time) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(station, node.station);
    }

    @Override
    public int hashCode() {
        return Objects.hash(station);
    }
}