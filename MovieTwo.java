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

public class MovieTwo extends JFrame {
    private double price = 10.00;
    JLabel label;
    private JButton[][] seats;
    private JButton returnButton;
    private JButton viewButton;
    private JLabel priceDisplay;
    private JButton confirmButton;
    private ArrayList<Ticket> tickets = new ArrayList<>();
    private String seatFile = "MovieTwo.txt";
    private String ticketFile = "ticketFile.txt";
    JTextArea bookingTextArea;

    public MovieTwo() {
        setTitle("Movie Two");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setSize(500, 400);
        bookingTextArea = new JTextArea(10, 30);

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        label = new JLabel("Movie 2");
        add(topPanel, BorderLayout.NORTH);
        JPanel bottomPanel = new JPanel();
        priceDisplay = new JLabel("Price: " + price);
        confirmButton = new JButton("Confirm Booking");
        confirmButton.addActionListener(e -> confirmBooking());
        bottomPanel.add(priceDisplay);
        bottomPanel.add(confirmButton);
        viewButton = new JButton("View Booking");
        viewButton.addActionListener(e -> viewBooking());
        bottomPanel.add(viewButton);

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
        returnButton = new JButton("Return");

        returnButton.addActionListener(e -> returnToGui());

        add(seatsPanel, BorderLayout.CENTER);
        bottomPanel.add(returnButton);
        add(bottomPanel, BorderLayout.SOUTH);

        restoreSeatsForMovie1();
    }

    private ArrayList<Ticket> movieTwoTickets() {
        return tickets;
    }

    public void viewBooking() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < movieTwoTickets().size(); i++) {
            sb.append(i).append(tickets.get(i).toString()).append("\n");
        }
        bookingTextArea.setText(sb.toString());

        JOptionPane.showMessageDialog(null, sb.toString(), "Ticket List", JOptionPane.INFORMATION_MESSAGE);
        if (!movieTwoTickets().isEmpty()) {
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

    private void seatSelected(int row, int col) {
        if (seats[row][col].getBackground().equals(Color.RED)) {
            seats[row][col].setBackground(null);
            JOptionPane.showMessageDialog(this,
                    "You have deselected Seat " + (row * 4 + col + 1));
            priceDisplay.setText("Price: " + price);
        } else {
            seats[row][col].setBackground(Color.RED);
            JOptionPane.showMessageDialog(this,
                    "You have selected Seat " + (row * 4 + col + 1) + ". Price: $" + price);

        }

    }

    private void confirmBooking() {

        Movie movie = new Movie("Migration", "17:00", "1h31m", 5);
        TheatreRoom newRoom = new TheatreRoom(movie, 2);

        boolean seatSelected = false;
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j].getBackground() == Color.RED) {
                    seatSelected = true;
                    Ticket ticket = new Ticket(movie, newRoom, i, j, price);
                    tickets.add(ticket); // Create and add a Ticket for the selected seat

                    saveSeats("MovieTwo.txt");
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter("MovieTwoTickets.txt"))) {
                        bw.write(ticket.toString());

                        bw.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    seatSelected = false;
                    saveSeats("MovieTwo.txt");
                }
            }
        }

        if (seatSelected) {
            JOptionPane.showMessageDialog(this, "Booking confirmed! Price: $" + price);
            writeTicket(tickets, "ticketFile.txt");
        }

    }

    private void writeTicket(ArrayList<Ticket> tickets, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(tickets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveSeats(String file) {
        try {
            BufferedWriter saveSeatInfo = new BufferedWriter(new FileWriter(file));

            for (int i = 0; i < seats.length; i++) {
                for (int j = 0; j < seats[i].length; j++) {
                    if (Color.RED.equals(seats[i][j].getBackground())) {
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

    private void restoreSeatsForMovie1() {
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

    private void returnToGui() {
        MovieTheatreGUI gui = new MovieTheatreGUI();
        gui.show();
        dispose();

    }
}