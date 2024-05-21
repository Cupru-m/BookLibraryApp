package com.example.booklibrary.controllers;


import com.example.booklibrary.dto.BooksDTO;

import com.example.booklibrary.service.BooksService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BooksController {
    private final BooksService booksService;

    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping("/books/high-rating")
    public List<BooksDTO> getBooksWithHighRating(@RequestParam int minRating) {
        return booksService.findBooksWithHighRating(minRating);
    }

    @GetMapping("/books/GetAll")
    public List<BooksDTO> getBooks() {
        return booksService.findAllBooks();
    }

    @PostMapping("/saveBooks")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveBooks(@RequestBody List<BooksDTO> booksDTOs) {
        booksService.saveBooks(booksDTOs);
    }

    @PostMapping("/saveBook")
    @ResponseStatus(HttpStatus.CREATED)
    public void createBook(@RequestBody BooksDTO data) {
        booksService.saveBook(data);
    }

    @PutMapping("/updateBook/{id}")
    public ResponseEntity<BooksDTO> updateBook(@PathVariable Long id,
                                               @RequestParam(required = false) String title,
                                               @RequestParam(required = false) String genre,
                                               @RequestParam(required = false) Integer year,
                                               @RequestParam(required = false) String author) {
        BooksDTO updatedBook = booksService.updateBook(id, title, genre, year, author);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            booksService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}