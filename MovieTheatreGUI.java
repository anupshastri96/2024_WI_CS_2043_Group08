import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class MovieTheatreGUI extends JFrame {
    private JButton[] movieButtons;
    private JButton[] ageButtons;
    private JButton[][] seats;
    private JLabel priceDisplay;
    private JButton confirmButton;
    private JButton viewButton;

    JTextField inputTextForCancel;
    JTextArea bookingTextArea;

    private String moviesFile = "MovieText.txt";
    ArrayList<Movie> movieList;
    private ArrayList<Ticket> tickets = new ArrayList<>();

    private Map<Movie, TheatreRoom> newRoomsForMovies = new HashMap<>();

    private double price = 10.0;
    private int selectedMovieIndex = -1;

    private String seatFile = "stateOfSeats.txt";
    private String ticketFile = "ticketFile.txt";

    public MovieTheatreGUI() {
        setTitle("Movie Theatre");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            movieList = readMoviesFromFile(moviesFile);
            tickets = readTicketFromFile("ticketFile.txt");
        } catch (FileNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
            movieList = new ArrayList<>(); // Incase Arraylist is empty
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert the movieList into an array of movie names
        String[] movies = movieList.stream().map(Movie::getName).toArray(String[]::new);

        movieButtons = new JButton[movies.length];
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        JPanel moviePanel = new JPanel();
        movieButtons = new JButton[movies.length];

        for (int i = 0; i < movies.length; i++) {
            JButton button = new JButton(movies[i]);
            int index = i;
            button.addActionListener(e -> selectMovie(index));
            movieButtons[i] = button;
            moviePanel.add(button);
            newTheatreRooms();

        }

        topPanel.add(moviePanel);
        JPanel seatsPanel = new JPanel(new GridLayout(3, 4));
        seats = new JButton[3][4];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                JButton seat = new JButton("Seat " + (i * 4 + j + 1));
                int row = i;
                int col = j;
                seat.addActionListener(e -> seatSelected(row, col));
                seats[i][j] = seat;
                seatsPanel.add(seat);
            }
        }

        bookingTextArea = new JTextArea(10, 30);
        inputTextForCancel = new JTextField("Please enter your ticket number.");
        JPanel bottomPanel = new JPanel();
        priceDisplay = new JLabel("Price: $0.0");
        confirmButton = new JButton("Confirm Booking");
        viewButton = new JButton("View Booking");
        confirmButton.addActionListener(e -> confirmBooking());
        viewButton.addActionListener(e -> viewBooking());
        bottomPanel.add(priceDisplay);
        bottomPanel.add(confirmButton);
        bottomPanel.add(viewButton);
        add(topPanel, BorderLayout.NORTH);
        add(seatsPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        restoreSeatsForMovie1(selectedMovieIndex);
    }

    private void newTheatreRooms() {
        int roomNum = 1; // first theatre room

        for (Movie m : movieList) {
            TheatreRoom room = new TheatreRoom(m, roomNum++);
            newRoomsForMovies.put(m, room);
        }
    }

    private void viewBooking() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tickets.size(); i++) {
            sb.append(i).append(tickets.get(i).toString()).append("\n");
        }
        bookingTextArea.setText(sb.toString());

        JOptionPane.showMessageDialog(null, sb.toString(), "Ticket List", JOptionPane.INFORMATION_MESSAGE);
        if (!tickets.isEmpty()) {
            try {
                String index = JOptionPane.showInputDialog("Enter Ticket you would like to cancel");
                if (index != null && !index.isEmpty()) {
                    int removing = Integer.parseInt(index);
                    removeTicket(removing);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Something went wrong, please try again :(");
            }
        }
    }

    private void removeTicket(int toRemove) {
        if (toRemove >= 0 && toRemove < tickets.size()) {
            Ticket removedTicket = tickets.remove(toRemove);
            int row = removedTicket.getSeatRow(); // Or calculate based on seatNum
            int column = removedTicket.getSeatCol(); // Or calculate based on seatNum
            seats[row][column].setBackground(null);
            JOptionPane.showMessageDialog(this, "Ticket Cancelled Successfully");
            writeTicket(tickets, "ticketFile.txt");
        } else {
            JOptionPane.showMessageDialog(this, "Ticket does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selectMovie(int index) {
        selectedMovieIndex = index;
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (index == 0) {
                    restoreSeatsForMovie1(index);
                }
                if (index == 1) {
                    restoreSeatsForMovie2(index);
                }
                if (index == 2) {
                    restoreSeatsForMovie3(index);
                }
            }

        }
        updatePrice();

    }

    private void updatePrice() {
        if (selectedMovieIndex != -1) {
            // price = prices[selectedMovieIndex][selectedAgeIndex];
            priceDisplay.setText("Price: $" + price);
        }
    }

    private void seatSelected(int row, int col) {
        if (seats[row][col].getBackground().equals(Color.RED)) {
            seats[row][col].setBackground(null);
            JOptionPane.showMessageDialog(this,
                    "You have deselected Seat " + (row * 4 + col + 1));
        } else {
            seats[row][col].setBackground(Color.RED);
            JOptionPane.showMessageDialog(this,
                    "You have selected Seat " + (row * 4 + col + 1) + ". Price: $" + price);

        }

    }

    private void confirmBooking() {

        Movie movie = movieList.get(selectedMovieIndex);
        TheatreRoom newRoom = newRoomsForMovies.get(movie);

        boolean seatSelected = false;
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j].getBackground() == Color.RED) {
                    seatSelected = true;
                    Ticket ticket = new Ticket(movie, newRoom, i, j, price);
                    tickets.add(ticket); // Create and add a Ticket for the selected seat
                    if (selectedMovieIndex == 0) {
                        saveSeats("MovieOne.txt");

                    }
                    if (selectedMovieIndex == 1) {
                        saveSeats("MovieTwo.txt");

                    }
                    if (selectedMovieIndex == 2) {
                        saveSeats("MovieTwo.txt");
                    }
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter("ListOfTickets.txt"))) {
                        bw.write(ticket.toString());
                        bw.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        if (seatSelected) {
            JOptionPane.showMessageDialog(this, "Booking confirmed! Price: $" + price);
            writeTicket(tickets, "ticketFile.txt");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a seat before confirming the booking.");
        }

    }

    private void cancelBooking() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel the booking?",
                "Cancel Booking", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            for (int i = 0; i < seats.length; i++) {
                for (int j = 0; j < seats[i].length; j++) {
                    if (seats[i][j].getBackground() == Color.RED) {
                        seats[i][j].setBackground(null);
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "Booking canceled!");
        }
    }

    private void saveSeats(String file) {
        try {
            BufferedWriter saveSeatInfo = new BufferedWriter(new FileWriter(file));
            BufferedWriter saveSeatInfo2 = new BufferedWriter(new FileWriter("MovieTwo.txt"));
            BufferedWriter saveSeatInfo3 = new BufferedWriter(new FileWriter("MovieThree.txt"));

            for (int i = 0; i < seats.length; i++) {
                for (int j = 0; j < seats[i].length; j++) {
                    if (Color.RED.equals(seats[i][j].getBackground())) {
                        if (selectedMovieIndex == 0) {
                            saveSeatInfo.write("[O]");
                            saveSeatInfo.newLine();

                        }
                        if (selectedMovieIndex == 1) {
                            saveSeatInfo2.write("[O]");
                            saveSeatInfo2.newLine();

                        }
                        if (selectedMovieIndex == 2) {
                            saveSeatInfo3.write("[O]");
                            saveSeatInfo3.newLine();

                        }

                    }

                    else {
                        saveSeatInfo.write("[V]");
                        saveSeatInfo.newLine();
                        saveSeatInfo2.write("[V]");
                        saveSeatInfo2.newLine();
                        saveSeatInfo3.write("[V]");
                        saveSeatInfo3.newLine();
                    }
                }
            }

            saveSeatInfo.flush();
            saveSeatInfo.close();
            saveSeatInfo2.flush();
            saveSeatInfo2.close();
            saveSeatInfo3.flush();
            saveSeatInfo3.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void restoreSeatsForMovie1(int movie) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(seatFile));

            String line;
            int i = 0;
            while ((line = br.readLine()) != null && i < seats.length * seats[0].length) {
                int r = i / seats[0].length; // row = i / number of columns
                int c = i % seats[0].length; // columns = remainder of when i / columns
                if (line.equals("[O]")) {
                    seats[r][c].setBackground(Color.RED);
                } else if (line.equals("[V]")) {
                    seats[r][c].setBackground(null);
                }
                i++;

            }
            br.close();

        } catch (Exception e) {

        }
    }

    private void restoreSeatsForMovie2(int movie) {
        try {
            BufferedReader br2 = new BufferedReader(new FileReader("MovieTwo.txt"));

            String line;
            int i = 0;
            while ((line = br2.readLine()) != null && i < seats.length * seats[0].length) {
                int r = i / seats[0].length; // row = i / number of columns
                int c = i % seats[0].length; // columns = remainder of when i / columns
                if (line.equals("[O]")) {
                    seats[r][c].setBackground(Color.RED);
                } else if (line.equals("[V]")) {
                    seats[r][c].setBackground(null);
                }
                i++;

            }
            br2.close();

        } catch (Exception e) {

        }
    }

    private void restoreSeatsForMovie3(int movie) {
        try {
            BufferedReader br3 = new BufferedReader(new FileReader("MovieThree.txt"));

            String line;
            int i = 0;
            while ((line = br3.readLine()) != null && i < seats.length * seats[0].length) {
                int r = i / seats[0].length; // row = i / number of columns
                int c = i % seats[0].length; // columns = remainder of when i / columns
                if (line.equals("[O]")) {
                    seats[r][c].setBackground(Color.RED);
                } else if (line.equals("[V]")) {
                    seats[r][c].setBackground(null);
                }
                i++;

            }
            br3.close();

        } catch (Exception e) {

        }
    }

    private void writeTicket(ArrayList<Ticket> tickets, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(tickets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Ticket> readTicketFromFile(String file) throws IOException, ClassNotFoundException {
        File f = new File(file);
        if (!f.exists() || f.length() == 0) {
            return new ArrayList<>(); // Return an empty list if the file doesn't exist or is empty
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<Ticket>) ois.readObject();
        }
    }

    public static ArrayList<Movie> readMoviesFromFile(String filePath) throws FileNotFoundException {
        ArrayList<Movie> movies = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filePath));
        int x = 0;
        while (scanner.hasNextLine() && x <= 2) {
            String line = scanner.nextLine();
            String[] details = line.split(",");

            if (details.length == 4) { // Check if the split line has exactly four elements: name, duration, airtime,
                                       // and rating
                String name = details[0];// Extract the movie name from the first element
                String airtime = details[1];// Extract the movie airtime from the second element.
                String duration = details[2];// Extract the movie duration from the third element
                double rating = Double.parseDouble(details[3]);// Convert the fourth element from String to double for
                                                               // the rating
                movies.add(new Movie(name, airtime, duration, rating)); // Create a new Movie object and add it to the
                x++; // list.
            }

        }

        scanner.close(); // closes the scanner
        return movies;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MovieTheatreGUI();
            }
        });
    }
}
