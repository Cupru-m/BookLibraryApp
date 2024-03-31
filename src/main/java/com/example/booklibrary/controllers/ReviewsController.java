package com.example.booklibrary.controllers;


import com.example.booklibrary.dtos.BookReviewDTO;
import com.example.booklibrary.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewsController {
    private final ReviewService reviewService;
    public ReviewsController( ReviewService reviewService)
    {
        this.reviewService = reviewService;
    }

    @GetMapping("/bookReviews")
    public List<BookReviewDTO> getReviews(@RequestParam String title) {
        return reviewService.getReviews(title);
    }

    @PostMapping("/postRevie")
    @ResponseStatus(HttpStatus.CREATED)
    public createReview(@RequestBody BookReviewDTO data)
    {
        reviewService.saveReviw(data);
    }
}