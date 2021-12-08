package bandcampscraper;

import bandcampscraper.Artist;

public class BandcampScraper {
    public static void main (String[] args) {
        String artistURL = "https://s-mamomo.bandcamp.com/"; /* Sample artist page for testing purposes
                                                             it has a "grid" layout displaying all albums */
        try {
            Artist mamomo = Artist.scrapeFromURL(artistURL);
        } catch (Exception e) {
            // For some reason the standard library HttpClient requires us to
            // catch exceptions. TODO: find a cleaner way to do this
            System.out.println("oh noes *dies*");
            System.exit(2);
        }
    }
}
