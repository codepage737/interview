package com.shahrabi.interview.service.main.impl;

import com.shahrabi.interview.TestcontainersConfiguration;
import com.shahrabi.interview.service.main.BookService;
import com.shahrabi.interview.service.main.BorrowService;
import com.shahrabi.interview.service.main.dto.BookDto;
import com.shahrabi.interview.service.main.dto.BorrowDto;
import com.shahrabi.interview.service.main.exception.BookAlreadyBorrowedException;
import com.shahrabi.interview.service.main.exception.BookAlreadyDeletedException;
import com.shahrabi.interview.service.main.exception.DuplicateIsbnException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DisplayName(value = "Book Service Test")
class BookServiceImplTest {

    private final String VALID_ISBN = "978-964-000-005-5";
    private final String DELETED_ISBN = "978-964-000-030-0";
    private final String INVALID_ISBN = "???-???-???-???-?";
    private final String NEW_ISBN = "978-964-000-111-1";
    private final String BORROWER_NAME = "soheil";

    @Autowired
    private BookService bookService;
    @Autowired
    private BorrowService borrowService;

    @Autowired
    PostgreSQLContainer<?> postgresql;

    @Nested
    @DisplayName(value = "Book Specification Test")
    class BorrowSpecificationTest {

    }

    @Nested
    @DisplayName(value = "Save New Book Test")
    @Transactional
    class SaveNewBookTest {

        @Test
        @DisplayName(value = "create book with new ISBN should work")
        void createBookWithNewISBNShouldWork() {
            BookDto.CommandBookDto test = BookDto.CommandBookDto.builder()
                    .title("fake book")
                    .authorName("Soheil")
                    .publishYear(1999)
                    .isbn(NEW_ISBN)
                    .build();

            BookDto.CommandBookDto commandBookDto = bookService.saveBook(test);
            assertEquals(test.getTitle(), commandBookDto.getTitle());
            assertEquals(test.getAuthorName(), commandBookDto.getAuthorName());
            assertEquals(test.getIsbn(), commandBookDto.getIsbn());
            assertEquals(test.getPublishYear(), commandBookDto.getPublishYear());
            assertTrue(commandBookDto.getIsAvailable());
            assertFalse(commandBookDto.getIsDeleted());
        }

        @Test
        @DisplayName(value = "create book should throw exception if ISBN already exist")
        void createBookShouldReturnThrowIfISBNAlreadyExist() {
            BookDto.CommandBookDto test = BookDto.CommandBookDto.builder()
                    .title("fake book")
                    .authorName("Soheil")
                    .publishYear(1999)
                    .isbn(NEW_ISBN)
                    .build();

            bookService.saveBook(test);
            Exception exception = assertThrows(DuplicateIsbnException.class, () -> {
                bookService.saveBook(test);
            });
            assertEquals("error.book.isbn.duplicate", exception.getMessage());
        }
    }


    @Nested
    @DisplayName(value = "Update Book")
    @Transactional
    class UpdateBookTest {

        @Test
        @DisplayName(value = "update book should throw exception if ISBN was not exist")
        void updateBookShouldReturnExceptionIfBookWasNotExist() {
            BookDto.CommandBookDto test = BookDto.CommandBookDto.builder()
                    .title("fake book")
                    .authorName("Soheil")
                    .publishYear(1999)
                    .isbn(NEW_ISBN)
                    .build();
            Exception exception = assertThrows(EntityNotFoundException.class, () -> {
                bookService.update(test);
            });
            assertEquals("error.book.isbn.not_found", exception.getMessage());
        }

        @Test
        @DisplayName(value = "update book should throw exception if ISBN was already deleted")
        void updateBookShouldReturnExceptionIfBookWasAlreadyDeleted() {
            BookDto.CommandBookDto bookDto = bookService.findByIsbn(DELETED_ISBN);
            bookDto.setTitle("");
            Exception exception = assertThrows(BookAlreadyDeletedException.class, () -> {
                bookService.update(bookDto);
            });
            assertEquals("error.book.operation.already_deleted", exception.getMessage());
        }

        @Test
        @DisplayName(value = "update book should works if ISBN was exist and not deleted")
        void updateBookShouldWorksIfISBNWasExistAndNotDeleted() {
            BookDto.CommandBookDto bookDto = bookService.findByIsbn(VALID_ISBN);
            bookDto.setTitle("");
            bookDto.setAuthorName("");
            bookDto.setPublishYear(1);
            bookService.update(bookDto);
            BookDto.CommandBookDto newBookDto = bookService.findByIsbn(VALID_ISBN);
            assertEquals("", newBookDto.getTitle());
            assertEquals("", newBookDto.getAuthorName());
            assertEquals(1, newBookDto.getPublishYear());
        }
    }

    @Nested
    @DisplayName(value = "Delete Book")
    @Transactional
    class DeleteBookTest {

        @Test
        @DisplayName(value = "delete book should throw exception if book was not exist")
        void deleteBookShouldReturnExceptionIfBookWasNotExist() {
            Exception exception = assertThrows(EntityNotFoundException.class, () -> {
                bookService.deleteByIsbn(INVALID_ISBN);
            });
            assertEquals("error.book.isbn.not_found", exception.getMessage());
        }

        @Test
        @DisplayName(value = "delete book should throw exception if book was already borrowed")
        void deleteBookShouldReturnExceptionIfBookWasAlreadyBorrowed() {
            BorrowDto.BorrowBookDto test = BorrowDto.BorrowBookDto.builder()
                    .isbn(VALID_ISBN)
                    .borrowerName(BORROWER_NAME).build();
            borrowService.borrowBook(test);

            Exception exception = assertThrows(BookAlreadyBorrowedException.class, () -> {
                bookService.deleteByIsbn(VALID_ISBN);
            });
            assertEquals("error.book.operation.active_borrow", exception.getMessage());
        }

        @Test
        @DisplayName(value = "delete book should works if ISBN was exist and not borrowed")
        void deleteBookShouldWorksIfISBNWasExistAndNotBorrowed() {
            bookService.deleteByIsbn(VALID_ISBN);
            BookDto.CommandBookDto bookDto = bookService.findByIsbn(VALID_ISBN);
            assertTrue(bookDto.getIsDeleted());
        }
    }
}
