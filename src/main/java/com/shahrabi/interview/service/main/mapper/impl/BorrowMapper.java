package com.shahrabi.interview.service.main.mapper.impl;

import com.shahrabi.interview.domain.main.Borrow;
import com.shahrabi.interview.service.main.dto.BorrowDto;
import com.shahrabi.interview.service.main.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BorrowMapper extends BaseMapper<Borrow, BorrowDto.CommandBorrowBookDto> {
    @Override
    @Mapping(target = "isbn", source = "book.isbn")
    @Mapping(target = "title", source = "book.title")
    BorrowDto.CommandBorrowBookDto toDto(Borrow borrow);

    @Override
    @Mapping(target = "id", ignore = true)
    Borrow toEntity(BorrowDto.CommandBorrowBookDto commandBorrowBookDto);

    @Override
    @Mapping(target = "isbn", source = "book.isbn")
    @Mapping(target = "title", source = "book.title")
    List<BorrowDto.CommandBorrowBookDto> toDtoList(List<Borrow> entities);

    @Override
    @Mapping(target = "id", ignore = true)
    List<Borrow> toEntityList(List<BorrowDto.CommandBorrowBookDto> dtos);

    @Override
    void update(@MappingTarget Borrow borrow, BorrowDto.CommandBorrowBookDto commandBorrowBookDto);

    @Mapping(target = "isbn", source = "book.isbn")
    @Mapping(target = "title", source = "book.title")
    BorrowDto.ReportBorrowDto toReportDto(Borrow entity);
}
