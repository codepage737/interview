package com.shahrabi.interview.service.main.mapper.impl;

import com.shahrabi.interview.domain.main.Loan;
import com.shahrabi.interview.service.main.dto.LoanDto;
import com.shahrabi.interview.service.main.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanMapper extends BaseMapper<Loan, LoanDto> {
    @Override
    LoanDto toDto(Loan loan);

    @Override
    Loan toEntity(LoanDto loanDto);

    @Override
    List<LoanDto> toDtoList(List<Loan> entities);

    @Override
    List<Loan> toEntityList(List<LoanDto> dtos);

    @Override
    void update(@MappingTarget Loan loan, LoanDto loanDto);
}
