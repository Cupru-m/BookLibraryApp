package com.example.booklibrary.repository;


import com.example.booklibrary.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BooksRepository extends JpaRepository<Book, Long> {
    Book findByTitle(String title);

    @Query(value = "SELECT b.* FROM books b JOIN reviews r ON b.id = r.book_id WHERE r.rating > :minRating", nativeQuery = true)
    List<Book> findBooksWithHighRating(@Param("minRating") int minRating);
    List<Book> findByGenre(String genre);
}


//SELECT b FROM Book b JOIN b.reviews r WHERE r.rating > :minRating