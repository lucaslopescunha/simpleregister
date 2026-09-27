package br.com.cunha.registrousuarios.domain;

public record Pessoa(Long id, String nome, String cpf, String endereço) {

    public Pessoa {
        validateNome(nome);
        validateCpf(cpf);
        validateEndereco(endereço);
    }

    public static void validateNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("nome must not be blank");
        }
    }

    public static void validateCpf(String cpf) {
        if (cpf == null) {
            throw new IllegalArgumentException("cpf is required");
        }
    }

    public static void validateEndereco(String endereço) {
        if (endereço == null || endereço.isBlank()) {
            throw new IllegalArgumentException("endereço must not be blank");
        }
    }
}