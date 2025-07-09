package com.first.wisecatalogapi.application.services;

import com.first.wisecatalogapi.application.dto.LivroDTO;
import com.first.wisecatalogapi.application.usecases.BuscarLivroUseCase;
import com.first.wisecatalogapi.domain.exceptions.LivroNaoEncontradoPeloAutorException;
import com.first.wisecatalogapi.domain.exceptions.LivroNaoEncontradoPeloGeneroException;
import com.first.wisecatalogapi.domain.exceptions.LivroNaoEncontradoPeloIdException;
import com.first.wisecatalogapi.domain.mapper.LivroMapper;
import com.first.wisecatalogapi.domain.repositories.LivroRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuscarLivroService implements BuscarLivroUseCase {

    private final LivroRepository livroRepository;
    private final LivroMapper livroMapper;

    public BuscarLivroService(LivroRepository livroRepository,
                              LivroMapper livroMapper) {
        this.livroRepository = livroRepository;
        this.livroMapper = livroMapper;
    }

    @Cacheable(value = "livroPorId", key = "#id")
    @Override
    public LivroDTO buscarPorId(Long id) {
        return livroRepository.buscarLivroPorId(id)
                .map(livroMapper::toDTO)
                .orElseThrow(() -> new LivroNaoEncontradoPeloIdException(id));
    }

    @Cacheable(value = "livros")
    @Override
    public Page<LivroDTO> listarTodosLivros(Pageable pageable) {
        return livroRepository.listarTodosLivros(pageable)
                .map(livroMapper::toDTO);
    }

    @Cacheable(value = "livrosPorGenero", key = "#genero")
    @Override
    public List<LivroDTO> buscarPorGenero(String genero) {
        var livros = livroRepository.buscarLivrosPorGenero(genero);

        if (livros.isEmpty()) {
            throw new LivroNaoEncontradoPeloGeneroException(genero);
        }

        return livros.stream()
                .map(livroMapper::toDTO)
                .toList();
    }

    @Cacheable(value = "livrosPorAutor", key = "#autor")
    @Override
    public List<LivroDTO> buscarPorAutor(String autor) {
        var livros = livroRepository.buscarLivrosPorAutor(autor);

        if (livros.isEmpty()) {
            throw new LivroNaoEncontradoPeloAutorException(autor);
        }

        return livros.stream()
                .map(livroMapper::toDTO)
                .toList();
    }
}
