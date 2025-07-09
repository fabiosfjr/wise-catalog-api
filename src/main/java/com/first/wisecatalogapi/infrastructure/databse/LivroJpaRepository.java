package com.first.wisecatalogapi.infrastructure.databse;

import com.first.wisecatalogapi.domain.entities.LivroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroJpaRepository extends JpaRepository<LivroEntity, Long> {
    List<LivroEntity> findByGenero(String genero);
    List<LivroEntity> findByAutor(String autor);
}
