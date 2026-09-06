package com.railsync.service;

import com.railsync.model.Booking;
import com.railsync.model.Customer;
import com.railsync.model.Seat;
import com.railsync.model.Train;
import com.railsync.exception.SeatNotAvailableException;
import com.railsync.exception.InvalidSeatException;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class BookingSystemSafe {
    private final Train train;
    private final Map<Integer, Booking> bookings;
    private final Map<Integer, Queue<Customer>> waitlists;

    public BookingSystemSafe(Train train) {
        this.train = train;
        this.bookings = new ConcurrentHashMap<>();
        this.waitlists = new HashMap<>();

        for (Seat s : train.getSeats()) {
            waitlists.put(s.getSeatNumber(), new LinkedList<>());
        }
    }

    public synchronized void bookSeat(Customer customer, int seatNumber) throws SeatNotAvailableException, InvalidSeatException {
        Seat targetSeat = findSeat(seatNumber);

        if (targetSeat == null) {
            throw new InvalidSeatException("Seat " + seatNumber + " does not exist on this train.");
        }

        if (!targetSeat.isBooked()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            targetSeat.setBooked(true);
            Booking booking = new Booking(customer, targetSeat, train);
            bookings.put(seatNumber, booking);
            System.out.println(customer.getName() + " successfully booked seat " + seatNumber);
        } else {
            throw new SeatNotAvailableException("Seat " + seatNumber + " is already booked.");
        }
    }

    public synchronized void bookOrWaitlist(Customer customer, int seatNumber) throws InvalidSeatException {
        Seat targetSeat = findSeat(seatNumber);

        if (targetSeat == null) {
            throw new InvalidSeatException("Seat " + seatNumber + " does not exist on this train.");
        }

        try {
            bookSeat(customer, seatNumber);
        } catch (SeatNotAvailableException e) {
            waitlists.get(seatNumber).add(customer);
            System.out.println(customer.getName() + " added to waitlist for seat " + seatNumber);
        }
    }

    public synchronized void cancelBooking(int seatNumber) throws InvalidSeatException {
        Seat targetSeat = findSeat(seatNumber);

        if (targetSeat == null) {
            throw new InvalidSeatException("Seat " + seatNumber + " does not exist on this train.");
        }

        if (!targetSeat.isBooked()) {
            System.out.println("Seat " + seatNumber + " was not booked, nothing to cancel.");
            return;
        }

        Booking cancelledBooking = bookings.remove(seatNumber);
        targetSeat.setBooked(false);
        System.out.println("Cancelled booking for " +
            (cancelledBooking != null ? cancelledBooking.getCustomer().getName() : "unknown customer") +
            " on seat " + seatNumber);

        Queue<Customer> queue = waitlists.get(seatNumber);
        if (!queue.isEmpty()) {
            Customer nextCustomer = queue.poll();
            try {
                bookSeat(nextCustomer, seatNumber);
                System.out.println(nextCustomer.getName() + " auto-assigned seat " + seatNumber + " from waitlist.");
            } catch (SeatNotAvailableException e) {
                System.out.println("Unexpected: could not assign waitlisted customer.");
            }
        }
    }

    public void printReport() {
        System.out.println("\n========== BOOKING REPORT ==========");
        System.out.println("Train: " + train.getTrainName() + " (" + train.getTrainId() + ")");
        System.out.println("Route: " + train.getRoute());
        System.out.println("-------------------------------------");
        for (Seat s : train.getSeats()) {
            String status = s.isBooked() ? "BOOKED" : "AVAILABLE";
            int waitlistSize = waitlists.get(s.getSeatNumber()).size();
            System.out.println("Seat " + s.getSeatNumber() + " (" + s.getSeatClass() + "): " +
                status + " | Waitlist: " + waitlistSize);
        }
        System.out.println("-------------------------------------");
        System.out.println("Total confirmed bookings: " + bookings.size());
        System.out.println("=====================================\n");
    }

    private Seat findSeat(int seatNumber) {
        for (Seat s : train.getSeats()) {
            if (s.getSeatNumber() == seatNumber) {
                return s;
            }
        }
        return null;
    }

    public Map<Integer, Booking> getBookings() {
        return bookings;
    }
}