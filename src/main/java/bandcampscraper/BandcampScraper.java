package bandcampscraper;

import java.util.Scanner;
import java.lang.InterruptedException;
import java.io.IOException;
import java.util.List;

import bandcampscraper.Artist;
import bandcampscraper.Album;

public class BandcampScraper {
    public static void main (String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Artist URL to scrape?");
        String artistURL = scan.nextLine();

        Artist artist;
        try {
            artist = Artist.scrapeFromURL(artistURL);
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("error");
        }

        // print the thing nicely
        // maybe put this in overridden toString later?
        System.out.println("=".repeat(79));
        System.out.println("Artist Name: " + artist.getName());
        System.out.println("Location: " + artist.getLocation());
        System.out.println("URL: " + artist.getURL());
        System.out.println("=".repeat(79));
        System.out.println();

        List<Album> albums = artist.getAlbums();
        for (int i = 0; i < albums.size(); i++) {
            Album currentAlbum = albums.get(i);
            System.out.println("=".repeat(79));

            System.out.println("Album Title: " + currentAlbum.getTitle());
            System.out.println("By " + currentAlbum.getArtistName());

            if (currentAlbum.getPriceType() == "Name Your Price") {
                System.out.println("Price: Name Your Price");
            } else if (currentAlbum.getPriceType() == "Free Download") {
                System.out.println("Price: Free Download");
            } else {
                System.out.println("Price: " + currentAlbum.getPriceAmount() + " " + currentAlbum.getPriceType());
            }

            System.out.println("Album URL: " + currentAlbum.getURL());

            System.out.println("=".repeat(79));
            System.out.println();
        }
    }
}
