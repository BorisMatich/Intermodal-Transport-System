package com.transport.algorithm;

import com.transport.model.Departure;

import java.util.List;

record State(Node node, double weight, List<Departure> path)
        implements Comparable<State> {
    @Override
    public int compareTo(State other) {
        return Double.compare(this.weight, other.weight);
    }

}