import java.io.*;

public class Movie implements Serializable {
    private String name;
    private int duration;
    private double rating;
    private String airingTime;

    public Movie(String name, int duration, double rating, String airingTime) {
        this.name = name;
        this.duration = duration;
        this.rating = rating;
        this.airingTime = airingTime;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public double getRating() {
        return rating;
    }

    public String getAiringTime() {
        return airingTime;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", rating=" + rating +
                ", airingTime='" + airingTime + '\'' +
                '}';
    }

    public static void main(String[] args) {
        Movie myMovie = new Movie("Interstellar", 169, 4, "09:00 PM");
        System.out.println(myMovie.toString());
    }
}
