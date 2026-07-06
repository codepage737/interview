package com.shahrabi.interview.service.main.impl;

import com.shahrabi.interview.TestcontainersConfiguration;
import com.shahrabi.interview.service.main.BorrowService;
import com.shahrabi.interview.service.main.dto.BorrowDto;
import com.shahrabi.interview.service.main.exception.BookAlreadyBorrowedException;
import com.shahrabi.interview.service.main.exception.BookAlreadyDeletedException;
import com.shahrabi.interview.service.main.exception.BookNotBorrowedException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DisplayName(value = "Borrow Service Test")
class BorrowServiceImplTest {

    private final String VALID_ISBN = "978-964-000-005-5";
    private final String DELETED_ISBN = "978-964-000-030-0";
    private final String INVALID_ISBN = "???-???-???-???-?";
    private final String BORROWER_NAME = "soheil";

    @Autowired
    private BorrowService borrowService;

    @Autowired
    PostgreSQLContainer<?> postgresql;
    
    @Nested
    @DisplayName(value = "Borrow Specification Test")
    class BorrowSpecificationTest {

    }

    @Nested
    @DisplayName(value = "Book Borrowing Test")
    @Transactional
    class BookBorrowingTest {

        @Test
        @DisplayName(value = "borrow a book should return exception if book was not exist")
        void borrowBookShouldReturnExceptionIfBookWasNotExist() {
            BorrowDto.BorrowBookDto test = BorrowDto.BorrowBookDto.builder()
                    .isbn(INVALID_ISBN)
                    .borrowerName(BORROWER_NAME).build();
            Exception exception = assertThrows(EntityNotFoundException.class, () -> {
                borrowService.borrowBook(test);
            });
            assertEquals("error.book.isbn.not_found", exception.getMessage());
        }

        @Test
        @DisplayName(value = "borrow a book should works if book was already deleted")
        void borrowBookShouldReturnExceptionIfBookWasAlreadyDeleted() {
            BorrowDto.BorrowBookDto test = BorrowDto.BorrowBookDto.builder()
                    .isbn(DELETED_ISBN)
                    .borrowerName(BORROWER_NAME).build();
            Exception exception = assertThrows(BookAlreadyDeletedException.class, () -> {
                borrowService.borrowBook(test);
            });
            assertEquals("error.book.operation.already_deleted", exception.getMessage());
        }

        @Test
        @DisplayName(value = "borrow a book should works if book exist and borrowable")
        void borrowBookShouldWorksIfBookExistAndBorrowable() {
            BorrowDto.BorrowBookDto test = BorrowDto.BorrowBookDto.builder()
                    .isbn(VALID_ISBN)
                    .borrowerName(BORROWER_NAME).build();
            BorrowDto.CommandBorrowBookDto borrowedBookDto = borrowService.borrowBook(test);

            assertEquals(BORROWER_NAME, borrowedBookDto.getBorrowerName());
            assertNull(borrowedBookDto.getReturnDate());

            assertTrue(borrowedBookDto.getBorrowDate().isBefore(LocalDateTime.now()));
            assertTrue(borrowedBookDto.getBorrowDate().isAfter(LocalDateTime.now().minusSeconds(2)));
        }

        @Test
        @DisplayName(value = "borrow a book should return exception if book was already borrowed to some one else")
        void borrowBookShouldReturnExceptionIfBookWasAlreadyBorrowedToSomeOneElse() {
            BorrowDto.BorrowBookDto test = BorrowDto.BorrowBookDto.builder()
                    .isbn(VALID_ISBN)
                    .borrowerName(BORROWER_NAME).build();
            borrowService.borrowBook(test);

            Exception exception = assertThrows(BookAlreadyBorrowedException.class, () -> {
                borrowService.borrowBook(test);
            });
            assertEquals("error.book.operation.active_borrow", exception.getMessage());
        }
    }

    @Nested
    @DisplayName(value = "Book Return Test")
    @Transactional
    class BookReturnTest {

        @Test
        @DisplayName(value = "return book should return exception if book was not exist")
        void returnBookRhouldReturnExceptionIfBookWsNotExist() {
            Exception exception = assertThrows(EntityNotFoundException.class, () -> {
                borrowService.returnBook(INVALID_ISBN);
            });
            assertEquals("error.book.isbn.not_found", exception.getMessage());
        }

        @Test
        @DisplayName(value = "return book should works if already borrowed")
        void returnBookShouldWorksIfAlreadyBorrowed() {
            BorrowDto.BorrowBookDto test = BorrowDto.BorrowBookDto.builder()
                    .isbn(VALID_ISBN)
                    .borrowerName(BORROWER_NAME).build();
            borrowService.borrowBook(test);

            borrowService.returnBook(VALID_ISBN);
        }

        @Test
        @DisplayName(value = "return book should return exception if book was not borrowed to anyone")
        void returnBookShouldReturnExceptionIfBookWasNotBorrowedToAnyone() {
            Exception exception = assertThrows(BookNotBorrowedException.class, () -> {
                borrowService.returnBook(VALID_ISBN);
            });
            assertEquals("error.book.operation.inactive_borrow", exception.getMessage());
        }
    }
}
