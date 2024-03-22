public class Ticket {
    private Movie movie;
    private TheatreRoom room;
    private int seatNum;

    public Ticket(Movie movie, TheatreRoom room, int seatNum) {
        this.movie = movie;
        this.room = room;
        this.seatNum = seatNum;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public String toString() {
        return "Movie{" +
                "name='" + movie.getName() + '\'' +
                ", duration=" + movie.getDuration() +
                ", rating=" + movie.getRating() +
                ", airingTime='" + movie.getAiringTime() + '\'' +
                ", Theatre Room Number: " + room.getRoomNum() +
                ", Seat Number: " + getSeatNum() +
                '}';
    }
}
