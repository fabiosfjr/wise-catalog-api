package com.first.wisecatalogapi.application.usecases;

import org.springframework.web.multipart.MultipartFile;

public interface ImportarLivrosCsvUseCase {
    void importarLivrosCsv(MultipartFile file);
}
