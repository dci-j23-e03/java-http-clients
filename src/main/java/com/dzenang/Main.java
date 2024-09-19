package com.dzenang;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {
//        getExample();
//        postExample();
        getExampleHttpClient();
    }

    public static void getExampleHttpClient() {
        try(HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create("https://catfact.ninja/fact"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Got response code: " + response.statusCode());
            System.out.println("Got response: \n" + response.body());
            System.out.println("Got headers: " + response.headers());
        }  catch (IOException | InterruptedException e) {
            System.out.println("Problem occurred with request: " + e.getMessage());
        }
    }

    public static void getExample() {
        HttpURLConnection connection = prepareConnection("https://catfact.ninja/fact");
        try {
            assert connection != null;
            printResponse(connection);
        } catch (IOException e) {
            System.out.println("Error executing request: " + e.getMessage());
        }
    }

    public static void postExample() {
        HttpURLConnection httpURLConnection = prepareConnection("https://postman-echo.com/post");
        try {
            assert httpURLConnection != null;
            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setRequestProperty("Content-Type", "text/plain");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write("Hello World".getBytes());
            printResponse(httpURLConnection);
        } catch (IOException e) {
            System.out.println("Error executing request: " + e.getMessage());
        }
    }

    private static void printResponse(HttpURLConnection httpURLConnection) throws IOException {
        int responseCode = httpURLConnection.getResponseCode();
        System.out.println("Request executed with code " + responseCode);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = bufferedReader.readLine()) != null) {
            content.append(inputLine);
        }
        System.out.println("Response: \n" + content);
    }

    private static HttpURLConnection prepareConnection(String urlString) {
        try {
            URL url = URI.create(urlString).toURL();
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Opening connection failed: " + e.getMessage());
        }
        return null;
    }
}