
//Majd's Driver class
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MovieDriver {
    public static void main(String[] args) {

        try {
            String newFile = "readFiles.txt";
            String movieFile = "MovieText.txt";
            ArrayList<Ticket> list = new ArrayList<>();
            ArrayList<Movie> movies = readMoviesFromFile(movieFile);
            /*
             * Movie m1 = new Movie("Interstellar", 185, 4.5, "9:00PM");
             * Movie m2 = new Movie("Batman", 185, 4.7, "5:00PM");
             * Movie m3 = new Movie("The Flash", 185, 2.8, "7:00PM");
             */

            TheatreRoom room1 = new TheatreRoom(movies.get(0), 0);
            TheatreRoom room2 = new TheatreRoom(movies.get(1), 1);
            TheatreRoom room3 = new TheatreRoom(movies.get(2), 2);

            Ticket t1 = new Ticket(movies.get(0), room1, 0);
            Ticket t2 = new Ticket(movies.get(1), room2, 1);
            Ticket t3 = new Ticket(movies.get(2), room3, 2);

            list.add(t1);
            list.add(t2);
            list.add(t3);

            for (int i = 0; i < movies.size(); i++) {
                System.out.println(movies.get(i).toString());
            }
            System.out.println();
            writeTicket(list);

            System.out.println("Tickets have been written to ticketFile.txt");
            try {
                ArrayList<Ticket> ticketsRead = readListFromFile("ticketFile.txt");
                System.out.println("Tickets have been read from ticketFile.txt");
                System.out.println();
                for (Ticket t : ticketsRead) {
                    System.out.println(t);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeTicket(ArrayList<Ticket> tickets) {
        try {
            ObjectOutputStream bw = new ObjectOutputStream(new FileOutputStream("ticketFile.txt"));
            bw.writeObject(tickets);
        } catch (Exception e) {
        }
    }

    public static ArrayList<Ticket> readListFromFile(String file) throws IOException, ClassNotFoundException {// Reads
                                                                                                              // tickets
                                                                                                              // from
                                                                                                              // file
                                                                                                              // for
                                                                                                              // restoration
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        ArrayList<Ticket> ticketList = (ArrayList<Ticket>) ois.readObject();
        ois.close();
        return ticketList;
    }

    public static ArrayList<Movie> readMoviesFromFile(String filePath) throws FileNotFoundException {
        ArrayList<Movie> movies = new ArrayList<>();
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
                movies.add(new Movie(name, airtime, duration, rating)); // Create a new Movie object and add it to the
                                                                        // list.
            }
        }

        scanner.close(); // closes the scanner
        return movies;
    }
}
