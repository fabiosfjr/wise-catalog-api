package com.first.wisecatalogapi.adapters.output.repositories;

import com.first.wisecatalogapi.domain.entities.LivroEntity;
import com.first.wisecatalogapi.domain.repositories.LivroRepository;
import com.first.wisecatalogapi.infrastructure.databse.LivroJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LivroRepositoryImpl implements LivroRepository {

    private final LivroJpaRepository livroJpaRepository;

    public LivroRepositoryImpl(LivroJpaRepository livroJpaRepository) {
        this.livroJpaRepository = livroJpaRepository;
    }

    @Override
    public Page<LivroEntity> listarTodosLivros(Pageable pageable) {
        return livroJpaRepository.findAll(pageable);
    }

    @Override
    public Optional<LivroEntity> buscarLivroPorId(Long id) {
        return livroJpaRepository.findById(id);
    }

    @Override
    public List<LivroEntity> buscarLivrosPorAutor(String autor) {
        return livroJpaRepository.findByAutor(autor);
    }

    @Override
    public List<LivroEntity> buscarLivrosPorGenero(String genero) {
        return livroJpaRepository.findByGenero(genero);
    }
}
