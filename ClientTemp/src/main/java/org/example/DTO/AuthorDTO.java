package org.example.DTO;

import java.util.List;

public class AuthorDTO {

    private Integer id;
    private String name;
    private List<BookDTO> books;

    public AuthorDTO() {
        // Default constructor
    }

    public AuthorDTO(Integer id, String name, List<BookDTO> books) {
        this.id = id;
        this.name = name;
        this.books = books;
    }

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BookDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookDTO> books) {
        this.books = books;
    }
}

