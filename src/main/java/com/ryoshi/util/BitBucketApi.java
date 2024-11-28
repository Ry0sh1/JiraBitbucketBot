package com.ryoshi.util;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class BitBucketApi {

    private static final String jiraUsername = System.getenv("USERNAME");
    private static final String jiraPassword = System.getenv("PASSWORD");

    private static final String auth = jiraUsername + ":" + jiraPassword;
    private static final byte[] encodedBytes = Base64.getEncoder().encode(auth.getBytes());

    public static String getCurrentPullRequests() {
        String httpsURL = "https://git.swl.informatik.uni-oldenburg.de//rest/api/latest/projects/SWP2024/repos/SWP2024f/pull-requests";

        try {
            URL url = new URL(httpsURL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", "Basic " + new String(encodedBytes));
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            inputLine = br.readLine();

            br.close();

            return inputLine;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPastPullRequests() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://git.swl.informatik.uni-oldenburg.de//rest/api/latest/projects/SWP2024/repos/SWP2024f/pull-requests?state=merged"))
                    .version(HttpClient.Version.HTTP_2)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Authorization", "Basic " + new String(encodedBytes))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
