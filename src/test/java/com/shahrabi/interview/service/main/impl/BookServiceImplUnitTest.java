package com.shahrabi.interview.service.main.impl;

import com.shahrabi.interview.domain.main.Book;
import com.shahrabi.interview.repository.main.BookRepository;
import com.shahrabi.interview.service.main.dto.BookDto;
import com.shahrabi.interview.service.main.exception.BookAlreadyBorrowedException;
import com.shahrabi.interview.service.main.exception.BookAlreadyDeletedException;
import com.shahrabi.interview.service.main.exception.BookNotBorrowedException;
import com.shahrabi.interview.service.main.exception.DuplicateIsbnException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName(value = "Book Service Unit Test")
class BookServiceImplUnitTest {

    private static final String VALID_ISBN = "978-964-000-005-5";
    private static final String DELETED_ISBN = "978-964-000-030-0";
    private static final String INVALID_ISBN = "???-???-???-???-?";
    private static final String NEW_ISBN = "978-964-000-111-1";

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book existingBook;
    private Book deletedBook;
    private Book borrowedBook;

    @BeforeEach
    void setUp() {
        existingBook = new Book();
        existingBook.setId(UUID.randomUUID());
        existingBook.setIsbn(VALID_ISBN);
        existingBook.setTitle("Existing Book");
        existingBook.setAuthorName("Some Author");
        existingBook.setPublishYear(2000);
        existingBook.setIsAvailable(true);
        existingBook.setIsDeleted(false);

        deletedBook = new Book();
        deletedBook.setId(UUID.randomUUID());
        deletedBook.setIsbn(DELETED_ISBN);
        deletedBook.setIsDeleted(true);
        deletedBook.setIsAvailable(true);

        borrowedBook = new Book();
        borrowedBook.setId(UUID.randomUUID());
        borrowedBook.setIsbn(DELETED_ISBN);
        borrowedBook.setIsAvailable(false);
        borrowedBook.setIsDeleted(false);
    }

    @Nested
    @DisplayName(value = "Save New Book Test")
    class SaveNewBookTest {

        @Test
        @DisplayName(value = "create book should throw exception if ISBN already exists")
        void createBookShouldThrowIfISBNAlreadyExists() {
            BookDto.CommandBookDto request = BookDto.CommandBookDto.builder()
                    .title("fake book")
                    .authorName("Soheil")
                    .publishYear(1999)
                    .isbn(NEW_ISBN)
                    .build();

            when(bookRepository.existsByIsbn(NEW_ISBN)).thenReturn(true);

            Exception exception = assertThrows(DuplicateIsbnException.class,
                    () -> bookService.saveBook(request));

            assertEquals("error.book.isbn.duplicate", exception.getMessage());
            verify(bookRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName(value = "Update Book Test")
    class UpdateBookTest {

        @Test
        @DisplayName(value = "update book should throw exception if ISBN does not exist")
        void updateBookShouldThrowIfBookDoesNotExist() {
            BookDto.CommandBookDto request = BookDto.CommandBookDto.builder()
                    .isbn(NEW_ISBN)
                    .title("fake book")
                    .build();

            when(bookRepository.findByIsbn(NEW_ISBN)).thenReturn(Optional.empty());

            Exception exception = assertThrows(EntityNotFoundException.class,
                    () -> bookService.update(request));

            assertEquals("error.book.isbn.not_found", exception.getMessage());
            verify(bookRepository, never()).save(any());
        }

        @Test
        @DisplayName(value = "update book should throw exception if ISBN was already deleted")
        void updateBookShouldThrowIfAlreadyDeleted() {
            BookDto.CommandBookDto request = BookDto.CommandBookDto.builder()
                    .isbn(DELETED_ISBN)
                    .title("")
                    .build();

            when(bookRepository.findByIsbn(DELETED_ISBN)).thenReturn(Optional.of(deletedBook));

            Exception exception = assertThrows(BookAlreadyDeletedException.class,
                    () -> bookService.update(request));

            assertEquals("error.book.operation.already_deleted", exception.getMessage());
            verify(bookRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName(value = "Delete Book Test")
    class DeleteBookTest {

        @Test
        @DisplayName(value = "delete book should throw exception if book does not exist")
        void deleteBookShouldThrowIfBookDoesNotExist() {
            when(bookRepository.findByIsbn(INVALID_ISBN)).thenReturn(Optional.empty());

            Exception exception = assertThrows(EntityNotFoundException.class,
                    () -> bookService.deleteByIsbn(INVALID_ISBN));

            assertEquals("error.book.isbn.not_found", exception.getMessage());
        }

        @Test
        @DisplayName(value = "delete book should throw exception if book is already borrowed")
        void deleteBookShouldThrowIfAlreadyBorrowed() {
            when(bookRepository.findByIsbn(VALID_ISBN)).thenReturn(Optional.of(borrowedBook));

            Exception exception = assertThrows(BookAlreadyBorrowedException.class,
                    () -> bookService.deleteByIsbn(VALID_ISBN));

            assertEquals("error.book.operation.active_borrow", exception.getMessage());
        }
    }

    @Nested
    @DisplayName(value = "Mark Book As Unavailable Test")
    class MarkUnavailableTest {

        @Test
        @DisplayName(value = "mark book as unavailable should throw exception if book does not exist")
        void deleteBookShouldThrowIfBookDoesNotExist() {
            when(bookRepository.findByIsbn(INVALID_ISBN)).thenReturn(Optional.empty());

            Exception exception = assertThrows(EntityNotFoundException.class,
                    () -> bookService.markBookAsUnAvailableAndReturn(INVALID_ISBN));

            assertEquals("error.book.isbn.not_found", exception.getMessage());
        }

        @Test
        @DisplayName(value = "mark book as unavailable should throw exception if book is already borrowed")
        void deleteBookShouldThrowIfAlreadyBorrowed() {
            when(bookRepository.findByIsbn(VALID_ISBN)).thenReturn(Optional.of(borrowedBook));

            Exception exception = assertThrows(BookAlreadyBorrowedException.class,
                    () -> bookService.markBookAsUnAvailableAndReturn(VALID_ISBN));

            assertEquals("error.book.operation.active_borrow", exception.getMessage());
        }
    }

    @Nested
    @DisplayName(value = "Mark Book As Available Test")
    class MarkAvailableTest {

        @Test
        @DisplayName(value = "mark book as available should throw exception if book does not exist")
        void markBookAsAvailableShouldThrowExceptionIfBookDoesNotExist() {
            when(bookRepository.findByIsbn(INVALID_ISBN)).thenReturn(Optional.empty());

            Exception exception = assertThrows(EntityNotFoundException.class,
                    () -> bookService.markBookAsAvailableAndReturn(INVALID_ISBN));

            assertEquals("error.book.isbn.not_found", exception.getMessage());
        }

        @Test
        @DisplayName(value = "mark book as available should throw exception if book is already borrowed")
        void markBookAsAvailableShouldThrowExceptionIfBookIsAlreadyBorrowed() {
            when(bookRepository.findByIsbn(VALID_ISBN)).thenReturn(Optional.of(existingBook));

            Exception exception = assertThrows(BookNotBorrowedException.class,
                    () -> bookService.markBookAsAvailableAndReturn(VALID_ISBN));

            assertEquals("error.book.operation.inactive_borrow", exception.getMessage());
        }
    }
}