package com.axreng.backend.utility;

import org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

public class HtmlRetriever {

    private final HttpClient client;

    public HtmlRetriever() {
        this.client = HttpClient.newHttpClient();
    }

    public String retrieve(String url) {
        try {
            return tryToRetrieve(url);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            return null;
        }
    }

    private String tryToRetrieve(String url) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .timeout(Duration.of(100, SECONDS))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (HttpStatus.isSuccess(response.statusCode())) {
            return response.body();
        }
        return null;
    }

}
