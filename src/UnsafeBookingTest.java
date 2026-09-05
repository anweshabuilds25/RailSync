public class UnsafeBookingTest {
    public static void main(String[] args) throws InterruptedException {
        // A train with just ONE seat, so a race condition is easy to trigger
        Train train = new Train("T101", "Rajdhani Express", "Delhi to Mumbai");
        train.addSeat(new Seat(1, "AC1"));

        BookingSystemUnsafe bookingSystem = new BookingSystemUnsafe(train);

        // 5 customers will all try to book the SAME seat (seat 1) at once
        Runnable bookingTask = () -> {
            Customer customer = new Customer(
                Thread.currentThread().getName(),
                Thread.currentThread().getName()
            );
            try {
                bookingSystem.bookSeat(customer, 1);
            } catch (SeatNotAvailableException | InvalidSeatException e) {
                System.out.println(Thread.currentThread().getName() + " FAILED: " + e.getMessage());
            }
        };

        Thread[] customers = new Thread[5];
        for (int i = 0; i < 5; i++) {
            customers[i] = new Thread(bookingTask, "Customer-" + (i + 1));
        }

        for (Thread t : customers) {
            t.start();
        }
        for (Thread t : customers) {
            t.join();
        }

        System.out.println("\n--- RESULT ---");
        System.out.println("Total successful bookings recorded: " + bookingSystem.getBookings().size());
        System.out.println("(There is only 1 seat — if this number is greater than 1, the bug occurred!)");
    }
}