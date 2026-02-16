package com.transport.algorithm;

import com.transport.model.Departure;
import com.transport.model.Station;

import java.time.LocalTime;
import java.util.*;

import static com.transport.algorithm.RouteUtils.calculateRouteWeight;
import static com.transport.algorithm.RouteUtils.samePath;

public class Yen {

    private final Map<Station, List<Departure>> departuresByOrigin;
    private final SearchCriteria criteria;
    private final int k;
    private final LocalTime departureTime;

    public Yen(Map<Station, List<Departure>> departuresByOrigin, SearchCriteria criteria, int k,LocalTime departureTime) {
         this.departureTime = departureTime;
        this.departuresByOrigin = departuresByOrigin;
        this.criteria = criteria;
        this.k = k;
    }

    public List<RouteResult> findKShortestPaths(Station start, Station end, LocalTime departureTime) {
        List<RouteResult> result = new ArrayList<>();

        PriorityQueue<RouteResult> candidates = new PriorityQueue<>(
                Comparator.comparingDouble(r -> calculateRouteWeight(r, criteria))
        );

        Dijkstra dijkstra = new Dijkstra(departuresByOrigin, criteria);
        Optional<RouteResult> firstPath = dijkstra.findShortestPath(start, end, departureTime);

        if (firstPath.isEmpty()) {
            return result;
        }

        result.add(firstPath.get());

        for (int i = 1; i < k; i++) {
            RouteResult previousPath = result.get(i - 1);
            List<Departure> previousDepartures = previousPath.getDepartures();

            for (int j = 0; j < previousDepartures.size(); j++) {
                List<Departure> rootPath = previousDepartures.subList(0, j);

                Station spurNode = (j == 0)
                        ? start
                        : previousDepartures.get(j - 1).getTo();

                LocalTime spurTime = (j == 0)
                        ? departureTime
                        : previousDepartures.get(j - 1).getArrivalTime();

                Set<Departure> blockedDepartures = findDeparturesToBlock(result, rootPath, j);

                Map<Station, List<Departure>> tempGraph = createTempGraph(blockedDepartures);

                Dijkstra tempDijkstra = new Dijkstra(tempGraph, criteria);
                Optional<RouteResult> spurPath = tempDijkstra.findShortestPath(spurNode, end, spurTime);

                if (spurPath.isPresent()) {
                    List<Departure> totalPath = new ArrayList<>(rootPath);
                    totalPath.addAll(spurPath.get().getDepartures());

                    RouteResult candidate = new RouteResult(totalPath, departureTime);

                    if (!containsPath(result, candidate) && !containsPath(candidates, candidate)) {
                        candidates.offer(candidate);
                    }
                }
            }

            if (candidates.isEmpty()) {
                break;
            }

            result.add(candidates.poll());

        }
        result.sort(Comparator.comparingDouble(r -> RouteUtils.calculateRouteWeight(r, criteria)));

        return result;
    }

    private Set<Departure> findDeparturesToBlock(List<RouteResult> existingPaths,
                                                 List<Departure> rootPath,
                                                 int spurIndex) {
        Set<Departure> blocked = new HashSet<>();

        for (RouteResult existing : existingPaths) {
            List<Departure> existingDeps = existing.getDepartures();

            if (existingDeps.size() > spurIndex && hasSameRootPath(existingDeps, rootPath, spurIndex)) {
                blocked.add(existingDeps.get(spurIndex));
            }
        }

        return blocked;
    }

    private boolean hasSameRootPath(List<Departure> path, List<Departure> rootPath, int length) {
        if (path.size() < length) return false;

        for (int i = 0; i < length; i++) {
            if (!path.get(i).equals(rootPath.get(i))) {
                return false;
            }
        }
        return true;
    }

    private Map<Station, List<Departure>> createTempGraph(Set<Departure> blockedDepartures) {
        Map<Station, List<Departure>> tempGraph = new HashMap<>();

        for (Map.Entry<Station, List<Departure>> entry : departuresByOrigin.entrySet()) {
            List<Departure> filtered = new ArrayList<>();

            for (Departure dep : entry.getValue()) {
                if (!blockedDepartures.contains(dep)) {
                    filtered.add(dep);
                }
            }

            if (!filtered.isEmpty()) {
                tempGraph.put(entry.getKey(), filtered);
            }
        }

        return tempGraph;
    }


    private boolean containsPath(Collection<RouteResult> collection, RouteResult candidate) {
        for (RouteResult existing : collection) {
            if (samePath(existing, candidate)) {
                return true;
            }
        }
        return false;
    }

}