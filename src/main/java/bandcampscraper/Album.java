package bandcampscraper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.math.BigDecimal;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class Album {
    String title;
    String URL;
    String artistName;
    int priceAmount;
    String priceType;

    public Album (String title, String URL, String artistName, int priceAmount, String priceType) {
        this.title = title;
        this.URL = URL;
        this.artistName = artistName;
        this.priceAmount = priceAmount;
        this.priceType = priceType;
    }

    public static Album scrapeFromURL (String URL) throws Exception {
        // need:
        // title done
        // URL given
        // artist name done
        // price
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

        // title and artist name
        Element titleSection = doc.getElementById("name-section"); // div containing title and artist name
        String title = titleSection.getElementsByClass("trackTitle").first().text();
        String artistName = titleSection.select("h3 span a[href]").text();

        // price
        // count cents for everything but JPY!!
        Element buyDiv = doc.select("li.buyItem div.ft").first();
        // unfinished
    }
}
