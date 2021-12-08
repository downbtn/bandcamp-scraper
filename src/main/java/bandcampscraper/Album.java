package bandcampscraper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class Album {
    String title;
    String URL;
    Artist artist;
    int priceAmount;
    PriceType priceType;

    public Album (String title, String URL, Artist artist, int priceAmount, PriceType priceType) {
        this.title = title;
        this.URL = URL;
        this.artist = artist;
        this.priceAmount = priceAmount;
        this.priceType = priceType;
    }

    public static Album scrapeFromURL throws Exception (String URL) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .build();
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();
        if (statusCode != 200) {
            System.out.println("Error code " + statusCode);
            throw new Exception("Request to artist url " + URL + " failed! (" + statusCode + ")");
        }
        Document doc = Jsoup.parse(response.body());
    }
}