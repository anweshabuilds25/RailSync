public class Seat {
    private final int seatNumber;
    private final String seatClass;
    private volatile boolean isBooked;

    public Seat(int seatNumber, String seatClass) {
        this.seatNumber = seatNumber;
        this.seatClass = seatClass;
        this.isBooked = false;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        this.isBooked = booked;
    }

    @Override
    public String toString() {
        return "Seat{" + seatNumber + ", " + seatClass + ", booked=" + isBooked + "}";
    }
}