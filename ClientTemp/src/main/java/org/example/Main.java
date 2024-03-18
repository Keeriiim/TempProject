package org.example;

import org.example.auth.AuthClient;
import org.example.models.AuthorClient;
import org.example.models.BookClient;
import org.example.models.UserClient;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Scanner;

public class Main {


    private static final Scanner scanner = new Scanner(System.in);
    private static AuthClient authClient;
    private static AuthorClient authorClient;
    private static BookClient bookClient;
    private static UserClient userClient;
    private static String active_token = "";


    public static void main(String[] args) throws IOException, InterruptedException {
      //String baseUrl = "http://localhost:8080";
        String baseUrl = "http://temp.eu-north-1.elasticbeanstalk.com";
        authClient = new AuthClient(baseUrl);
        authorClient = new AuthorClient(baseUrl);
        bookClient = new BookClient(baseUrl);
        userClient = new UserClient(baseUrl);


        while (true) {
            System.out.println("\nVälkommen till applikationen. Välj ett alternativ:");
            System.out.println("1. Logga in ");
            System.out.println("2. Registrera en användare (admin)");
            System.out.println("3. Administrera författare ");
            System.out.println("4. Administrera böcker");
            System.out.println("5. Administrera användare (admin)");
            System.out.println("6. Avsluta");

            System.out.print("Val: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    userLogin();
                    break;
                case "2":
                    registerUser();
                    break;
                case "3":
                    System.out.println("Current Token: " + active_token);
                    authorClient.run(active_token);
                    break;
                case "4":
                    System.out.println("Current Token: " + active_token);
                    bookClient.run(active_token);
                    break;
                case "5":
                    System.out.println("Current Token: " + active_token);
                    userClient.run(active_token);
                    break;
                case "6":
                    System.out.println("Avslutar programmet...");
                    System.exit(0);
                default:
                    System.out.println("Ogiltigt val. Försök igen.");
            }
        }
    }

    private static void registerUser() {
        System.out.print("Användarnamn: ");
        String username = scanner.nextLine();
        System.out.print("Lösenord: ");
        String password = scanner.nextLine();

        try {
            String token = authClient.register(username, password);
            System.out.println("Registreringen lyckades. Token: " + token);
        } catch (Exception e) {
            System.out.println("Registreringen misslyckades. Felmeddelande: " + e.getMessage());
        }
    }

    private static void userLogin() {
        System.out.print("Användarnamn: ");
        String username = scanner.nextLine();
        System.out.print("Lösenord: ");
        String password = scanner.nextLine();

        try {
            String token = authClient.login(username, password);
            if(!token.isEmpty()){
                System.out.println("Inloggning lyckades. Token: " + token);

                JSONObject authTokenObj = new JSONObject(token);
                active_token = authTokenObj.getString("accessToken");


            }
        } catch (Exception e) {
            System.out.println("Inloggning misslyckades. Felmeddelande: " + e.getMessage());
        }
    }

}