import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadFile {
    // Method to read movies from a text file and return an array of Movie objects
    public static List<Movie> readMoviesFromFile(String filePath) throws FileNotFoundException {
        List<Movie> movies = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filePath));

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] details = line.split(",");

            if (details.length == 4) { // Check if the split line has exactly four elements: name, duration, airtime,
                                       // and rating
                String name = details[0];// Extract the movie name from the first element
                String duration = details[1];// Extract the movie duration from the second element
                String airtime = details[2];// Extract the movie airtime from the third element.
                double rating = Double.parseDouble(details[3]);// Convert the fourth element from String to double for
                                                               // the rating
                movies.add(new Movie(name, duration, airtime, rating)); // Create a new Movie object and add it to the
                                                                        // list.
            }
        }

        scanner.close(); // closes the scanner
        return movies;
    }

    // Method to add movies to a list and serialize the list to a file
    public static void saveMoviesToFile(List<Ticket> tickets, String filePath) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
        oos.writeObject(tickets);
        oos.close(); // closes output stream
    }

    public static void addMovieToList(List<Movie> movieList, Movie movieToAdd) {
        // Check if the movie list already contains a movie with the same title
        boolean movieExists = false;
        for (Movie movie : movieList) {
            if (movie.getTitle().equals(movieToAdd.getTitle())) {
                movieExists = true;
                break; // Exit the loop as we found a movie with the same title
            }
        }

        // If the movie does not exist in the list, add it
        if (!movieExists) {
            movieList.add(movieToAdd);
            System.out.println("Movie added to the list: " + movieToAdd.getTitle());
        } else {
            System.out.println("Movie already exists in the list: " + movieToAdd.getTitle());
        }
    }

    public static ArrayList<Ticket> readListFromFile(String file) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        ArrayList<Ticket> ticketList = (ArrayList<Ticket>) ois.readObject();
        ois.close();
        return ticketList;
    }
}