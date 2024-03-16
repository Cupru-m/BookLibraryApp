package com.example.booklibrary.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
