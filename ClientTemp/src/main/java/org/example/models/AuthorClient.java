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



public class AuthorClient {

    private String baseUrl;
    private HttpClient httpClient;
    private String authToken;
    private Scanner scan;

    public AuthorClient(String baseUrl) {
        this.baseUrl = baseUrl + "/authors";
        this.httpClient = HttpClient.newHttpClient();
        this.scan = new Scanner(System.in);
    }


    public void run(String token) throws IOException, InterruptedException {
        this.authToken = token;
        Boolean program_is_running = true;

        while (program_is_running) {
            System.out.println("\n ** Författar Meny **");
            System.out.println("1. Se alla författare (noAuth)");
            System.out.println("2. Hitta en författare utifrån id (noAuth)");
            System.out.println("3. Lägg till bok hos författare");
            System.out.println("4. Lägg till författare");
            System.out.println("5. Uppdatera författare");
            System.out.println("6. Ta bort författare");
            System.out.println("7. Tillbaka");
            System.out.print("Val: ");
            String choice = scan.nextLine();
            switch (choice) {
                case "1":
                    String authorsJson = getAllAuthors();
                    printJson(authorsJson);
                    break;
                case "2":
                    System.out.println("AuthorId: ");
                    String authorId = scan.nextLine();
                    String author = findAuthorById(authorId);
                    System.out.println(author);
                    printJson(author);
                    break;
                case "3":
                    System.out.print("AuthorId: ");
                    String authorID = scan.nextLine();
                    System.out.print("BookId: ");
                    String bookId = scan.nextLine();
                    addBookToAuthor(authToken, authorID, bookId);
                    break;
                case "4":
                    System.out.print("Author name: ");
                    String authorName = scan.nextLine();
                    createAuthor(authToken, authorName);
                    break;
                case "5":
                    System.out.print("AuthorId: ");
                    String authorIdToUpdate = scan.nextLine();
                    System.out.print("New author name: ");
                    String newAuthorName = scan.nextLine();
                    updateAuthor(authToken, authorIdToUpdate, newAuthorName);
                    break;
                case "6":
                    System.out.print("AuthorId: ");
                    String authorIdToDelete = scan.nextLine();
                    deleteAuthor(authToken, authorIdToDelete);
                    break;
                case "7":
                    program_is_running = false;
                    break;
                default:
                    System.out.println("Ogiltigt val. Försök igen.");
            }
        }
    }

    private void addBookToAuthor(String authToken, String authorID, String bookId) {
        String json = "{\"authorId\": " + authorID + ", \"bookId\": " + bookId + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + authorID + "/book/" + bookId))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(handleResponse(response));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


/*
    private AuthorDTO dtoHandler(String authorIdToUpdate) throws IOException, InterruptedException {
        String authorJson = findAuthorById(authorIdToUpdate);

        if (!authorJson.isEmpty()) {

            // Get the author with all details in json code
            // Extract the id, name and list of books from the json code
            try {
                JSONObject authorObj = new JSONObject(authorJson);
                Integer id = authorObj.getInt("id");
                String name = authorObj.getString("name");
                JSONArray booksArray = authorObj.getJSONArray("books");

                List<BookDTO> bookDTOList = new ArrayList<>();
                for (int i = 0; i < booksArray.length(); i++) {
                    JSONObject bookObj = booksArray.getJSONObject(i);
                    Integer bookId = bookObj.getInt("id");
                    String bookTitle = bookObj.getString("title");
                    String bookGenre = bookObj.getString("genre");
                    bookDTOList.add(new BookDTO(bookId, bookTitle, bookGenre));
                }
                // Create and return the AuthorDTO object
                return new AuthorDTO(id, name, bookDTOList);
            } catch (JSONException e) {
                System.out.println("Error parsing JSON: " + e.getMessage());
                e.printStackTrace();
            }
            ;
        }

        System.out.println("Author with id: " + authorIdToUpdate + " does not exist");
        return new AuthorDTO();

    }

 */



    public String getAllAuthors() throws IOException, InterruptedException {
        // This method sends a GET request to the server to get all authors
        // The server will respond with a list of authors
        // The list is then returned to the caller

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl)) // baseUrl + "/authors"
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(handleResponse(response));
        return response.body();
    }

    private String findAuthorById(String authorId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + authorId)) // baseUrl + "/authors"
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(handleResponse(response));
        return response.body();
    }

    public String createAuthor(String authToken, String authorName) throws IOException, InterruptedException {
        // This method sends a POST request to the server to create a new author
        // The server will respond with the created author
        // The author is then returned to the caller

        String json = "{\"name\": \"" + authorName + "\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl ))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        handleResponse(response);
        return response.body();
    }

    private String updateAuthor(String authToken, String authorIdToUpdate, String newAuthorName) throws IOException, InterruptedException {
        String json = "{\"name\": \"" + newAuthorName + "\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + authorIdToUpdate))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(handleResponse(response));
        return response.body();
    }
    private String deleteAuthor(String authToken, String requestToDeleteId) throws IOException, InterruptedException {

        if (findAuthorById(requestToDeleteId).isEmpty()) {
            System.out.println("Author with id: " + requestToDeleteId + " does not exist");
            return "";
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + requestToDeleteId))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        handleResponse(response);
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
                        String name = bookObj.getString("name");
                        JSONArray booksArray = bookObj.getJSONArray("books");

                        System.out.println("\nAuthor " + id + ":");
                        System.out.println("  Name: " + name);

                        if (booksArray.length() > 0) {
                            System.out.println("  Books:");

                            for (int j = 0; j < booksArray.length(); j++) {
                                JSONObject book = booksArray.getJSONObject(j);
                                int bookId = book.getInt("id");
                                String bookTitle = book.getString("title");
                                String bookGenre = book.getString("genre");
                                System.out.println("    Book ID: " + bookId);
                                System.out.println("    Book Name: " + bookTitle);
                                System.out.println("    Book Genre: " + bookGenre + "\n");
                            }
                        } else {
                            System.out.println("  No books available.");
                        }
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
                            String bookTitle = book.getString("title");
                            String bookGenre = book.getString("genre");
                            System.out.println("    Book ID: " + bookId);
                            System.out.println("    Book Name: " + bookTitle);
                            System.out.println("    Book Genre: " + bookGenre + "\n");
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
