import java.io.Serializable;

public class TheatreRoom implements Serializable {
    private int roomNum;
    private Movie movie;

    public TheatreRoom(Movie movie, int roomNum) {
        this.roomNum = roomNum;
        this.movie = movie;
    }

    public int getRoomNum() {
        return roomNum;
    }

    @Override
    public String toString() {
        return ", Theatre Room Number: " + getRoomNum() +
                '}';
    }

}
