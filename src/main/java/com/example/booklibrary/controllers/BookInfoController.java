package com.example.booklibrary.controllers;


import com.example.booklibrary.dtos.BookInfoDTO;
import com.example.booklibrary.service.BookInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}