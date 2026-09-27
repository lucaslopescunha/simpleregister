package br.com.cunha.registrousuarios.application;

import br.com.cunha.registrousuarios.domain.Pessoa;

public class BuscarPessoaPorIdUseCase {

    private final PessoaRepository repository;

    public BuscarPessoaPorIdUseCase(PessoaRepository repository) {
        this.repository = repository;
    }

    public Pessoa executar(Long id) {
        return repository.findById(id).orElseThrow(() -> new PessoaNaoEncontradaException(id));
    }
}