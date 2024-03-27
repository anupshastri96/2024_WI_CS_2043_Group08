import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
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

public class MovieTheatreGUI extends JFrame {
    private JButton[] movieButtons;
    private JButton[] ageButtons;
    private JButton[][] seats;
    private JLabel priceDisplay;
    private JButton confirmButton;
    private JButton cancelButton;

    private String moviesFile = "MovieText.txt";
    ArrayList<Movie> movieList;

    private String[] ages = { "Senior", "Child", "Adult" };
    private double[][] prices = { { 10.0, 9.0, 13.0 },
            { 10.0, 9.0, 13.0 },
            { 10.0, 9.0, 13.0 } };
    private double selectedPrice = 0.0;
    private int selectedMovieIndex = -1;
    private int selectedAgeIndex = -1;
    private String seatFile = "stateOfSeats.txt";
    private String ticketFile = "tickets.txt";

    public MovieTheatreGUI() {
        setTitle("Movie Theatre");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            movieList = readMoviesFromFile(moviesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            movieList = new ArrayList<>(); // incase file is not found, fall back to empty list
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
        }

        JPanel agePanel = new JPanel();
        ageButtons = new JButton[ages.length];
        for (int i = 0; i < ages.length; i++) {
            JButton button = new JButton(ages[i]);
            int index = i;
            button.addActionListener(e -> selectAge(index));
            ageButtons[i] = button;
            agePanel.add(button);
        }

        topPanel.add(moviePanel);
        topPanel.add(agePanel);
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

        JPanel bottomPanel = new JPanel();
        priceDisplay = new JLabel("Price: $0.0");
        confirmButton = new JButton("Confirm Booking");
        cancelButton = new JButton("Cancel Booking");
        confirmButton.addActionListener(e -> confirmBooking());
        cancelButton.addActionListener(e -> cancelBooking());
        bottomPanel.add(priceDisplay);
        bottomPanel.add(confirmButton);
        bottomPanel.add(cancelButton);
        add(topPanel, BorderLayout.NORTH);
        add(seatsPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        restoreSeats();

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                saveSeats(seatFile);
            }
        });
        this.setVisible(true);

    }

    private void selectMovie(int index) {
        selectedMovieIndex = index;
        updatePrice();
    }

    private void selectAge(int index) {
        selectedAgeIndex = index;
        updatePrice();
    }

    private void updatePrice() {
        if (selectedMovieIndex != -1 && selectedAgeIndex != -1) {
            selectedPrice = prices[selectedMovieIndex][selectedAgeIndex];
            priceDisplay.setText("Price: $" + selectedPrice);
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
                    "You have selected Seat " + (row * 4 + col + 1) + ". Price: $" + selectedPrice);

        }

    }

    private void confirmBooking() {
        boolean seatSelected = false;
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j].getBackground() == Color.RED) {
                    seatSelected = true;
                    break;
                }
            }
        }

        if (seatSelected) {
            JOptionPane.showMessageDialog(this, "Booking confirmed! Price: $" + selectedPrice);
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
            for (int i = 0; i < seats.length; i++) {
                for (int j = 0; j < seats[i].length; j++) {
                    if (seats[i][j].getBackground().equals(Color.RED)) {
                        saveSeatInfo.write("[O]");
                        saveSeatInfo.newLine();
                    }

                    else {
                        saveSeatInfo.write("[V]");
                        saveSeatInfo.newLine();
                    }
                }
            }

            saveSeatInfo.flush();
            saveSeatInfo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void restoreSeats() {
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

    /*
     * private static void writeTicket(ArrayList<Ticket> tickets) {
     * try {
     * ObjectOutputStream bw = new ObjectOutputStream(new
     * FileOutputStream("ticketFile.txt"));
     * bw.writeObject(tickets);
     * } catch (Exception e) {
     * }
     * }
     */
    public static ArrayList<Ticket> readTicketFromFile(String file) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        ArrayList<Ticket> ticketList = (ArrayList<Ticket>) ois.readObject();
        ois.close();
        return ticketList;
    }

    public static ArrayList<Movie> readMoviesFromFile(String filePath) throws FileNotFoundException {
        ArrayList<Movie> movies = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filePath));
        int x = 0;
        while (scanner.hasNextLine() && x < 1) {
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
                x++; // list.
            }

        }

        scanner.close(); // closes the scanner
        return movies;
    }
    /*
     * private void seatSelect(int row, int col) {
     * JOptionPane.showMessageDialog(this,
     * "You have selected Seat " + (row * 4 + col + 1) + ". Price: $" +
     * selectedPrice);
     * }
     * 
     * 
     * 
     * private void confirmBook() {
     * JOptionPane.showMessageDialog(this, "Booking confirmed!");
     * }
     * 
     * 
     * 
     * private void cancelBook() {
     * int choice = JOptionPane.showConfirmDialog(this,
     * "Are you sure you want to cancel the booking?",
     * "Cancel Booking", JOptionPane.YES_NO_OPTION);
     * if (choice == JOptionPane.YES_OPTION) {
     * JOptionPane.showMessageDialog(this, "Booking canceled!");
     * }
     * }
     */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MovieTheatreGUI();
            }
        });
    }
}
