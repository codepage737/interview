package com.shahrabi.interview.service.main.impl;

import com.shahrabi.interview.TestcontainersConfiguration;
import com.shahrabi.interview.service.main.LoanService;
import com.shahrabi.interview.service.main.dto.LoanDto;
import com.shahrabi.interview.service.main.exception.BookAlreadyBorrowedException;
import com.shahrabi.interview.service.main.exception.BookNotBorrowedException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DisplayName(value = "Loan Service Test")
class LoanServiceImplTest {

    @Autowired
    private LoanService loanService;

    @Autowired
    PostgreSQLContainer<?> postgresql;
    
    @Nested
    @DisplayName(value = "Loan Specification Test")
    class LoanSpecificationTest {

    }

    @Nested
    @DisplayName(value = "Book Borrowing Test")
    class BookBorrowingTest {

        private final String VALID_ISBN1 = "978-964-000-005-5";
        private final String VALID_ISBN2 = "978-964-000-006-6";
        private final String INVALID_ISBN = "???-???-???-???-?";
        private final String BORROWER_NAME = "soheil";

        @Test
        @DisplayName(value = "borrow a book should return exception if book was not exist")
        void borrowBookShouldReturnExceptionIfBookWasNotExist() {
            LoanDto.BorrowBookDto test = LoanDto.BorrowBookDto.builder()
                    .isbn(INVALID_ISBN)
                    .borrowerName(BORROWER_NAME).build();
            Exception exception = assertThrows(EntityNotFoundException.class, () -> {
                loanService.borrowBook(test);
            });
            assertEquals("error.book.isbn.not_found", exception.getMessage());
        }

        @Test
        @DisplayName(value = "borrow a book should works if book exist and borrowable")
        void borrowBookShouldWorksIfBookExistAndBorrowable() {
            LoanDto.BorrowBookDto test = LoanDto.BorrowBookDto.builder()
                    .isbn(VALID_ISBN1)
                    .borrowerName(BORROWER_NAME).build();
            LoanDto.CommandLoanBookDto loanBookDto = loanService.borrowBook(test);

            assertEquals(BORROWER_NAME, loanBookDto.getBorrowerName());
            assertNull(loanBookDto.getReturnDate());

            assertTrue(loanBookDto.getLoanDate().isBefore(LocalDateTime.now()));
            assertTrue(loanBookDto.getLoanDate().isAfter(LocalDateTime.now().minusSeconds(2)));
        }

        @Test
        @DisplayName(value = "borrow a book should return exception if book was already borrowed to some one else")
        void borrowBookShouldReturnExceptionIfBookWasAlreadyBorrowedToSomeOneElse() {
            LoanDto.BorrowBookDto test = LoanDto.BorrowBookDto.builder()
                    .isbn(VALID_ISBN2)
                    .borrowerName(BORROWER_NAME).build();
            loanService.borrowBook(test);

            Exception exception = assertThrows(BookAlreadyBorrowedException.class, () -> {
                loanService.borrowBook(test);
            });
            assertEquals("error.book.operation.active_loan", exception.getMessage());
        }
    }

    @Nested
    @DisplayName(value = "Book Return Test")
    class BookReturnTest {
        private final String VALID_ISBN1 = "978-964-000-011-1";
        private final String VALID_ISBN2 = "978-964-000-012-2";
        private final String INVALID_ISBN = "???-???-???-???-?";
        private final String BORROWER_NAME = "soheil";

        @Test
        @DisplayName(value = "return book should return exception if book was not exist")
        void returnBookRhouldReturnExceptionIfBookWsNotExist() {
            Exception exception = assertThrows(EntityNotFoundException.class, () -> {
                loanService.returnBook(INVALID_ISBN);
            });
            assertEquals("error.book.isbn.not_found", exception.getMessage());
        }

        @Test
        @DisplayName(value = "return book should works if already borrowed")
        void returnBookShouldWorksIfAlreadyBorrowed() {
            LoanDto.BorrowBookDto test = LoanDto.BorrowBookDto.builder()
                    .isbn(VALID_ISBN1)
                    .borrowerName(BORROWER_NAME).build();
            LoanDto.CommandLoanBookDto loanBookDto = loanService.borrowBook(test);

            loanService.returnBook(VALID_ISBN1);
        }

        @Test
        @DisplayName(value = "return book should return exception if book was not borrowed to anyone")
        void returnBookShouldReturnExceptionIfBookWasNotBorrowedToAnyone() {
            Exception exception = assertThrows(BookNotBorrowedException.class, () -> {
                loanService.returnBook(VALID_ISBN2);
            });
            assertEquals("error.book.operation.inactive_loan", exception.getMessage());
        }
    }
}
