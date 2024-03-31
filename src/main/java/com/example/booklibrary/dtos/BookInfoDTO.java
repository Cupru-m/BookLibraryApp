package com.example.booklibrary.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookInfoDTO {

    private Long id;
    private String title;
    private String genre;
    private int year;
    private String author;

    public BookInfoDTO(Long id,String title, String genre, int year, String author) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.author = author;
    }
}
