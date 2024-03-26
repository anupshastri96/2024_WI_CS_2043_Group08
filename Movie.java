
import java.io.*;

public class Movie implements Serializable, Comparable<Movie> {
    private String name;
    private String duration;
    private double rating;
    private String airingTime;

    public Movie(String name, String airingTime, String duration, double rating) {
        this.name = name;
        this.airingTime = airingTime;
        this.duration = duration;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
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

    // CompareTo method to compare movies based on their rating
    @Override
    public int compareTo(Movie other) {
        return Double.compare(this.rating, other.rating);
    }

    /*
     * public static void main(String[] args) {
     * Movie myMovie = new Movie("Interstellar", 169, 4, "09:00 PM");
     * System.out.println(myMovie.toString());
     * }
     */
}
