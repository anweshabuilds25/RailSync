public class RaceConditionDemo {
    private static int seatsLeft = 1;

    public static void main(String[] args) throws InterruptedException {
        Runnable bookingAttempt = () -> {
            if (seatsLeft > 0) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                seatsLeft = seatsLeft - 1;
                System.out.println(Thread.currentThread().getName() + " booked the seat!");
            } else {
                System.out.println(Thread.currentThread().getName() + " found it sold out.");
            }
        };

        Thread customerA = new Thread(bookingAttempt, "Customer-A");
        Thread customerB = new Thread(bookingAttempt, "Customer-B");

        customerA.start();
        customerB.start();

        customerA.join();
        customerB.join();

        System.out.println("Final seatsLeft value: " + seatsLeft);
    }
}