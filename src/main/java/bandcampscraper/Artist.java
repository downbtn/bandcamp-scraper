package bandcampscraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.URL;
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
    String artistURL;
    List<Album> albums;

    public Artist(String name, String location, String artistURL, List<Album> albums) {
        this.name = name;
        this.location = location;
        this.artistURL = artistURL;
        this.albums = albums;
    }

    public String getName() {
        return this.name;
    }

    public String getLocation() {
        return this.location;
    }

    public String getURL() {
        return this.artistURL;
    }

    public List<Album> getAlbums() {
        return this.albums;
    }

    public static Artist scrapeFromURL(String artistURL) throws IOException,InterruptedException {
        // Send the HTTP request and get contents of the artist page
        String albumsURL = (new URL(new URL(artistURL), "/music")).toString();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(albumsURL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        if (statusCode != 200) {
            System.out.println("Error code " + statusCode);
            throw new RuntimeException("Request to artist albums url " + albumsURL + " failed! (" + statusCode + ")");
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
            String relPath = albumElement.select("a").first().attr("href"); // gives us relative path of album
            String absURL = (new URL(new URL(artistURL), relPath)).toString(); // concatenate it to the given URL
            try {
                albums.add(Album.scrapeFromURL(absURL));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new Artist(name, location, artistURL, albums);
    }
}
