import java.util.HashMap;
import java.util.Map;

public class BookingSystemUnsafe {
    private final Train train;
    private final Map<Integer, Booking> bookings;

    public BookingSystemUnsafe(Train train) {
        this.train = train;
        this.bookings = new HashMap<>();
    }

    public void bookSeat(Customer customer, int seatNumber) throws SeatNotAvailableException, InvalidSeatException {
        Seat targetSeat = findSeat(seatNumber);

        if (targetSeat == null) {
            throw new InvalidSeatException("Seat " + seatNumber + " does not exist on this train.");
        }

        // This is the race condition: checking availability and marking
        // it booked are two separate steps here, with nothing stopping
        // two threads from both passing the check before either books
        if (!targetSeat.isBooked()) {
            // Small artificial delay so the race condition is reliably
            // observable during testing, rather than an inconsistent
            // one-in-a-million occurrence
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