import com.example.booklibrary.dto.ReviewDTO;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Review;
import com.example.booklibrary.exceptions.ListEmptyException;
import com.example.booklibrary.repository.BooksRepository;
import com.example.booklibrary.repository.ReviewsRepository;
import com.example.booklibrary.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private BooksRepository booksRepository;

    @Mock
    private ReviewsRepository reviewsRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    public Book bookConstructor(String title, String author, String genre, int year,long id) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setId(id);
        book.setGenre(genre);
        book.setYear(year);
        book.setReviews(new ArrayList<>());
        return book;
    }
    public Review reviewConstructor(Book book,String content,int rating,long id)
{
    Review review = new Review();
    review.setContent(content);
    review.setRating(rating);
    review.setId(id);
    review.setBooks(book);
    return  review;
}

    @Test
    void testGetReviewsNotFound() {
        String title = "Nonexistent Title";
        when(booksRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(ListEmptyException.class, () -> reviewService.getReviews(title));
    }
    @Test
    void testSaveReview() {
        // Подготовка
        String title = "Test Title";
        String content = "Great book!";
        int rating = 5;
        long bookId = 123456789L; // Предполагаемый ID книги
        long reviewId = 987654321L; // Предполагаемый ID отзыва
        Book book = bookConstructor(title, "Name", "Genre", 2000, bookId);
        // Нас тройка поведения моков
        when(booksRepository.findByTitle(title)).thenReturn(book);
        
        booksRepository.save(book);
        reviewService.saveReview(title, content, rating);

        // Проверка
        verify(booksRepository, times(1)).findByTitle(title);
        verify(reviewsRepository, times(1)).save(any(Review.class));
        assertTrue(reviewService.getReviews(title).stream().anyMatch(review -> review.getContent().equals(content) && review.getRating() == rating));
    }

    @Test
    void testUpdateReviewContent_ExistingReview() {
        // Подготовка
        Long existingReviewId = 1L;
        String newContent = "Updated Content";
        Review existingReview = new Review();
        existingReview.setId(existingReviewId);
        existingReview.setContent("Initial Content");

        // Настройка мока для симуляции существующего отзыва
        when(reviewsRepository.findById(existingReviewId)).thenReturn(Optional.of(existingReview));

        // Выполнение
        reviewService.updateReviewContent(existingReviewId, newContent);

        // Проверка
        verify(reviewsRepository, times(1)).findById(existingReviewId);
        verify(reviewsRepository, times(1)).save(any(Review.class));

        // Проверка, что содержание отзыва было обновлено
        Review updatedReview = reviewsRepository.findById(existingReviewId).get();
        assertEquals(newContent, updatedReview.getContent());
    }

    @Test
    void testUpdateReviewContent_NonExistingReview() {
        // Подготовка
        Long nonExistingReviewId = 999L;

        // Ожидаемое исключение
        assertThrows(IllegalArgumentException.class, () -> {
            reviewService.updateReviewContent(nonExistingReviewId, "New Content");
        });
    }
    @Test
    void testUpdateReviewRating_ExistingReview() {
        // Подготовка
        Long existingReviewId = 1L;
        int newRating = 5;
        Review existingReview = new Review();
        existingReview.setId(existingReviewId);
        existingReview.setRating(3);

        // Настройка мока для симуляции существующего отзыва
        when(reviewsRepository.findById(existingReviewId)).thenReturn(Optional.of(existingReview));

        // Выполнение
        reviewService.updateReviewRating(existingReviewId, newRating);

        // Проверка
        verify(reviewsRepository, times(1)).findById(existingReviewId);
        verify(reviewsRepository, times(1)).save(any(Review.class));

        // Проверка, что рейтинг отзыва был обновлен
        Review updatedReview = reviewsRepository.findById(existingReviewId).get();
        assertEquals(newRating, updatedReview.getRating());
    }

    @Test
    void testUpdateReviewRating_NonExistingReview() {
        // Подготовка
        Long nonExistingReviewId = 999L;

        // Ожидаемое исключение
        assertThrows(IllegalArgumentException.class, () -> {
            reviewService.updateReviewRating(nonExistingReviewId, 5);
        });
    }
}
