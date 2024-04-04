package com.example.booklibrary.service;

import com.example.booklibrary.dtos.ReviewDTO;
import com.example.booklibrary.entity.BookInfo;
import com.example.booklibrary.entity.Review;
import com.example.booklibrary.exceptions.ListEmptyException;
import com.example.booklibrary.repository.BookInfoRepository;
import com.example.booklibrary.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final BookInfoRepository bookInfoRepository;
    private final ReviewRepository reviewRepository;

    public ReviewService(BookInfoRepository bookInfoRepository, ReviewRepository reviewRepository)
    {
        this.bookInfoRepository = bookInfoRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<ReviewDTO> getReviews(String title) {
        List<BookInfo> books = bookInfoRepository.findAll();
        BookInfo foundBook = books.stream()
                .filter(book -> title.equals(book.getTitle()))
                .findFirst()
                .orElseThrow(() -> new ListEmptyException("Книга не найдена"));
        List<ReviewDTO> reviews =foundBook.getReviews().stream()
                .map(review -> new ReviewDTO(foundBook.getTitle(),review.getContent(), review.getRating()))
                .toList();
        if(reviews.isEmpty())
        {
            throw new ListEmptyException("No reviews");
        }
        return reviews;
    }
    public void saveReview(String title,String content,int rating)
    {
        Review review = new Review();
        review.setContent(content);
        review.setRating(rating);
        BookInfo bookInfo = bookInfoRepository.findByTitle(title);
        if(bookInfo!=null)
        review.setBookInfo(bookInfo);
        else
            throw new ListEmptyException("Книга не найдена");
        reviewRepository.save(review);
    }


    public void deleteReview(Long id) {
        if(reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
        }
        else {
            throw  new EntityNotFoundException("Отзыв с ID: [" + id + "] не найден");
        }
    }

    public void updateReviewContent(Long id, String content) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
                review.setContent(content);
            reviewRepository.save(review);
        } else {
            throw new IllegalArgumentException("Отзыв с ID [" + id + "] не найден.");
        }
    }

    public void updateReviewRating(Long id, int rating) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);

        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
                review.setRating(rating);
            reviewRepository.save(review);
        } else {
            throw new IllegalArgumentException("Отзыв с ID [" + id + "] не найден.");
        }
    }
}
