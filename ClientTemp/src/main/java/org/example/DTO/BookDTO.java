package org.example.DTO;

public class BookDTO {

    private Integer id;
    private String title;
    private String genre;
    private Integer authorId; // We'll use authorId instead of author object

    // Constructors, Getters, and Setters

    public BookDTO() {
    }

    public BookDTO(Integer id, String title, String genre, Integer authorId) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.authorId = authorId;
    }

    public BookDTO(Integer id, String title, String genre) {
        this.id = id;
        this.title = title;
        this.genre = genre;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }
}

