
//Tests
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TicketTest {

    @Test
    public void testToString() {
        Movie movie = mock(Movie.class);
        TheatreRoom room = mock(TheatreRoom.class);

        when(movie.getName()).thenReturn("MovieName");
        when(movie.getDuration()).thenReturn(120);
        when(movie.getRating()).thenReturn("PG");
        when(movie.getAiringTime()).thenReturn("12:00");

        when(room.getRoomNum()).thenReturn(1);

        Ticket ticket = new Ticket(movie, room, 1, 1, 10.0);

        String expected = "Ticket{" +
                "name='MovieName'" +
                ", duration=120" +
                ", rating=PG" +
                ", airingTime='12:00'" +
                ", Theatre Room Number: 1" +
                ", Seat Row: 1" +
                ", Seat Column: 1" +
                ", Price: 10.0" +
                "}\n";

        assertEquals(expected, ticket.toString());
    }
}