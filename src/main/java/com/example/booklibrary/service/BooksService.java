package com.example.booklibrary.service;

import com.example.booklibrary.cache.MyCache;
import com.example.booklibrary.dto.BooksDTO;
import com.example.booklibrary.entity.Book;

import com.example.booklibrary.exceptions.ListEmptyException;
import com.example.booklibrary.repository.BooksRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BooksService {
    private final BooksRepository booksRepository;
    private final MyCache myCache;

    public BooksService(BooksRepository bookRepository, MyCache myCache) {
        this.booksRepository = bookRepository;
        this.myCache = myCache;
    }

    public List<BooksDTO> findAllBooks() {
        log.info("Получение всех книг");
        List<Book> books = booksRepository.findAll();
        if (books.isEmpty()) {
            log.warn("Список книг пуст");
            throw new ListEmptyException("List is Empty");
        }
        return books.stream()
                .map(book -> new BooksDTO(book.getId(), book.getTitle(), book.getGenre(), book.getYear(), book.getAuthor()))
                .toList();
    }

    public List<BooksDTO> findBooksByGenre(String genre) {
        log.info("Поиск книг по жанру: {}", genre);
        List<Book> books = booksRepository.findByGenre(genre);
        if (books.isEmpty()) {
            log.warn("Не найдено книг с жанром:{} ", genre);
            throw new ListEmptyException("No books found with the genre: " + genre);
        }
        return books.stream()
                .map(book -> new BooksDTO(book.getId(), book.getTitle(), book.getGenre(), book.getYear(), book.getAuthor()))
                .toList();
    }

    public void saveBook(BooksDTO booksDTO) {
        log.info("Сохранение книги: {}", booksDTO.getTitle());
        Book books = new Book();
        books.setTitle(booksDTO.getTitle());
        books.setAuthor(booksDTO.getAuthor());
        books.setGenre(booksDTO.getGenre());
        books.setYear(booksDTO.getYear());
        books.setReviews(new ArrayList<>());
        booksRepository.save(books);
    }

    public BooksDTO updateBook(Long id, String title, String genre, Integer year, String author) {
        log.info("Обновление книги с ID:{}", id);
        Book books = booksRepository.findById(id)
                .orElseThrow(() -> new ListEmptyException("Book not found with id: " + id));

        if (title != null) {
            books.setTitle(title);
        }
        if (genre != null) {
            books.setGenre(genre);
        }
        if (year != null) {
            books.setYear(year);
        }
        if (author != null) {
            books.setAuthor(author);
        }

        booksRepository.save(books);
        return convertToDTO(books);
    }

    public BooksDTO convertToDTO(Book books) {
        return new BooksDTO(books.getId(), books.getTitle(), books.getGenre(),
                books.getYear(), books.getAuthor());
    }

    public void deleteBook(Long id) {
        log.info("Удаление книги с ID: {}", id);
        if (booksRepository.existsById(id)) {
            booksRepository.deleteById(id);
        } else {
            log.warn("Книга с ID: [{}] не найдена", id);
            throw new EntityNotFoundException("Книга с ID: [" + id + "] не найдена");
        }
    }

    public List<BooksDTO> findBooksWithHighRating(int minRating) {
        log.info("Поиск книг с рейтингом выше:{} ", minRating);
        String cacheKey = "booksWithHighRating_" + minRating;
        List<BooksDTO> cachedBooks = (List<BooksDTO>) myCache.get(cacheKey);
        if (cachedBooks != null) {
            log.info("Книги с рейтингом выше {} найдены в кэше", minRating);
            return cachedBooks;
        }

        List<Book> books = booksRepository.findBooksWithHighRating(minRating);
        if (books.isEmpty()) {
            log.warn("Список книг пуст");
            throw new ListEmptyException("List is Empty");
        }
        List<BooksDTO> booksDTOs = books.stream()
                .map(book -> new BooksDTO(book.getId(), book.getTitle(), book.getGenre(), book.getYear(), book.getAuthor()))
                .toList();
        myCache.put(cacheKey, booksDTOs);
        return booksDTOs;
    }
}
