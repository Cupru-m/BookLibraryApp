package com.example.booklibrary.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BooksDTO {

    private Long id;
    private String title;
    private String genre;
    private int year;
    private String author;

    public BooksDTO(Long id, String title, String genre, int year, String author) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.author = author;
    }
}
