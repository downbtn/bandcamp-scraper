import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal; 

import java.io.IOException;
import java.lang.InterruptedException;

import bandcampscraper.Album;

import static org.junit.jupiter.api.Assertions.*;
 
public class AlbumTest {
    @Test
    @DisplayName("Test scraping Name Your Price album")
    public void testScrapeNYP() {
        Album album;
        try {
            album = Album.scrapeFromURL("https://s-mamomo.bandcamp.com/album/hometown");
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("looks like the test failed didnt it"); // i dont know how to do errors in java
        }
        assertEquals(album.getTitle(), "Hometown");
        assertEquals(album.getArtistName(), "mamomo");
        assertEquals(album.getURL(), "https://s-mamomo.bandcamp.com/album/hometown");
        assertEquals(album.getPriceType(), "Name Your Price");
        assertEquals(album.getPriceAmount(), new BigDecimal(0));
    }
    
    @Test
    @DisplayName("Test scraping free download album")
    public void testScrapeFree() {
        String URL = "https://phritz.bandcamp.com/track/porter-robinson-look-at-the-sky-phritz-cover";
        Album album;
        try {
            album = Album.scrapeFromURL(URL);
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("looks like the test failed didnt it"); // i dont know how to do errors in java
        }

        assertEquals(album.getTitle(), "Porter Robinson - Look at the Sky (phritz cover)");
        assertEquals(album.getArtistName(), "phritz");
        assertEquals(album.getURL(), "https://phritz.bandcamp.com/track/porter-robinson-look-at-the-sky-phritz-cover");
        assertEquals(album.getPriceType(), "Free Download");
        assertEquals(album.getPriceAmount(), new BigDecimal(0));
    }

    @Test
    @DisplayName("Test scraping USD album")
    public void testScrapeUSD() {
        String URL = "https://mididuck.bandcamp.com/track/drowsy";
        Album album;
        try {
            album = Album.scrapeFromURL(URL);
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("looks like the test failed didnt it"); // i dont know how to do errors in java
        }
        BigDecimal correctPrice = new BigDecimal(0.50);
        assertEquals(album.getTitle(), "drowsy");
        assertEquals(album.getArtistName(), "mididuck");
        assertEquals(album.getURL(), URL);
        assertEquals(album.getPriceType(), "USD");
        assertEquals(correctPrice.compareTo(album.getPriceAmount()), 0); // compares value, so 0.5==0.50
    }
}
