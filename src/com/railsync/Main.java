package com.railsync;

import com.railsync.model.Customer;
import com.railsync.model.Seat;
import com.railsync.model.Train;
import com.railsync.service.BookingSystemSafe;
import com.railsync.exception.InvalidSeatException;
import com.railsync.exception.InvalidBookingRequestException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Train train = new Train("T101", "Rajdhani Express", "Delhi to Mumbai");
        for (int i = 1; i <= 5; i++) {
            train.addSeat(new Seat(i, i <= 2 ? "AC1" : "AC2"));
        }

        BookingSystemSafe bookingSystem = new BookingSystemSafe(train);

        boolean keepRunning = true;
        while (keepRunning) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleBookSeat(scanner, bookingSystem);
                    break;
                case "2":
                    handleCancelBooking(scanner, bookingSystem);
                    break;
                case "3":
                    bookingSystem.printReport();
                    break;
                case "4":
                    handleBookingRush(bookingSystem);
                    break;
                case "5":
                    keepRunning = false;
                    System.out.println("Exiting RailSync. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice, please enter a number from 1-5.");
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n===== RailSync: Ticket Booking Rush System =====");
        System.out.println("1. Book a seat");
        System.out.println("2. Cancel a booking");
        System.out.println("3. View booking report");
        System.out.println("4. Simulate a concurrent booking rush (demo)");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void handleBookSeat(Scanner scanner, BookingSystemSafe bookingSystem) {
        try {
            System.out.print("Enter your name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter seat number to book: ");
            int seatNumber = Integer.parseInt(scanner.nextLine().trim());

            if (name.isEmpty()) {
                throw new InvalidBookingRequestException("Customer name cannot be empty.");
            }

            Customer customer = new Customer("C-" + System.currentTimeMillis(), name);
            bookingSystem.bookOrWaitlist(customer, seatNumber);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input: seat number must be a number.");
        } catch (InvalidSeatException | InvalidBookingRequestException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
    }

    private static void handleCancelBooking(Scanner scanner, BookingSystemSafe bookingSystem) {
        try {
            System.out.print("Enter seat number to cancel: ");
            int seatNumber = Integer.parseInt(scanner.nextLine().trim());
            bookingSystem.cancelBooking(seatNumber);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: seat number must be a number.");
        } catch (InvalidSeatException e) {
            System.out.println("Cancellation failed: " + e.getMessage());
        }
    }

    private static void handleBookingRush(BookingSystemSafe bookingSystem) {
        System.out.println("\nSimulating 8 customers rushing to book seat 1 at the same time...");

        Runnable bookingTask = () -> {
            Customer customer = new Customer(
                Thread.currentThread().getName(),
                Thread.currentThread().getName()
            );
            try {
                bookingSystem.bookOrWaitlist(customer, 1);
            } catch (InvalidSeatException e) {
                System.out.println(Thread.currentThread().getName() + " FAILED: " + e.getMessage());
            }
        };

        Thread[] customers = new Thread[8];
        for (int i = 0; i < 8; i++) {
            customers[i] = new Thread(bookingTask, "RushCustomer-" + (i + 1));
        }

        for (Thread t : customers) {
            t.start();
        }
        for (Thread t : customers) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Booking rush simulation complete.");
        bookingSystem.printReport();
    }
}