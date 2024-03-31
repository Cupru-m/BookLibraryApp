package com.example.booklibrary.service;

import com.example.booklibrary.dtos.BookReviewDTO;
import com.example.booklibrary.entity.BookInfo;
import com.example.booklibrary.exceptions.ListEmptyException;
import com.example.booklibrary.repository.BookInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final BookInfoRepository bookInfoRepository;
    public ReviewService(BookInfoRepository bookInfoRepository) {
        this.bookInfoRepository = bookInfoRepository;
    }

    public List<BookReviewDTO> getReviews(String title) {
        List<BookInfo> books = bookInfoRepository.findAll();
        BookInfo foundBook = books.stream()
                .filter(book -> title.equals(book.getTitle()))
                .findFirst()
                .orElseThrow(() -> new ListEmptyException("Книга не найдена"));
        List<BookReviewDTO> reviews =foundBook.getReviews().stream()
                .map(review -> new BookReviewDTO(review.getContent(), review.getRating()))
                .toList();
        if(reviews.isEmpty())
        {
            throw new ListEmptyException("No reviews");
        }
        return reviews;
    }
}
