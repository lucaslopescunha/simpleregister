package br.com.cunha.registrousuarios.application;

public class PessoaNaoEncontradaException extends RuntimeException {

    public PessoaNaoEncontradaException(Long id) {
        super("Pessoa não encontrada: " + id);
    }
}