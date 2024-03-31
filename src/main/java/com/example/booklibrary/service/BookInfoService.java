package com.example.booklibrary.service;
import com.example.booklibrary.dtos.BookInfoDTO;
import com.example.booklibrary.entity.BookInfo;
import com.example.booklibrary.entity.Review;
import com.example.booklibrary.exceptions.ListEmptyException;
import com.example.booklibrary.repository.BookInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookInfoService
{
    private final BookInfoRepository bookInfoRepository;


    public BookInfoService(BookInfoRepository bookInfoRepository) {
        this.bookInfoRepository = bookInfoRepository;
    }


    public List<BookInfoDTO> findAllBooks() {

        List<BookInfo> books = bookInfoRepository.findAll();
        if(books.isEmpty())
        {
            throw new ListEmptyException("List is Empty");
        }
        return books.stream()
                .map(book -> new BookInfoDTO(book.getId(),book.getTitle(), book.getGenre(), book.getYear(), book.getAuthor()))
                .toList();
    }

    public List<BookInfoDTO> findBooksByTitle(String title) {
        List<BookInfo> books = (List<BookInfo>) bookInfoRepository.findByTitle(title);
        if (books.isEmpty()) {
            throw new ListEmptyException("No books found with the title: " + title);
        }
        return books.stream()
                .map(book -> new BookInfoDTO(book.getId(),book.getTitle(), book.getGenre(), book.getYear(),book.getAuthor()))
                .toList();
    }
    public void saveBook(BookInfoDTO bookInfoDTO) {
        BookInfo bookInfo = new BookInfo();
        bookInfo.setTitle(bookInfoDTO.getTitle());
        bookInfo.setAuthor(bookInfoDTO.getAuthor());
        bookInfo.setGenre(bookInfoDTO.getGenre());
        bookInfo.setYear(bookInfoDTO.getYear());
        bookInfo.setReviews(new ArrayList<Review>());
        bookInfoRepository.save(bookInfo);
    }

    public BookInfoDTO updateBook(Long id, String title, String genre, Integer year, String author) {
        BookInfo bookInfo = bookInfoRepository.findById(id)
                .orElseThrow(() -> new ListEmptyException("Book not found with id: " + id));

        if (title != null) {
            bookInfo.setTitle(title);
        }
        if (genre != null) {
            bookInfo.setGenre(genre);
        }
        if (year != null) {
            bookInfo.setYear(year);
        }
        if (author != null) {
            bookInfo.setAuthor(author);
        }

        bookInfoRepository.save(bookInfo);
        return convertToDTO(bookInfo);
    }
    public BookInfoDTO convertToDTO(BookInfo bookInfo)
    {
        BookInfoDTO  bookInfoDTO = new BookInfoDTO
                (bookInfo.getId(),bookInfo.getTitle(),bookInfo.getGenre(),bookInfo.getYear(),bookInfo.getAuthor());
        return bookInfoDTO;
    }

    public void deleteBook(Long id) {
        if(bookInfoRepository.existsById(id)){
            bookInfoRepository.deleteById(id);
        }
        else{
            throw  new EntityNotFoundException("Книга с ID: [" + id + "] не найдена");
        }
    }
}
