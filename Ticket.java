import java.io.Serializable;

public class Ticket implements Serializable {
    private Movie movie;
    private TheatreRoom room;
    private int seatNum;
    private String ticketType;

    public Ticket(Movie movie, TheatreRoom room, String ticketType, int seatNum) {
        this.movie = movie;
        this.room = room;
        this.seatNum = seatNum;
        this.ticketType = ticketType;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public String getTicketType() {
        return ticketType;
    }

    public String toString() {
        return "Ticket{" +
                "name='" + movie.getName() + '\'' +
                ", duration=" + movie.getDuration() +
                ", rating=" + movie.getRating() +
                ", airingTime='" + movie.getAiringTime() + '\'' +
                ", Theatre Room Number: " + room.getRoomNum() +
                ", Seat Number: " + getSeatNum() +
                ", Ticket Type=" + getTicketType() +
                ", Price=$" + getPrice() +
                '}';
    }
}
