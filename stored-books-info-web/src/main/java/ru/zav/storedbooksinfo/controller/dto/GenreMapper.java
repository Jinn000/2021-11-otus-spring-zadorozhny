package ru.zav.storedbooksinfo.controller.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.zav.storedbooksinfo.domain.Genre;

@Mapper
public interface GenreMapper {
    GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);

    GenreDto toDto(Genre entity);

    Genre toEntity(GenreDto dto);

}