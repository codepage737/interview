package com.shahrabi.interview.service.main.mapper.impl;

import com.shahrabi.interview.domain.main.Loan;
import com.shahrabi.interview.service.main.dto.LoanDto;
import com.shahrabi.interview.service.main.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanMapper extends BaseMapper<Loan, LoanDto.CommandLoanBookDto> {
    @Override
    LoanDto.CommandLoanBookDto toDto(Loan loan);

    @Override
    Loan toEntity(LoanDto.CommandLoanBookDto commandLoanBookDto);

    @Override
    List<LoanDto.CommandLoanBookDto> toDtoList(List<Loan> entities);

    @Override
    List<Loan> toEntityList(List<LoanDto.CommandLoanBookDto> dtos);

    @Override
    void update(@MappingTarget Loan loan, LoanDto.CommandLoanBookDto commandLoanBookDto);

    @Mapping(target = "isbn", source = "book.isbn")
    @Mapping(target = "title", source = "book.title")
    LoanDto.ReportLoansDto toReportDto(Loan entity);
}
