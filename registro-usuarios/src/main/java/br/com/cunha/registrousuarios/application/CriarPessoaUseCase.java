package br.com.cunha.registrousuarios.application;

import br.com.cunha.registrousuarios.domain.Pessoa;

public class CriarPessoaUseCase {

    private final PessoaRepository repository;

    public CriarPessoaUseCase(PessoaRepository repository) {
        this.repository = repository;
    }

    public Pessoa executar(String nome, String cpf, String endereço) {
        Pessoa novaPessoa = new Pessoa(null, nome, cpf, endereço);
        if (repository.findByCpf(cpf).isPresent()) {
            throw new CpfDuplicadoException();
        }
        return repository.save(novaPessoa);
    }
}