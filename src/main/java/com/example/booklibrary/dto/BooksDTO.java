package com.example.booklibrary.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BooksDTO)) return false;
        BooksDTO that = (BooksDTO) o;
        return getId().equals(that.getId()) &&
                getTitle().equals(that.getTitle()) &&
                getGenre().equals(that.getGenre()) &&
                getYear() == that.getYear() && // Убедитесь, что тип года совместим с оператором ==
                getAuthor().equals(that.getAuthor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getGenre(), getYear(), getAuthor());
    }

}
