package com.example.booklibrary.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonSerialize(using = BookInfoDTOSerializer.class)
public class BookInfoDTO {

    private String title;
    private String genre;
    private int year;
    private String author;

    public BookInfoDTO(String title, String genre, int year, String author) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.author = author;
    }
}
