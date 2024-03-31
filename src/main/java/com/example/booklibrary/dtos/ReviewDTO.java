package com.example.booklibrary.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private String content;
    private int rating;
    private String bookTitle;
    public ReviewDTO(String bookTitle,String content, int rating)
    {
        this.content = content;
        this.rating = rating;
    }
}
