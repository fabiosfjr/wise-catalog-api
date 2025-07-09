package com.first.wisecatalogapi.application.services;

import com.first.wisecatalogapi.application.usecases.ImportarLivrosCSVUseCase;
import com.first.wisecatalogapi.domain.entities.LivroEntity;
import com.first.wisecatalogapi.infrastructure.databse.LivroJpaRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Service
public class ImportarLivrosService implements ImportarLivrosCSVUseCase {

    private final LivroJpaRepository livroJpaRepository;

    public ImportarLivrosService(LivroJpaRepository livroJpaRepository) {
        this.livroJpaRepository = livroJpaRepository;
    }

    @Override
    public void importarLivrosCsv(String caminhoArquivoCsv) throws FileNotFoundException {
        try (CSVReader reader = new CSVReader(new FileReader(caminhoArquivoCsv))) {
            String[] linha;
            reader.readNext(); // Ignorar cabeçalho
            while ((linha = reader.readNext()) != null) {
                var livro = new LivroEntity();
                livro.setId(Long.parseLong(linha[0]));
                livro.setTitulo(linha[1]);
                livro.setAutor(linha[2]);
                livro.setGenero(linha[3]);
                livro.setSubGenero(linha[4]);
                livro.setTipo(linha[5]);
                livro.setPreco(Double.parseDouble(linha[6].replace(",", "")));
                livro.setAvaliacao(Double.parseDouble(linha[7]));
                livro.setNumeroAvaliacoes((int) Double.parseDouble(linha[8]));
                livro.setUrl(linha[9]);
                livroJpaRepository.save(livro);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
