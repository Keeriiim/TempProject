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

public class BookClient {
    private String baseUrl;
    private HttpClient httpClient;
    private String authToken;
    private Scanner scan;

    public BookClient(String baseUrl) {
        this.baseUrl = baseUrl + "/books";
        this.httpClient = HttpClient.newHttpClient();
        this.scan = new Scanner(System.in);
    }

    public void run(String token) throws IOException, InterruptedException {
        this.authToken = token;
        Boolean program_is_running = true;

        while(program_is_running) {
            System.out.println("\n ** Bok Meny **");
            System.out.println("1. Se alla böcker (noAuth)");
            System.out.println("2. Hitta en bok utifrån id (noAuth)");
            System.out.println("3. Lägg till en bok (Auth)");
            System.out.println("4. Uppdatera en bok (Auth)");
            System.out.println("5. Ta bort en bok (Auth)");
            System.out.println("6. Tillbaka");
            System.out.print("Val: ");
            String choice = scan.nextLine();


            switch (choice) {
                case "1":
                    String booksJson = getAllBooks(baseUrl);
                    printJson(booksJson);
                    break;
                case "2":
                    System.out.print("BookId: ");
                    String bookId = scan.nextLine();
                    String bookJson = findBookById(bookId);
                    printJson(bookJson);
                    break;
                case "3":
                    System.out.print("Title of the book: ");
                    String title = scan.nextLine();
                    System.out.print("Genre of the book: ");
                    String genre = scan.nextLine();
                    System.out.println(title + " " + genre);
                    addBook(baseUrl, authToken, title, genre);
                    break;

                case "4":
                    System.out.print("BookId: ");
                    String updateId = scan.nextLine();
                    System.out.println(findBookById(updateId));
                    System.out.print("New title: ");
                    String updatedTitle = scan.nextLine();
                    System.out.print("New genre ");
                    String updatedGenre = scan.nextLine();
                    System.out.println(updatedTitle + " " + updatedGenre);

                    String updatedBookJson = updateBook(authToken,updateId,updatedTitle,updatedGenre);
                    printJson(updatedBookJson);

                    break;
                case "5":
                    System.out.print("BookId: ");
                    String requestToDeleteId = scan.nextLine();
                    deleteBook(authToken,requestToDeleteId);

                    break;
                case "6":
                    program_is_running = false;
                    break;
                default:
                    System.out.println("Ogiltigt val. Försök igen.");
            }

        }
    }

    private String getAllBooks(String baseUrl) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl)) // baseUrl + "/authors"
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(handleResponse(response));
        return response.body();
    }

    private String findBookById(String bookId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + bookId)) // baseUrl + "/authors"
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(handleResponse(response));
        return response.body();
    }

    private String addBook(String baseUrl, String authToken, String title, String genre) throws IOException, InterruptedException {
        String json = "{\"title\": \"" + title + "\", \"genre\": \"" + genre + "\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(handleResponse(response));
        return response.body();
    }

    private String updateBook(String authToken, String updateId, String title, String genre ) throws IOException, InterruptedException {
        String json = "{\"title\": \"" + title + "\", \"genre\": \"" + genre + "\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + updateId))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(handleResponse(response));
        return response.body();
    }

    private String deleteBook(String authToken, String requestToDeleteId) throws IOException, InterruptedException {
        if (findBookById(requestToDeleteId).isEmpty()) {
            System.out.println("Book with id: " + requestToDeleteId + " does not exist");
            return "";
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + requestToDeleteId))
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
                        int id = bookObj.getInt("id");
                        String title = bookObj.getString("title");
                        String genre = bookObj.getString("genre");

                        System.out.println("\nBook " + id + ":");
                        System.out.println("  Title: " + title);
                        System.out.println("  Genre: " + genre);
                    }

                } else {
                    // Handle case where there is only one author
                    JSONObject bookObj = new JSONObject(bookJson);
                    int id = bookObj.getInt("id");
                    String name = bookObj.getString("name");
                    JSONArray booksArray = bookObj.getJSONArray("books");

                    System.out.println("\nAuthor " + id + ":");
                    System.out.println("  Name: " + name);

                    if (booksArray.length() > 0) {
                        System.out.println("  Books:");

                        for (int j = 0; j < booksArray.length(); j++) {
                            JSONObject book = booksArray.getJSONObject(j);
                            int bookId = book.getInt("id");
                            String bookName = book.getString("name");
                            System.out.println("    Book ID: " + bookId);
                            System.out.println("    Book Name: " + bookName);
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
        else if(response.statusCode() == 404){
            return "404: Not found";
        }
        else {
            return response.statusCode() + ": Error";
        }
    }
}
