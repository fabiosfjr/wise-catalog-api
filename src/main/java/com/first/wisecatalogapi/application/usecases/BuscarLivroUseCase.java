package com.first.wisecatalogapi.application.usecases;

import com.first.wisecatalogapi.application.dto.LivroDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BuscarLivroUseCase {
    LivroDTO buscarPorId(Long id);
    Page<LivroDTO> listarTodosLivros(Pageable pageable);
    List<LivroDTO> buscarPorGenero(String genero);
    List<LivroDTO> buscarPorAutor(String autor);
}
