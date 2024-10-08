package com.example.booklibrary.service;

import com.example.booklibrary.dto.ReviewDTO;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Review;
import com.example.booklibrary.exceptions.ListEmptyException;
import com.example.booklibrary.repository.BooksRepository;
import com.example.booklibrary.repository.ReviewsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final BooksRepository booksRepository;
    private final ReviewsRepository reviewsRepository;

    public ReviewService(BooksRepository booksRepository, ReviewsRepository reviewsRepository) {
        this.booksRepository = booksRepository;
        this.reviewsRepository = reviewsRepository;
    }

    public List<ReviewDTO> getReviews(String title) {
        Book foundBook = booksRepository.findByTitle(title);
        if(foundBook == null){
         throw    new ListEmptyException("Книга не найдена");
        }
        List<ReviewDTO> reviews = foundBook.getReviews().stream()
                .map(review -> new ReviewDTO(review.getId(), review.getContent(), review.getRating()))
                .toList();
        if (reviews.isEmpty()) {
            throw new ListEmptyException("No reviews");
        }
        return reviews;
    }

    public void saveReview(String title, String content, int rating) {
        Review review = new Review();
        review.setContent(content);
        review.setRating(rating);
        review.setId(1L);
        Book books = booksRepository.findByTitle(title);
        if (books != null) {
            review.setBooks(books);
           List<Review> reviews = new ArrayList<Review>();
           reviews.add(review);
           List<Review> otherReviews = books.getReviews();
           if(otherReviews != null)
           {
             reviews.addAll(otherReviews);
           }
               books.setReviews(reviews);
        } else {
            throw new ListEmptyException("Книга не найдена");
        }

        reviewsRepository.save(review);
    }

    public void deleteReview(Long id) {
        if (reviewsRepository.existsById(id)) {
            reviewsRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Отзыв с ID: [" + id + "] не найден");
        }
    }

    public void updateReviewContent(Long id, String content) {
        Optional<Review> reviewOptional = reviewsRepository.findById(id);
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            review.setContent(content);
            reviewsRepository.save(review);
        } else {
            throw new IllegalArgumentException("Отзыв с ID [" + id + "] не найден.");
        }
    }

    public void updateReviewRating(Long id, int rating) {
        Optional<Review> reviewOptional = reviewsRepository.findById(id);

        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            review.setRating(rating);
            reviewsRepository.save(review);
        } else {
            throw new IllegalArgumentException("Отзыв с ID [" + id + "] не найден.");
        }
    }
}
