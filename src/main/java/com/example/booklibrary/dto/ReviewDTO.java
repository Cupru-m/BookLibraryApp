package com.example.booklibrary.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private String content;
    private int rating;
    private long id;
    public ReviewDTO(long id,String content, int rating)
    {
        this.id=id;
        this.content = content;
        this.rating = rating;
    }
}
