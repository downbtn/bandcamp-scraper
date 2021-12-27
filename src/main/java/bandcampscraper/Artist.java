package bandcampscraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.io.IOException;
import java.lang.InterruptedException;

import java.util.List;
import java.util.ArrayList;

import bandcampscraper.Album;

class Artist {
    String name;
    String location;
    String URL;
    List<Album> albums;

    public Artist(String name, String location, String URL, List<Album> albums) {
        this.name = name;
        this.location = location;
        this.URL = URL;
        this.albums = albums;
    }

    public static Artist scrapeFromURL(String URL) throws Exception {
        // Send the HTTP request and get contents of the artist page
        // TODO *DOES NOT WORK FOR NON GRID LAYOUT PAGES*
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        if (statusCode != 200) {
            System.out.println("Error code " + statusCode);
            throw new Exception("Request to artist url " + URL + " failed! (" + statusCode + ")");
        }

        Document doc = Jsoup.parse(response.body());

        // First let's get the artist name and location
        Element nameLocation = doc.getElementById("band-name-location");
        String name = nameLocation.child(0).text();
        String location = nameLocation.child(1).text();

        // select all li inside the grid element ol#music-grid - ALBUMS
        Elements albumElements = doc.select("ol#music-grid li.music-grid-item");
        ArrayList<Album> albums = new ArrayList<Album>(albumElements.size());

        // loop through albums and scrape link to the album list
        for (Element albumElement : albumElements) {
            String link = albumElement.select("a").first().attr("href");
            try {
                albums.add(Album.scrapeFromURL(link));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new Artist(name, location, URL, albums);
    }
}
