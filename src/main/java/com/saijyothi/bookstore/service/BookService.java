package com.saijyothi.bookstore.service;

import com.saijyothi.bookstore.dto.BookRequest;
import com.saijyothi.bookstore.dto.BookResponse;
import com.saijyothi.bookstore.entity.Book;
import com.saijyothi.bookstore.exception.BookNotFoundException;
import com.saijyothi.bookstore.repository.BookRepository;
import com.saijyothi.bookstore.specification.BookSpecification;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookResponse> getBooks(
            String search,
            String category,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            BigDecimal minRating,
            String sortBy) {
        return bookRepository.findAll(BookSpecification.filter(search, category, minPrice, maxPrice, minRating), mapSort(sortBy))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public BookResponse getBookById(Long id) {
        return toResponse(getBookEntity(id));
    }

    public List<String> getCategories() {
        return bookRepository.findDistinctCategories();
    }

    public BookResponse createBook(BookRequest request) {
        Book book = new Book();
        applyRequest(book, request);
        return toResponse(bookRepository.save(book));
    }

    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = getBookEntity(id);
        applyRequest(book, request);
        return toResponse(bookRepository.save(book));
    }

    public void deleteBook(Long id) {
        Book book = getBookEntity(id);
        bookRepository.delete(book);
    }

    private Book getBookEntity(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    private void applyRequest(Book book, BookRequest request) {
        book.setTitle(request.title().trim());
        book.setAuthor(request.author().trim());
        book.setPrice(request.price());
        book.setCategory(request.category().trim());
        book.setRating(request.rating());
        book.setImage(request.image().trim());
        book.setDescription(request.description().trim());
        book.setIsbn(request.isbn().trim());
        book.setPages(request.pages());
        book.setPublisher(request.publisher().trim());
        book.setLanguage(request.language().trim());
    }

    private Sort mapSort(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return Sort.by(Sort.Direction.ASC, "id");
        }

        return switch (sortBy.trim().toLowerCase()) {
            case "name", "title" -> Sort.by(Sort.Direction.ASC, "title");
            case "price-low", "priceasc", "price-asc" -> Sort.by(Sort.Direction.ASC, "price");
            case "price-high", "pricedesc", "price-desc" -> Sort.by(Sort.Direction.DESC, "price");
            case "rating" -> Sort.by(Sort.Direction.DESC, "rating").and(Sort.by(Sort.Direction.ASC, "title"));
            default -> Sort.by(Sort.Direction.ASC, "id");
        };
    }

    private BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                book.getCategory(),
                book.getRating(),
                book.getImage(),
                book.getDescription(),
                book.getIsbn(),
                book.getPages(),
                book.getPublisher(),
                book.getLanguage());
    }

    @Component
    static class BookDataLoader implements CommandLineRunner {

        private static final int DEFAULT_BOOK_STOCK = 10;

        private final BookRepository bookRepository;
        private final ObjectMapper objectMapper;

        BookDataLoader(BookRepository bookRepository, ObjectMapper objectMapper) {
            this.bookRepository = bookRepository;
            this.objectMapper = objectMapper;
        }

        @Override
        public void run(String... args) throws IOException {
            if (bookRepository.count() > 0) {
                restoreLegacySeedStockIfNeeded();
                return;
            }

            ClassPathResource resource = new ClassPathResource("seed/books.json");
            try (InputStream inputStream = resource.getInputStream()) {
                List<BookSeedItem> seedItems = objectMapper.readValue(inputStream, new TypeReference<>() {});
                List<Book> books = seedItems.stream().map(this::toEntity).toList();
                bookRepository.saveAll(books);
            }
        }

        private Book toEntity(BookSeedItem item) {
            Book book = new Book();
            book.setTitle(item.title());
            book.setAuthor(item.author());
            book.setPrice(item.price());
            book.setCategory(item.category());
            book.setRating(item.rating());
            book.setImage(item.image());
            book.setDescription(item.description());
            book.setIsbn(item.isbn());
            book.setPages(item.pages());
            book.setPublisher(item.publisher());
            book.setLanguage(item.language());
            book.setStock(DEFAULT_BOOK_STOCK);
            return book;
        }

        private void restoreLegacySeedStockIfNeeded() {
            long totalBooks = bookRepository.count();
            long inStockBooks = bookRepository.countByStockGreaterThan(0);

            if (totalBooks == 0 || inStockBooks > 0) {
                return;
            }

            List<Book> repairedBooks = bookRepository.findAllByOrderByIdAsc().stream()
                    .peek(book -> book.setStock(DEFAULT_BOOK_STOCK))
                    .toList();

            bookRepository.saveAll(repairedBooks);
        }
    }

    record BookSeedItem(
            String title,
            String author,
            BigDecimal price,
            String category,
            BigDecimal rating,
            String image,
            String description,
            String isbn,
            Integer pages,
            String publisher,
            String language) {
    }
}
