package com.first.wisecatalogapi.domain.mapper;

import com.first.wisecatalogapi.application.dto.LivroDTO;
import com.first.wisecatalogapi.domain.entities.LivroEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LivroMapper {
    LivroDTO toDTO(LivroEntity entity);
}
