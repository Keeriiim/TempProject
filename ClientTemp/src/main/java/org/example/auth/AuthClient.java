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
        String url = baseurl + "/auth/login"; // The endpoint to send the POST request to
        String json = objectMapper.writeValueAsString(Map.of("username", username, "password", password)); // Creating the JSON payload

        HttpRequest request = HttpRequest.newBuilder() // Building the request
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(handleResponse(response));
        return response.body();
    }

    public String register(String username, String password) throws IOException, InterruptedException {
        String url = baseurl + "/auth/register";
        String json = objectMapper.writeValueAsString(Map.of("username", username, "password", password)); // Creating the JSON payload

        HttpRequest request = HttpRequest.newBuilder() // Building the request
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(handleResponse(response));
        return response.body();
    }
    private String handleResponse(HttpResponse<String> response) {
        if (response.statusCode() == 401) {
            return "401: felaktiga behörigheter";
        }
        else if(response.statusCode() == 200){
            return "200: Success";
        }
        else if(response.statusCode() == 404){
            return "404: Not found";
        }
        else {
            return response.statusCode() + ": Error";
        }
    }
}
