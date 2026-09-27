package br.com.cunha.registrousuarios.application;

import br.com.cunha.registrousuarios.domain.Pessoa;

import java.util.List;

public class ListarPessoasUseCase {

    private final PessoaRepository repository;

    public ListarPessoasUseCase(PessoaRepository repository) {
        this.repository = repository;
    }

    public List<Pessoa> executar() {
        return repository.findAll().stream()
                .sorted((primeira, segunda) -> primeira.id().compareTo(segunda.id()))
                .toList();
    }
}