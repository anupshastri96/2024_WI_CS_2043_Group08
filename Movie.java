import java.util.ArrayList;
import java.util.List;

public class Movie implements Comparable<Movie> {
    private final String title;
    private final List<Integer> ratings;

    public Movie(String title) {
        this.title = title;
        this.ratings = new ArrayList<>();
    }
	
	public String getTitle() {
        return title;
    }

    public void addRating(int rating) {
        if (isValidRating(rating)) {
            ratings.add(rating);
        } 
		else {
            System.out.println("Invalid rating. Ratings must be integers between 0 and 5 inclusive.");
        }
    }

    public void clearRatings() {
        ratings.clear();
    }

    public Double getAverageRating() {
        if (ratings.isEmpty()) {
            return null;
        }
        int sum = 0;
		for (int i = 0; i < ratings.size(); i++) {
		   sum += ratings.get(i);
    }

    return (double) sum / ratings.size();
	}

    private boolean isValidRating(int rating) {
        return rating >= 0 && rating <= 5;
    }
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Movie movie = (Movie) obj;
        return title.equals(movie.title);
    }


	@Override
	public int compareTo(Movie other) {
		Double otherRating = other.getAverageRating();
		Double thisRating = this.getAverageRating();

		if (otherRating == null && thisRating == null) {
			return 0; 
		} else if (otherRating == null) {
			return -1; 
		} else if (thisRating == null) {
			return 1; 
		} else {
			return Double.compare(otherRating, thisRating);
		}
	}
	
	public String toString() {
	    return "Movie{" +"title='" + title + '\'' +", ratings=" + ratings +", averageRating=" + getAverageRating() +'}';
	}




}

