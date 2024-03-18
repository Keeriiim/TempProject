package org.example.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class UserClient {

    private String baseUrl;
    private HttpClient httpClient;
    private String authToken;
    private Scanner scan;

    public UserClient(String baseUrl){
        this.baseUrl = baseUrl + "/admin/users";
        this.httpClient = HttpClient.newHttpClient();
        this.scan = new Scanner(System.in);
    }

    public void run(String token) throws IOException, InterruptedException {
        this.authToken = token;
        Boolean program_is_running = true;

        while(program_is_running) {
            System.out.println("\n **Administrera användare Meny**");
            System.out.println("1. Se alla användare");
            System.out.println("2. Hitta användare genom id");
            System.out.println("3. Ta bort en användare");
            System.out.println("4. Uppgradera en användares behörigheter");
            System.out.println("5. Nedgradera en användares behörigheter");
            System.out.println("6. Tillbaka");
            System.out.print("Val: ");
            String choice = scan.nextLine();
            switch (choice){
                case "1":
                    String json = findAllUsers(authToken);
                    printJson(json);
                    break;
                case "2":
                    System.out.print("AnvändarID: ");
                    String userId = scan.nextLine();
                    String userJson = findUserById(authToken, userId);
                    printJson(userJson);
                    break;
                case "3":
                    System.out.print("AnvändarID: ");
                    String deleteId = scan.nextLine();
                    deleteUser(authToken,deleteId);
                    System.out.println("Ta bort användare");
                    break;
                case "4":
                    System.out.print("AnvändarID: ");
                    String updateId = scan.nextLine();
                    updateUserRole(authToken,updateId);
                    break;
                case "5":
                    System.out.print("AnvändarID: ");
                    String removeId = scan.nextLine();
                    removeRole(authToken, removeId);
                    break;
                case "6":
                    program_is_running = false;
                    break;
                default:
                    System.out.println("Felaktigt val, försök igen");
                    break;
            }
        }
    }

    private String findAllUsers(String authToken) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(handleResponse(response));
        return response.body();
    }

    private String findUserById(String authToken, String userId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + userId))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(handleResponse(response));
        return response.body();
    }

    private String deleteUser(String authToken, String userId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + userId))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(handleResponse(response));
        return response.body();
    }

    private String updateUserRole(String authToken, String updateId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roles/" + updateId))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(handleResponse(response));
        return response.body();
    }

    private String removeRole(String authToken, String removeId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roles/" + removeId))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(handleResponse(response));
        return response.body();
    }

    private void printJson(String bookJson) {
        if (!bookJson.isEmpty()) {
            try {
                // Check if the JSON starts with an array character '['
                if (bookJson.startsWith("[")) {
                    JSONArray jsonArray = new JSONArray(bookJson);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject bookObj = jsonArray.getJSONObject(i);
                        int id = bookObj.getInt("userId");
                        String name = bookObj.getString("username");
                        JSONArray booksArray = bookObj.getJSONArray("authorities");

                        System.out.println("\nUser " + id + ":");
                        System.out.println("  Username: " + name);

                        if (booksArray.length() > 0) {
                            System.out.println("  Authorities:");

                            for (int j = 0; j < booksArray.length(); j++) {
                                JSONObject book = booksArray.getJSONObject(j);
                                String authority = book.getString("authority");
                                System.out.println("    Role: " + authority);
                            }
                        } else {
                            System.out.println("  No books available.");
                        }
                    }
                } else {
                    // Handle case where there is only one author
                    JSONObject bookObj = new JSONObject(bookJson);
                    int id = bookObj.getInt("userId");
                    String name = bookObj.getString("username");
                    JSONArray booksArray = bookObj.getJSONArray("authorities");

                    System.out.println("\nUser " + id + ":");
                    System.out.println("  Username: " + name);

                    if (booksArray.length() > 0) {
                        System.out.println("  Authorities:");

                        for (int j = 0; j < booksArray.length(); j++) {
                            JSONObject book = booksArray.getJSONObject(j);
                            String user = bookObj.getString("username");
                            JSONArray authoritiess = bookObj.getJSONArray("authorities");
                        }
                    } else {
                        System.out.println("  No books available.");
                    }
                }
            } catch (JSONException e) {
                System.out.println("Error parsing JSON: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private String handleResponse(HttpResponse<String> response) {
        if (response.statusCode() == 401) {
            return "401: felaktiga behörigheter";
        }
        else if(response.statusCode() == 200){
            return "200: Success";
        }
        else if(response.statusCode() == 204){
            return "204: No Content (Delete successful)";
        }
        else {
            return response.statusCode() + ": Error";
        }
    }


}
