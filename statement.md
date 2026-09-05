# Problem Statement

During high-demand booking windows — such as India's IRCTC tatkal booking rush, flight sale windows, or exam registration — large numbers of users attempt to book the same limited set of seats within seconds of each other. A naively implemented booking system can suffer from a race condition, where the "check if available" and "mark as booked" steps are not atomic, allowing more than one user to successfully book the same seat. This is a well-documented class of concurrency bug that affects real-world booking and inventory systems at scale.

# Scope

RailSync is a console-based Java application that:
- Models trains, seats, and customers
- Implements a concurrent, thread-safe booking engine using Java's `synchronized` keyword
- Demonstrates the race condition bug in an intentionally unsynchronized version, then shows the fix
- Supports seat cancellation with automatic waitlist promotion
- Generates booking reports

The project is scoped as a single-process, in-memory simulation intended to demonstrate core Java concurrency concepts (OOP, exception handling, multithreading) rather than a production-scale distributed booking system.

# Target Users

- Primary: an evaluator/instructor assessing understanding of Java concurrency concepts
- Illustrative real-world users: passengers attempting to book train tickets during a high-demand rush window

# High-Level Features

- Seat and train management
- Thread-safe concurrent seat booking (`synchronized` methods)
- Automatic waitlist queue with promotion on cancellation
- Booking cancellation
- Booking/seat status reports
- Custom checked exceptions for invalid input and unavailable seats
- CLI menu for interactive use
- Standalone demos proving the race condition and its fix