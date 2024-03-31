package com.example.booklibrary.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "bookinfo")
public class BookInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String genre;
    private int year;
    private String author;

    @OneToMany(mappedBy = "bookInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
}
