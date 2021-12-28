package bandcampscraper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.lang.Number;

import java.io.IOException;
import java.lang.InterruptedException;
import java.lang.RuntimeException;
import java.text.ParseException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Album {
    String title;
    String URL;
    String artistName;
    BigDecimal priceAmount;
    String priceType;

    public Album (String title, String URL, String artistName, BigDecimal priceAmount, String priceType) {
        this.title = title;
        this.URL = URL;
        this.artistName = artistName;
        this.priceAmount = priceAmount;
        this.priceType = priceType;
    }

    public String getTitle() {
        return this.title;
    }

    public String getURL() {
        return this.URL;
    }

    public String getArtistName() {
        return this.artistName;
    }

    public BigDecimal getPriceAmount() {
        return this.priceAmount;
    }

    public String getPriceType() {
        return this.priceType;
    }

    public static Album scrapeFromURL(String URL) throws IOException, InterruptedException {
        // make HTTP request and put resultng document into Jsoup
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();
        if (statusCode != 200) {
            System.out.println("Error code " + statusCode);
            throw new RuntimeException("Request to artist url " + URL + " had an error! (" + statusCode + ")");
        }
        Document doc = Jsoup.parse(response.body());

        // title and artist name
        Element titleSection = doc.getElementById("name-section"); // div containing title and artist name
        String title = titleSection.getElementsByClass("trackTitle").first().text();
        String artistName = titleSection.select("h3 span a[href]").text();

        // price
        Element buyH4 = doc.select("li.buyItem div.ft h4.ft").first();
        System.out.println("buyH4.text() == <<"+buyH4.text()+">>"); // debug

        String priceType;
        BigDecimal priceAmount;
        if (buyH4.text().equals("Free Download")) {
            // if the album is freedl it will only say "Free Download"
            priceType = "Free Download";
            priceAmount = new BigDecimal(0);
        } else {
            // the 2nd child element will be a span containing price info
            Element span = buyH4.child(1);
            System.out.println("span text: <<" + span.text() + ">> - check to nyp");
            if (span.text().equals("name your price")) {
                System.out.println("NYP yes");
                priceType = "Name Your Price";
                priceAmount = new BigDecimal(0);
            } else {
                String amtStr = span.child(0).text().substring(1); // substring to remove currency symbol

                Number amtNumber;
                try {
                    amtNumber = NumberFormat.getInstance().parse(amtStr);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                    throw new RuntimeException("Failed parsing number " + amtStr);
                }

                priceAmount = new BigDecimal(amtNumber.toString());
                priceType = span.child(1).text().trim();
            }
        }

        System.out.println("priceType = <<"+priceType+">>");
        System.out.println("priceAmount = <<"+priceAmount+">>");
        return new Album(title, URL, artistName, priceAmount, priceType);
    }
}
