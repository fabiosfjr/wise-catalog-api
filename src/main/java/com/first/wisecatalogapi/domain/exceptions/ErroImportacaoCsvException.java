package com.first.wisecatalogapi.domain.exceptions;

public class ErroImportacaoCsvException extends RuntimeException {
    public ErroImportacaoCsvException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
