package com.example.booklibrary.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookReviewDTO {
    private String content;
    private int rating;
    public BookReviewDTO(String content, int rating)
    {
        this.content = content;
        this.rating = rating;
    }
}
