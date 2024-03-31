package com.example.booklibrary.repository;


import com.example.booklibrary.entity.BookInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookInfoRepository extends JpaRepository<BookInfo,Long>
{
    BookInfo findByTitle(String title);
}