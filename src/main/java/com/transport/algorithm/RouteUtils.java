package com.transport.algorithm;

import com.transport.model.Departure;

import java.time.LocalTime;
import java.util.List;

public class RouteUtils {


    public static double calculateDepartureWeight(Departure dep, SearchCriteria criteria, LocalTime currentTime) {
        return switch (criteria) {
            case PRICE -> dep.getPrice();
            case DURATION -> {
                long waitingMinutes = java.time.Duration.between(currentTime, dep.getDepartureTime()).toMinutes();
                if (waitingMinutes < 0) waitingMinutes += 24 * 60;
                yield waitingMinutes + dep.getDuration().toMinutes();
            }
            case LEAST_TRANSFERS -> {
                // Primarno: 1 presjedanje = 1,000,000 bodova
                // Sekundarno: cijena (max nekoliko hiljada)
                // Ovo garantuje da presjedanja uvijek imaju prioritet nad cijenom
                yield 1_000_000 + dep.getPrice();
            }
        };
    }

    public static double calculateRouteWeight(RouteResult route, SearchCriteria criteria) {
        return switch (criteria) {
            case PRICE -> route.getTotalPrice();
            case DURATION -> route.getTotalDuration().toMinutes();
            case LEAST_TRANSFERS -> {
                // Primarno: broj presjedanja * veliki broj
                // Sekundarno: cijena
                yield route.getDepartures().size() * 1_000_000 + route.getTotalPrice();
            }
        };
    }

    public static boolean samePath(RouteResult a, RouteResult b) {
        List<Departure> depsA = a.getDepartures();
        List<Departure> depsB = b.getDepartures();

        if (depsA.size() != depsB.size()) return false;

        for (int i = 0; i < depsA.size(); i++) {
            if (!depsA.get(i).equals(depsB.get(i))) {
                return false;
            }
        }
        return true;
    }
}
