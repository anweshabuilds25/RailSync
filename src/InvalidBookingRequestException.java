// Thrown for bad input, such as an empty customer name
public class InvalidBookingRequestException extends Exception {
    public InvalidBookingRequestException(String message) {
        super(message);
    }
}