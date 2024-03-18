# Project Documentation

## Overview

This documentation provides comprehensive information about the project, including its description, prerequisites, setup instructions, API details, and project type.

## Project Description

Our project is a Java 17 Maven application that leverages GitHub workflows for Continuous Integration and Continuous Deployment (CI/CD). It's designed as a Spring Boot web service, focusing on managing books and authors, establishing a robust relationship between them. Utilizing RESTful endpoints, the project interacts with a MySQL database for data persistence.

One of the key highlights of our project is its integration with Amazon Web Services (AWS) for cloud deployment. By deploying our application to the cloud, we ensure scalability, reliability, and accessibility. The entire development process, from code commits to live deployment, is streamlined through a comprehensive CI/CD pipeline. This ensures that changes made to the codebase are automatically tested, built, and deployed to a live server environment.

The database, powered by MySQL, is hosted online on the cloud to facilitate seamless data management and accessibility from anywhere. This allows our application to operate efficiently with real-time data updates and ensures high availability and data integrity.

In summary, our application embodies modern software development practices by integrating cutting-edge technologies like Java 17, Spring Boot, GitHub workflows, AWS cloud infrastructure, and MySQL database management. It epitomizes the industry-standard CI/CD approach, enabling rapid development cycles, robust testing, and efficient deployment to live servers.

## Prerequisites

Before running the program, ensure you have the following prerequisites installed:

- Java Development Kit (JDK) 17: Ensure JDK 17 or later is installed on your system.
- AWS Account: Create an AWS account for cloud deployment and integration.
- Spring Boot IDE: Optionally, use an Integrated Development Environment (IDE) like IntelliJ IDEA or Eclipse with Spring Boot support for convenient development.
- Postman: You'll need Postman to interact with the program and its endpoints.
- MySQL Workbench: If you decide to run the program locally make sure you create a database according to the `application.properties` file.

## Running the Program

To run the program, follow these steps:

1. **Step 1: MySQL Database Setup**: Set up a MySQL database instance either locally or on a cloud platform like AWS RDS.
2. **Step 2: Start Your Instances and Turn On the Spring Boot Application**: Make sure your locally- or cloud-driven database is running.
3. **Step 3: Once everything runs successfully, open Postman and use the documented endpoints.**

## API

The project includes the following endpoints and functionalities:

### Books & Authors (Public Access)

#### Endpoint 1: [View All Books]

- **Description**: This endpoint gives you a list of all the books currently in the database.
- **Method**: GET
- **Endpoint**: `http://localhost:8080/books`
- **Request Parameters**:
  - Parameter 1: No parameters required
- **Request Body**: No body required
- **Response**: A list of ALL books

#### Endpoint 2: [View One Book]

- **Description**: This endpoint gives you a specific book based on its book ID.
- **Method**: GET
- **Endpoint**: `http://localhost:8080/books/{bookId}`
- **Request Parameters**:
  - Parameter 1: bookId (Integer) - The ID of the existing book
- **Request Body**: No body required
- **Response**: Title, Genre, Author

#### Endpoint 3: [View All Authors]

- **Description**: View all authors currently in the database.
- **Method**: GET
- **Endpoint**: `http://localhost:8080/authors`
- **Request Parameters**:
  - Parameter 1: No parameters required
- **Request Body**: No body required
- **Response**: A list of ALL authors

#### Endpoint 4: [View One Author]

- **Description**: View one author currently in the database.
- **Method**: GET
- **Endpoint**: `http://localhost:8080/authors/{authorId}`
- **Request Parameters**:
  - Parameter 1: authorId (Integer) - The ID of the existing Author
- **Request Body**: No body required
- **Response**: Name, Books

### Authentication (Public Access)

#### Endpoint 1: [Register]

- **Description**: Register as a User.
- **Method**: POST
- **Endpoint**: `http://localhost:8080/auth/register`
- **Request Parameters**: No parameters required
- **Request Body**: username, password
- **Response**: You have successfully registered

#### Endpoint 2: [Login]

- **Description**: Login as a User or as an Admin.
- **Method**: POST
- **Endpoint**: `http://localhost:8080/auth/login`
- **Request Parameters**: No parameters required
- **Request Body**: username, password
- **Response**: Jwt Bearer Token

### CRUD Operations (User Access)

#### Books:

#### Endpoint 1: [Create Book]

- **Description**: Create a new book.
- **Method**: POST
- **Endpoint**: `http://localhost:8080/books`
- **Request Parameters**: No parameters required
- **Request Body**: Title, Genre, Author
- **Response**: [ ]

#### Endpoint 2: [Update a Book]

- **Description**: Update an existing book.
- **Method**: PATCH
- **Endpoint**: `http://localhost:8080/books/{bookId}`
- **Request Parameters**:
  - Parameter 1: bookId (Integer) - The ID of the existing book
- **Request Body**: Title, Genre, Author
- **Response**: Updated Book

#### Endpoint 3: [Delete Book]

- **Description**: Delete an existing book.
- **Method**: DELETE
- **Endpoint**: `http://localhost:8080/books/{bookId}`
- **Request Parameters**:
  - Parameter 1: bookId (Integer) - The ID of the existing book
- **Request Body**: No body required
- **Response**: None, 201 OK

#### Authors:

#### Endpoint 1: [Create an Author]

- **Description**: Creates an Author and connects the author to the books with the same name.
- **Method**: POST
- **Endpoint**: `http://localhost:8080/authors`
- **Request Parameters**: No parameters required
- **Request Body**: name
- **Response**: Author Created, 201 OK

#### Endpoint 2: [Add Book To Author]

- **Description**: Adds an existing book to the specified author.
- **Method**: POST
- **Endpoint**: `http://localhost:8080/authors/{authorId}/book/{bookId}`
- **Request Parameters**:
  - Parameter 1: authorId (Integer) - The ID of the author to which the book will be added
  - Parameter 2: bookId (Integer) - The ID of the existing book to be added to the author
- **Request Body**: No body required
- **Response**: The updated Author object

#### Endpoint 3: [Update Author]

- **Description**: Update an existing Author.
- **Method**: PATCH
- **Endpoint**: `http://localhost:8080/authors/{authorId}`
- **Request Parameters**:
  - Parameter 1: authorId (Integer) - The ID of the existing Author
- **Request Body**: name
- **Response**: The updated Author object

#### Endpoint 4: [Delete Author]

- **Description**: Delete an existing Author.
- **Method**: DELETE
- **Endpoint**: `http://localhost:8080/authors/{authorId}`
- **Request Parameters**:
  - Parameter 1: authorId (Integer) - The ID of the existing author
- **Request Body**: No body required
- **Response**: author Deleted

### Admin Operations (Admin & Root Admin Access)

#### Endpoint 1: [Get all Users]

- **Description**: Get a list of all registered Users.
- **Method**: GET
- **Endpoint**: `http://localhost:8080/admin/users`
- **Request Parameters**: No parameter required
- **Request Body**: No body required
- **Response**: A list of all registered Users

#### Endpoint 2: [Get one User]

- **Description**: Get one user based on their User ID.
- **Method**: GET
- **Endpoint**: `http://localhost:8080/admin/users/`
- **Request Parameters**:
  - Parameter 1: userID (Integer) - The ID of the existing User
- **Request Body**: No body required
- **Response**: Info about specified User

#### Endpoint 3: [Delete User]

- **Description**: Delete an existing User.
- **Method**: DELETE
- **Endpoint**: `http://localhost:8080/admin/users/{userId}`
- **Request Parameters**:
  - Parameter 1: userID (Integer) - The id of the existing User
- **Request Body**: No body required
- **Response**: User deleted, 201 OK

#### Endpoint 4: [Update User Role]

- **Description**: Update a User's role from User to Admin.
- **Method**: PUT
- **Endpoint**: `http://localhost:8080/admin/users/roles{userId}`
- **Request Parameters**:
  - Parameter 1: userID (Integer) - The id of the existing User
- **Request Body**: [ ]
- **Response**: [ ]

#### Endpoint 5: [Remove Admin Role]

- **Description**: Remove Admin Role from a User.
- **Method**: DELETE
- **Endpoint**: `http://localhost:8080/admin/users/roles/{userId}`
- **Request Parameters**:
  - Parameter 1: userID (Integer) - The id of the existing User
- **Request Body**: No body required
- **Response**: User Role Deleted, 201 OK

## Root Admin Operations And Limitations

Root Admins has all the permissions Admin and User has.
Root Admins cannot be removed or demoted to a normal User.
