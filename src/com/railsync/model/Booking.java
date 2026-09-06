package com.railsync.model;

public class Booking {
    private final Customer customer;
    private final Seat seat;
    private final Train train;
    private final long bookingTime;

    public Booking(Customer customer, Seat seat, Train train) {
        this.customer = customer;
        this.seat = seat;
        this.train = train;
        this.bookingTime = System.currentTimeMillis();
    }

    public Customer getCustomer() {
        return customer;
    }

    public Seat getSeat() {
        return seat;
    }

    public Train getTrain() {
        return train;
    }

    public long getBookingTime() {
        return bookingTime;
    }

    @Override
    public String toString() {
        return "Booking{customer=" + customer.getName() +
               ", seat=" + seat.getSeatNumber() +
               ", train=" + train.getTrainId() + "}";
    }
}