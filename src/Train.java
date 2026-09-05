import java.util.ArrayList;
import java.util.List;

public class Train {
    private final String trainId;
    private final String trainName;
    private final String route;
    private final List<Seat> seats;

    public Train(String trainId, String trainName, String route) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.route = route;
        this.seats = new ArrayList<>();
    }

    public void addSeat(Seat seat) {
        seats.add(seat);
    }

    public String getTrainId() {
        return trainId;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getRoute() {
        return route;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public long getAvailableSeatCount() {
        return seats.stream().filter(s -> !s.isBooked()).count();
    }

    public void printAvailability() {
        System.out.println("Train: " + trainName + " (" + trainId + ") - Route: " + route);
        for (Seat s : seats) {
            System.out.println("  " + s);
        }
    }
}