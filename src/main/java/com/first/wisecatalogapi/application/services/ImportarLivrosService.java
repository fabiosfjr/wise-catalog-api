package com.first.wisecatalogapi.application.services;

import com.first.wisecatalogapi.application.usecases.ImportarLivrosCsvUseCase;
import com.first.wisecatalogapi.domain.entities.LivroEntity;
import com.first.wisecatalogapi.domain.exceptions.ErroImportacaoCsvException;
import com.first.wisecatalogapi.infrastructure.databse.LivroJpaRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class ImportarLivrosService implements ImportarLivrosCsvUseCase {

    private final LivroJpaRepository livroJpaRepository;

    public ImportarLivrosService(LivroJpaRepository livroJpaRepository) {
        this.livroJpaRepository = livroJpaRepository;
    }

    @Override
    public void importarLivrosCsv(MultipartFile arquivo) {
        try (var reader = new CSVReader(new InputStreamReader(arquivo.getInputStream()))) {
            String[] linha;
            reader.readNext();
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
        } catch (IOException | CsvValidationException exception) {
            throw new ErroImportacaoCsvException("Erro ao importar o arquivo CSV", exception);
        }
    }
}
