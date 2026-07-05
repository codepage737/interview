package com.shahrabi.interview.service.main.mapper.impl;

import com.shahrabi.interview.domain.main.Book;
import com.shahrabi.interview.service.main.dto.BookDto;
import com.shahrabi.interview.service.main.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper extends BaseMapper<Book, BookDto.CommandBookDto> {
    @Override
    BookDto.CommandBookDto toDto(Book book);

    @Override
    @Mapping(target = "id", ignore = true)
    Book toEntity(BookDto.CommandBookDto commandBookDto);

    @Override
    List<BookDto.CommandBookDto> toDtoList(List<Book> entities);

    @Override
    List<Book> toEntityList(List<BookDto.CommandBookDto> dtos);

    @Override
    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget Book book, BookDto.CommandBookDto commandBookDto);

    BookDto.ReportingBookDto toReportDto(Book entity);
}
