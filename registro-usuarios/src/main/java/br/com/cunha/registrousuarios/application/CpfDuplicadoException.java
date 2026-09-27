package br.com.cunha.registrousuarios.application;

public class CpfDuplicadoException extends RuntimeException {

    public CpfDuplicadoException() {
        super("CPF já cadastrado");
    }
}