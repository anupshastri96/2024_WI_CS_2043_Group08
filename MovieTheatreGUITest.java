import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MovieTheatreGUITest {

    @Mock
    private JFrame frame;

    @Mock
    private JTextArea bookingTextArea;

    @Mock
    private JButton[][] seats;

    private MovieTheatreGUI movieTheatreGUI;

    @Before
    public void setUp() {
        movieTheatreGUI = new MovieTheatreGUI();
        movieTheatreGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        movieTheatreGUI.setLayout(null);

        movieTheatreGUI.bookingTextArea = bookingTextArea;
        movieTheatreGUI.seats = seats;
    }

    @Test
    public void testConfirmBooking() {
        // Mock selected movie index and seats
        movieTheatreGUI.selectedMovieIndex = 0;
        when(seats.length).thenReturn(5);
        when(seats[0].length).thenReturn(5);
        when(seats[0][0].getBackground()).thenReturn(Color.RED);
        when(seats[1][1].getBackground()).thenReturn(Color.RED);
        when(seats[2][2].getBackground()).thenReturn(null);

        // Mock JOptionPane
        JOptionPane optionPane = mock(JOptionPane.class);
        when(optionPane.showConfirmDialog(frame, "Are you sure you want to cancel the booking?",
                "Cancel Booking", JOptionPane.YES_NO_OPTION)).thenReturn(JOptionPane.YES_OPTION);

        // Mock movie list and new rooms
        ArrayList<Movie> movieList = new ArrayList<>();
        movieList.add(new Movie("Movie1", "2 hours", "7:00 PM", 4.5));
        movieTheatreGUI.movieList = movieList;
        Map<Movie, TheatreRoom> newRoomsForMovies = new HashMap<>();
        newRoomsForMovies.put(movieList.get(0), new TheatreRoom(movieList.get(0), 1));
        movieTheatreGUI.newRoomsForMovies = newRoomsForMovies;

        // Test confirmBooking method
        movieTheatreGUI.confirmBooking();

        // Verify that the booking is confirmed and the ticket is added
        verify(bookingTextArea).setText("Booking confirmed! Price: $" + movieTheatreGUI.price);
    }
}