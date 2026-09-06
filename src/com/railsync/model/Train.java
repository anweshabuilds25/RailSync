package com.railsync.model;

import java.util.ArrayList;
import java.util.List;

public class Train {
    private final String trainId;
    private final String trainName;
    private final String route;
    private final List<Seat> seats;

    public Train(String trainId, String trainName, String route) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.route = route;
        this.seats = new ArrayList<>();
    }

    public void addSeat(Seat seat) {
        seats.add(seat);
    }

    public String getTrainId() {
        return trainId;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getRoute() {
        return route;
    }

    // Returning the live list here is a deliberate simplification for this
    // project's scope — only individual Seat objects are mutated
    // concurrently, not this list itself, so it's safe as-is
    public List<Seat> getSeats() {
        return seats;
    }

    public long getAvailableSeatCount() {
        return seats.stream().filter(s -> !s.isBooked()).count();
    }

    public void printAvailability() {
        System.out.println("Train: " + trainName + " (" + trainId + ") - Route: " + route);
        for (Seat s : seats) {
            System.out.println("  " + s);
        }
    }
}