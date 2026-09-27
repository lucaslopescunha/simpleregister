package br.com.cunha.registrousuarios.application;

public record PatchPessoa(
        boolean nomeInformado,
        String nome,
        boolean cpfInformado,
        String cpf,
        boolean enderecoInformado,
        String endereço) {

    public boolean vazio() {
        return !nomeInformado && !cpfInformado && !enderecoInformado;
    }
}