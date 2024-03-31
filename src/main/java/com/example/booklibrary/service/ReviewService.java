package com.example.booklibrary.service;

import com.example.booklibrary.dtos.ReviewDTO;
import com.example.booklibrary.entity.BookInfo;
import com.example.booklibrary.entity.Review;
import com.example.booklibrary.exceptions.ListEmptyException;
import com.example.booklibrary.repository.BookInfoRepository;
import com.example.booklibrary.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void saveReview(ReviewDTO data)
    {
        Review review = new Review();
        review.setContent(data.getContent());
        review.setRating(data.getRating());
        review.setBookInfo(bookInfoRepository.findByTitle(data.getBookTitle()));
        reviewRepository.save(review);
    }

}
