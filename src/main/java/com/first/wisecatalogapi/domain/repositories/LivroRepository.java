package com.first.wisecatalogapi.domain.repositories;

import com.first.wisecatalogapi.domain.entities.LivroEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LivroRepository {
    Page<LivroEntity> listarTodosLivros(Pageable pageable);
    Optional<LivroEntity> buscarLivroPorId(Long id);
    List<LivroEntity> buscarLivrosPorAutor(String autor);
    List<LivroEntity> buscarLivrosPorGenero(String genero);
}
