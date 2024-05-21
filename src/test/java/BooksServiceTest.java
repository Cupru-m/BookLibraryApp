import com.example.booklibrary.entity.Book;
import com.example.booklibrary.exceptions.ListEmptyException;
import com.example.booklibrary.service.BooksService;
import com.example.booklibrary.repository.BooksRepository;
import com.example.booklibrary.cache.MyCache;
import com.example.booklibrary.dto.BooksDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BooksServiceTest {

    private BooksService booksService;
    private BooksRepository booksRepository;
    private MyCache myCache;

    @BeforeEach
    public void setUp() {
        booksRepository = mock(BooksRepository.class);
        myCache = mock(MyCache.class);
        booksService = new BooksService(booksRepository, myCache);
    }
    public Book bookConstructor(String title, String author, String genre, int year,long id) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setId(id);
        book.setGenre(genre);
        book.setYear(year);
        return book;
    }
    @Test
    public void testFindAllBooks() {
        Book book1 = bookConstructor("Book 1","Author 1","Genre 1",2020,1L);

        Book book2 = bookConstructor("Book 2","Author 2","Genre 2",2021,2L);

        List<Book> books = Arrays.asList(book1, book2);

        when(booksRepository.findAll()).thenReturn(books);

        List<BooksDTO> result = booksService.findAllBooks();

        assertAll("Проверка всех книг",
                () -> assertEquals(2, result.size(), "Размер списка должен быть 2"),
                () -> assertEquals("Book 1", result.get(0).getTitle(), "Заголовок первой книги должен быть 'Book 1'"),
                () -> assertEquals("Genre 1", result.get(0).getGenre(), "Жанр первой книги должен быть 'Genre 1'"),
                () -> assertEquals(2020, result.get(0).getYear(), "Год первой книги должен быть 2020"),
                () -> assertEquals("Author 1", result.get(0).getAuthor(), "Автор первой книги должен быть 'Author 1'"),
                () -> assertEquals("Book 2", result.get(1).getTitle(), "Заголовок второй книги должен быть 'Book 2'"),
                () -> assertEquals("Genre 2", result.get(1).getGenre(), "Жанр второй книги должен быть 'Genre 2'"),
                () -> assertEquals(2021, result.get(1).getYear(), "Год второй книги должен быть 2021"),
                () -> assertEquals("Author 2", result.get(1).getAuthor(), "Автор второй книги должен быть 'Author 2'")
        );
    }
    @Test
    public void testSaveBook() {
        BooksDTO booksDTO = new BooksDTO((long)0,"Book1", "genre", 2023, "author");

        booksService.saveBook(booksDTO);

        verify(booksRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testUpdateBook() {
        Long id = 1L;
        String title = "New Title";
        String genre = "Fiction";
        Integer year = 2000;
        String author = "New Author";

        Book expectedBook = new Book();
        expectedBook.setAuthor(author);
        expectedBook.setYear(year);
        expectedBook.setTitle(title);
        expectedBook.setGenre(genre);
        expectedBook.setId(id);

        when(booksRepository.findById(id)).thenReturn(Optional.of(expectedBook));

        BooksDTO actualDTO = booksService.updateBook(id, title, genre, year, author);

        assertEquals(expectedBook.getTitle(), actualDTO.getTitle());
        assertEquals(expectedBook.getGenre(), actualDTO.getGenre());
        assertEquals(expectedBook.getYear(), actualDTO.getYear());
        assertEquals(expectedBook.getAuthor(), actualDTO.getAuthor());

        verify(booksRepository, times(1)).save(expectedBook);
    }
    @Test
    public void testConvertToDTO() {
        Book book = bookConstructor("Book 1","Author 1","Genre 1",2020,1L);

        BooksDTO dto = booksService.convertToDTO(book);

        assertEquals(book.getId(), dto.getId());
        assertEquals(book.getTitle(), dto.getTitle());
        assertEquals(book.getGenre(), dto.getGenre());
        assertEquals(book.getYear(), dto.getYear());
        assertEquals(book.getAuthor(), dto.getAuthor());
    }
    @Test
    public void testDeleteBookExists() {
        Long id = 1L;

        when(booksRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> booksService.deleteBook(id));

        verify(booksRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteBookNotExists() {
        Long id = 1L;

        when(booksRepository.existsById(id)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> booksService.deleteBook(id));
        assertEquals("Книга с ID: [" + id + "] не найдена", exception.getMessage());

        verify(booksRepository, never()).deleteById(id);
    }
    @Test
    public void FindBooksWithHighRatingCachedBooksExist() {
        int minRating = 4;

        String cacheKey = "booksWithHighRating_" + minRating;
        List<BooksDTO> cachedBooks = Collections.singletonList(new BooksDTO(1L, "Book Title", "Genre", 2021, "Author"));

        when(myCache.get(cacheKey)).thenReturn(cachedBooks);

        List<BooksDTO> result = booksService.findBooksWithHighRating(minRating);

        assertEquals(cachedBooks, result);
        verify(myCache, times(1)).get(cacheKey);
        verify(booksRepository, never()).findBooksWithHighRating(minRating);
    }

    @Test
    void testFindBooksWithHighRatingEmptyList() {
        int minRating = 5;
        when(booksRepository.findBooksWithHighRating(minRating)).thenReturn(Collections.emptyList());

        assertThrows(ListEmptyException.class, () -> booksService.findBooksWithHighRating(minRating));
    }
    @Test
    void FindBooksWithHighRatingFromCache() {
        int minRating = 5;
        List<BooksDTO> expectedBooks = Arrays.asList(new BooksDTO(1L, "Book1", "Genre1", 2000, "Author1"),
                new BooksDTO(2L, "Book2", "Genre2", 2001, "Author2"));
        when(myCache.get(anyString())).thenReturn(expectedBooks);
        myCache.put("booksWithHighRating_"+minRating,expectedBooks);
        List<BooksDTO> result = booksService.findBooksWithHighRating(minRating);

        verify(myCache, times(1)).get(anyString());
        assertEquals(expectedBooks, result);
    }
    @Test
    void testFindBooksWithHighRatingFromRepoAndCache() {
        int minRating = 5;
        List<Book> books = Arrays.asList(bookConstructor("Author1", "Book1", "Genre1", 2000, 1L),bookConstructor("Author2", "Book2", "Genre2", 2001, 2L));
        List<BooksDTO> expectedBooks = books.stream()
                .map(book -> new BooksDTO(book.getId(), book.getTitle(), book.getGenre(), book.getYear(), book.getAuthor()))
                .toList();
        when(booksRepository.findBooksWithHighRating(minRating)).thenReturn(books);
        when(myCache.get(anyString())).thenReturn(null);

        List<BooksDTO> result = booksService.findBooksWithHighRating(minRating);

        verify(myCache, times(1)).put(anyString(), anyList());
        verify(booksRepository, times(1)).findBooksWithHighRating(minRating);
        assertEquals(expectedBooks, result);
    }
    @Test// проверка что метод сэйв вызоветмя 2 раза для каждой книги
    void testSaveBooks() {
        List<BooksDTO> booksDTOs = Arrays.asList(
                new BooksDTO(1L, "Book1", "Genre1", 2000, "Author1"),
                new BooksDTO(2L, "Book2", "Genre2", 2001, "Author2")
        );
        booksService.saveBooks(booksDTOs);
        verify(booksRepository, times(2)).save(any(Book.class));
    }

}
