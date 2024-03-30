import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

public class ReadFileTest {

    @Test
    public void testReadMoviesFromFile() {
        try {
            List<Movie> movies = ReadFile.readMoviesFromFile("movies.txt");

            assertNotNull(movies);
            assertEquals(2, movies.size());

            Movie movie1 = movies.get(0);
            assertEquals("Movie1", movie1.getTitle());
            assertEquals("120 mins", movie1.getDuration());
            assertEquals("7:00 PM", movie1.getAirtime());
            assertEquals(4.5, movie1.getRating(), 0.001);

            Movie movie2 = movies.get(1);
            assertEquals("Movie2", movie2.getTitle());
            assertEquals("90 mins", movie2.getDuration());
            assertEquals("9:00 PM", movie2.getAirtime());
            assertEquals(3.8, movie2.getRating(), 0.001);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}