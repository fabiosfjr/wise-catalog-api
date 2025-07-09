package com.first.wisecatalogapi.infrastructure.cache;

import com.first.wisecatalogapi.application.ports.VisualizacaoLivroOutputPort;
import com.first.wisecatalogapi.domain.entities.LivroEntity;
import com.first.wisecatalogapi.infrastructure.databse.LivroJpaRepository;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Component
public class RedisVisualizacaoLivroCache implements VisualizacaoLivroOutputPort {

    private static final String PREFIXO_CHAVE = "recentes:";
    private static final int LIMITE_VISUALIZACOES = 5;

    private final RedisTemplate<String, Long> redisTemplate;
    private final ListOperations<String, Long> listOperations;
    private final LivroJpaRepository livroJpaRepository;

    public RedisVisualizacaoLivroCache(RedisTemplate<String, Long> redisTemplate,
                                       LivroJpaRepository livroJpaRepository) {
        this.redisTemplate = redisTemplate;
        this.listOperations = redisTemplate.opsForList();
        this.livroJpaRepository = livroJpaRepository;
    }

    @Override
    public void registrarVisualizacao(String sessionId,
                                      Long livroId) {
        String chave = PREFIXO_CHAVE + sessionId;

        listOperations.remove(chave, 0, livroId);
        listOperations.leftPush(chave, livroId);
        listOperations.trim(chave, 0, LIMITE_VISUALIZACOES - 1);
        redisTemplate.expire(chave, Duration.ofHours(1));
    }

    @Override
    public List<Long> buscarVisualizadosRecentemente(String sessionId) {
        String chave = PREFIXO_CHAVE + sessionId;
        return listOperations.range(chave, 0, LIMITE_VISUALIZACOES - 1);
    }

    @Override
    public List<LivroEntity> buscarLivrosPorIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        return livroJpaRepository.findAllById(ids);
    }
}
