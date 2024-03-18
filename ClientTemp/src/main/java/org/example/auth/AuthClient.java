package org.example.auth;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class AuthClient {
    private final String baseurl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AuthClient(String baseurl) { // Initializing the AuthClient with the baseurl, HttpClient and ObjectMapper
        this.baseurl = baseurl;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public String login(String username, String password) throws IOException, InterruptedException {
        // This method sends a POST request to the server to login an user
        // The server will respond with a JWT token if the login is successful
        // The token is then returned to the caller

        String url = baseurl + "/auth/login"; // The endpoint to send the POST request to
        String json = objectMapper.writeValueAsString(Map.of("username", username, "password", password)); // Creating the JSON payload

        HttpRequest request = HttpRequest.newBuilder() // Building the request
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        handleResponse(response);
        return response.body();
    }

    public String register(String username, String password) throws IOException, InterruptedException {
        // This method sends a POST request to the server to register an user
        // The server will respond with a JWT token if the registration is successful
        // The token is then returned to the caller

        String url = baseurl + "/auth/register";
        String json = objectMapper.writeValueAsString(Map.of("username", username, "password", password)); // Creating the JSON payload

        HttpRequest request = HttpRequest.newBuilder() // Building the request
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        handleResponse(response);
        return response.body();
    }

    private void handleResponse(HttpResponse<String> response) {
        if (response.statusCode() >= 400) {
            System.out.println("Error: " + response.statusCode());
        }
    }


}
