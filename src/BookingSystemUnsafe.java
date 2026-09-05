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

        // --- THE DANGER ZONE: check and act are two separate steps ---
        if (!targetSeat.isBooked()) {
            // Artificial delay to make the race condition easy to observe
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
