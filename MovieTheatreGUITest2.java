//Tests

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MovieTheatreGUITest {

    @Mock
    private JFrame frame;

    private MovieTheatreGUI movieTheatreGUI;

    @Before
    public void setUp() {
        movieTheatreGUI = new MovieTheatreGUI();
        movieTheatreGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Test
    public void testSelectMovie() {
        // Mock selected movie index
        movieTheatreGUI.selectedMovieIndex = 0;

        // Test selectMovie method
        movieTheatreGUI.selectMovie(0);

        // Verify that the current frame is disposed
        verify(frame).dispose();
        // Verify that the MovieOne window is shown
        assertTrue(movieTheatreGUI.isVisible());
    }
}