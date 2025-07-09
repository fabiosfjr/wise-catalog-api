package com.first.wisecatalogapi.application.usecases;

import java.io.FileNotFoundException;

public interface ImportarLivrosCSVUseCase {
    void importarLivrosCsv(String caminhoArquivoCsv) throws FileNotFoundException;
}
