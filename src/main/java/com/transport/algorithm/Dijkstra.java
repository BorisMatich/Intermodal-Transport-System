package com.transport.algorithm;

import com.transport.model.Departure;
import com.transport.model.Station;

import java.time.LocalTime;
import java.util.*;

import static com.transport.algorithm.RouteUtils.calculateDepartureWeight;


public class Dijkstra {
    private final Map<Station, List<Departure>> departuresByOrigin;
    private final SearchCriteria criteria;

    public Dijkstra(Map<Station, List<Departure>> departuresByOrigin, SearchCriteria criteria) {
        this.departuresByOrigin = departuresByOrigin;
        this.criteria = criteria;
    }

    public Optional<RouteResult> findShortestPath(Station start, Station end, LocalTime departureTime) {
        PriorityQueue<State> pq = new PriorityQueue<>();
        Map<Station, Double> visited = new HashMap<>();

        Node startNode = new Node(start, departureTime);
        pq.offer(new State(startNode, 0.0, new ArrayList<>()));

        while (!pq.isEmpty()) {
            State current = pq.poll();
            Station currentStation = current.node().station();
            LocalTime currentTime = current.node().time();

            // Stigli smo do cilja
            if (currentStation.equals(end)) {
                return Optional.of(new RouteResult(current.path(), departureTime));
            }

            // Već posjećeno sa boljom cijenom
            if (visited.containsKey(currentStation) && visited.get(currentStation) <= current.weight()) {
                continue;
            }
            visited.put(currentStation, current.weight());

            // Prođi sve polaske iz trenutne stanice
            List<Departure> departures = departuresByOrigin.getOrDefault(currentStation, Collections.emptyList());

            for (Departure dep : departures) {
                if (!canCatchDeparture(currentTime, dep, current.path())) {
                    continue;
                }

                double newWeight = current.weight() + calculateDepartureWeight(dep, criteria, currentTime);
                Node nextNode = new Node(dep.getTo(), dep.getArrivalTime());

                List<Departure> newPath = new ArrayList<>(current.path());
                newPath.add(dep);

                pq.offer(new State(nextNode, newWeight, newPath));
            }

            // Transfer unutar istog grada (različite stanice)
            addTransferOptions(current, pq);
        }

        return Optional.empty();
    }

    private boolean canCatchDeparture(LocalTime currentTime, Departure dep, List<Departure> path) {
        // Ako je prva stanica, samo provjeri da polazak nije prošao
        if (path.isEmpty()) {
            return !dep.getDepartureTime().isBefore(currentTime);
        }

        // Inače, dodaj minTransferTime
        Departure lastDep = path.get(path.size() - 1);
        LocalTime earliestDeparture = lastDep.getArrivalTime().plus(dep.getMinTransferTime());
        return !dep.getDepartureTime().isBefore(earliestDeparture);
    }

    private void addTransferOptions(State current, PriorityQueue<State> pq) {
        Station currentStation = current.node().station();
        String currentCity = currentStation.getCity();

        // Nađi sve stanice u istom gradu
        for (Station station : departuresByOrigin.keySet()) {
            if (station.getCity().equals(currentCity) && !station.equals(currentStation)) {
                // Transfer na drugu stanicu u istom gradu (bez cijene, samo vrijeme)
                Node transferNode = new Node(station, current.node().time());
                pq.offer(new State(transferNode, current.weight(), current.path()));
            }
        }
    }

}
