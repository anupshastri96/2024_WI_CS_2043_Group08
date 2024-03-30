import java.io.Serializable;

public class Ticket implements Serializable {
    private Movie movie;
    private TheatreRoom room;
    private int seatNum;
    private double price;
    private int seatRow;
    private int seatCol;

    public Ticket(Movie movie, TheatreRoom room, int seatRow, int seatCol, double price) {
        this.movie = movie;
        this.room = room;
        this.price = price;
        this.seatCol = seatCol;
        this.seatRow = seatRow;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public double getPrice() {
        return price;
    }

    public int getSeatRow() {
        return seatRow;
    }

    public int getSeatCol() {
        return seatCol;
    }

    public String toString() {

        if (movie == null || room == null) {
            return "Ticket info is incomplete";
        }
        return "Ticket{" +
                "name='" + movie.getName() + '\'' +
                ", duration=" + movie.getDuration() +
                ", rating=" + movie.getRating() +
                ", airingTime='" + movie.getAiringTime() + '\'' +
                ", Theatre Room Number: " + room.getRoomNum() +
                ", Seat Row: " + getSeatRow() +
                ", Seat Column: " + getSeatCol() +
                ", Price: " + getPrice() +
                "}\n";
    }
}
