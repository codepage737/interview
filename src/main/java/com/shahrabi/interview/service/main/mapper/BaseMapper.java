package com.shahrabi.interview.service.main.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

public interface BaseMapper<E, D> {
    D toDto(E e);

    E toEntity(D d);

    List<D> toDtoList(List<E> entities);

    List<E> toEntityList(List<D> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget E e, D d);
}
