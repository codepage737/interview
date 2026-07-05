package com.shahrabi.interview.service.main.mapper.impl;

import com.shahrabi.interview.domain.main.Book;
import com.shahrabi.interview.service.main.dto.BookDto;
import com.shahrabi.interview.service.main.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper extends BaseMapper<Book, BookDto.CommandBookDto> {
    @Override
    BookDto.CommandBookDto toDto(Book book);

    @Override
    Book toEntity(BookDto.CommandBookDto commandBookDto);

    @Override
    List<BookDto.CommandBookDto> toDtoList(List<Book> entities);

    @Override
    List<Book> toEntityList(List<BookDto.CommandBookDto> dtos);

    @Override
    void update(@MappingTarget Book book, BookDto.CommandBookDto commandBookDto);

    BookDto.ReportingBookDto toReportDto(Book entity);
}
