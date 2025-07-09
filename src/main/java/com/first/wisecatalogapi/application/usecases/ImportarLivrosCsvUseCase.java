package com.first.wisecatalogapi.application.usecases;

import java.io.FileNotFoundException;

public interface ImportarLivrosCsvUseCase {
    void importarLivrosCsv(String caminhoArquivoCsv) throws FileNotFoundException;
}
