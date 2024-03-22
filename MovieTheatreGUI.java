import javax.swing.*;
import java.awt.*;

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

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){ 
                new MovieTheatreGUI();}});
    }
}