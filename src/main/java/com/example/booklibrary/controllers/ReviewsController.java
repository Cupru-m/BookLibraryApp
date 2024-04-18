package com.example.booklibrary.controllers;



import com.example.booklibrary.dto.ReviewDTO;
import com.example.booklibrary.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<ReviewDTO> getReviews(@RequestParam String title) {
        return reviewService.getReviews(title);
    }

    @PostMapping("/postRevie")
    @ResponseStatus(HttpStatus.CREATED)
    public void createReview(@RequestParam String title,String content, int rating)
    {
        reviewService.saveReview(title,content,rating);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/UpdateReview/content/{id}")
    public ResponseEntity<String> updateReviewContent(@PathVariable Long id, @RequestBody String content) {
        reviewService.updateReviewContent(id, content);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/UpdateReview/rating/{id}")
    public ResponseEntity<String> updateReviewRating(@PathVariable Long id, @RequestBody int rating) {
        reviewService.updateReviewRating(id, rating);
        return ResponseEntity.ok().build();
    }
}