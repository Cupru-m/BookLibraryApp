package com.example.booklibrary.controllers;


import com.example.booklibrary.dtos.BookInfoDTO;

import com.example.booklibrary.service.BookInfoService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookInfoController {
    private final BookInfoService bookInfoService;

    public BookInfoController(BookInfoService bookInfoService) {
        this.bookInfoService = bookInfoService;
    }

    @GetMapping("/books")
    public List<BookInfoDTO> getBooks(@RequestParam(required = false) String title)
    {
            if (title != null) {

                return bookInfoService.findBooksByTitle(title);
            }
            return bookInfoService.findAllBooks();
    }
    @PostMapping("/saveBook")
    @ResponseStatus(HttpStatus.CREATED)
    public void createBook(@RequestBody BookInfoDTO data) {
          bookInfoService.saveBook(data);
    }

    @PutMapping("/updateBook/{id}")
    public ResponseEntity<BookInfoDTO> updateBook(@PathVariable Long id,
                                                  @RequestParam(required = false) String title,
                                                  @RequestParam(required = false) String genre,
                                                  @RequestParam(required = false) Integer year,
                                                  @RequestParam(required = false) String author) {
        BookInfoDTO updatedBook = bookInfoService.updateBook(id, title, genre, year, author);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            bookInfoService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}