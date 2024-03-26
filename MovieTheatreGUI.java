import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MovieTheatreGUI extends JFrame{
    private JButton[] movieButtons;
    private JButton[] ageButtons;
    private JButton[][] seats;
    private JLabel priceDisplay;
    private JButton confirmButton;
    private JButton cancelButton;
    private String[] movies = {"Movie 1", "Movie 2", "Movie 3"};
    private String[] ages = {"Senior", "Child", "Adult"};
    private double[][] prices = 
           {{10.0, 8.0, 12.0},
            {9.0, 7.0, 11.0},
            {11.0, 9.0, 13.0}};
    private double selectedPrice = 0.0;
    private int selectedMovieIndex = -1;
    private int selectedAgeIndex = -1;
    private String seatFile = "stateOfSeats.txt";

    public MovieTheatreGUI(){
        setTitle("Movie Theatre");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
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
        for (int i = 0; i < ages.length; i++){
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
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 4; j++){
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
    }

    private void selectMovie(int index){
        selectedMovieIndex = index;
        updatePrice();
    }

    private void selectAge(int index){
        selectedAgeIndex = index;
        updatePrice();
    }

    private void updatePrice(){
        if (selectedMovieIndex != -1 && selectedAgeIndex != -1){
            selectedPrice = prices[selectedMovieIndex][selectedAgeIndex];
            priceDisplay.setText("Price: $" + selectedPrice);
        }
    }

    private void seatSelected(int row, int col){
        seats[row][col].setBackground(Color.RED);
        JOptionPane.showMessageDialog(this, "You have selected Seat " + (row * 4 + col + 1) + ". Price: $" + selectedPrice);
    }

    private void confirmBooking(){
        JOptionPane.showMessageDialog(this, "Booking confirmed!");
    }

    private void cancelBooking(){
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel the booking?", "Cancel Booking", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Booking canceled!");
        }
    }
    private void seatSelected(int row, int col) {
        JOptionPane.showMessageDialog(this,
                "You have selected Seat " + (row * 4 + col + 1) + ". Price: $" + selectedPrice);
    }

    private void confirmBooking() {
        JOptionPane.showMessageDialog(this, "Booking confirmed!");
    }

    private void cancelBooking() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel the booking?",
                "Cancel Booking", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Booking canceled!");
        }
    }

    private void saveSeats() {
        try {
            BufferedWriter saveSeatInfo = new BufferedWriter(new FileWriter(seatFile));
            for (JButton b : seats) {
                if (b.getBackground().equals(Color.RED)) {
                    saveSeatInfo.write("[O]");
                    saveSeatInfo.write("\n");
                }

                else {
                    saveSeatInfo.write("[V]");
                    saveSeatInfo.write("\n");
                }
            }

            saveSeatInfo.flush();
            saveSeatInfo.close();
        } catch (Exception e) {

        }
    }

    private void restoreSeats() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(seatFile));
            String line;
            int i = 0;
            while ((line = br.readLine()) != null && i < seats.length) {

                if (line.equals("[O]")) {
                    seats[i].setBackground(Color.RED);
                }
                if (line.equals("[V]")) {
                    seats[i].setBackground(null);
                }
                i++;
            }
            br.close();

        } catch (Exception e) {

        }
    }

    private static void writeTicket(ArrayList<Ticket> tickets) {
        try {
            ObjectOutputStream bw = new ObjectOutputStream(new FileOutputStream("ticketFile.txt"));
            bw.writeObject(tickets);
        } catch (Exception e) {
        }
    }

    public static ArrayList<Ticket> readTicketFromFile(String file) throws IOException, ClassNotFoundException {
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

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){ 
                new MovieTheatreGUI();}});
    }
}
