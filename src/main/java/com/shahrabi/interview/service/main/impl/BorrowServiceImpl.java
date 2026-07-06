package com.shahrabi.interview.service.main.impl;

import com.shahrabi.interview.common.http.PagedResponseDto;
import com.shahrabi.interview.domain.main.Borrow;
import com.shahrabi.interview.repository.main.BorrowRepository;
import com.shahrabi.interview.service.main.BookService;
import com.shahrabi.interview.service.main.BorrowService;
import com.shahrabi.interview.service.main.dto.BookDto;
import com.shahrabi.interview.service.main.dto.BorrowDto;
import com.shahrabi.interview.service.main.mapper.impl.BorrowMapper;
import com.shahrabi.interview.service.main.specification.BorrowSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {

    private final BorrowMapper mapper;
    private final BorrowRepository repository;
    private final BookService bookService;
    private final BorrowSpecification specification;

    @Override
    public PagedResponseDto<BorrowDto.ReportBorrowDto> findAll(BorrowDto.QueryBorrowDto query, Pageable pageable) {
        Specification<Borrow> allSpecifications = specification.getAllSpecifications(query);
        Page<Borrow> all = repository.findAll(allSpecifications, pageable);
        Page<BorrowDto.ReportBorrowDto> map = all.map(mapper::toReportDto);
        return new PagedResponseDto<>(map);
    }

    @Override
    public BorrowDto.CommandBorrowBookDto findById(UUID id) {
        Borrow borrow = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("error.borrow.not_found"));
        return mapper.toDto(borrow);
    }

    @Override
    @Transactional
    public BorrowDto.CommandBorrowBookDto borrowBook(BorrowDto.BorrowBookDto dto) {
        BookDto.CommandBookDto book = bookService.markBookAsUnAvailableAndReturn(dto.getIsbn());
        BorrowDto.CommandBorrowBookDto borrowedBookDto = BorrowDto.CommandBorrowBookDto.builder()
                .bookId(book.getId())
                .isbn(dto.getIsbn())
                .title(book.getTitle())
                .borrowerName(dto.getBorrowerName())
                .borrowDate(LocalDateTime.now()).build();
        Borrow entity = mapper.toEntity(borrowedBookDto);
        Borrow save = repository.save(entity);
        borrowedBookDto.setId(save.getId());
        return borrowedBookDto;
    }

    @Override
    @Transactional
    public Boolean returnBook(String isbn) {
        BookDto.CommandBookDto bookDto = bookService.markBookAsAvailableAndReturn(isbn);
        Optional<Borrow> borrowedBookOptional = repository.findFirstByBookIdAndReturnDateIsNullOrderByBorrowDateDesc(bookDto.getId());
        if (borrowedBookOptional.isPresent()) {
            Borrow borrowedBook = borrowedBookOptional.get();
            borrowedBook.setReturnDate(LocalDateTime.now());
            bookDto.setIsAvailable(Boolean.TRUE);
            return Boolean.TRUE;
        } else {
            throw new RuntimeException();
        }
    }
}
