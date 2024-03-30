import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TheatreRoomTest {

    @Test
    public void testToString() {
        Movie movie = mock(Movie.class);
        when(movie.getName()).thenReturn("MovieName");
        when(movie.getDuration()).thenReturn(120);
        when(movie.getRating()).thenReturn("PG");
        when(movie.getAiringTime()).thenReturn("12:00");

        TheatreRoom theatreRoom = new TheatreRoom(movie, 1);

        String expected = ", Theatre Room Number: 1}";

        assertEquals(expected, theatreRoom.toString());
    }
}