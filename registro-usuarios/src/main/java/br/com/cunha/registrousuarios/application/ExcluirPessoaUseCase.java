package br.com.cunha.registrousuarios.application;

public class ExcluirPessoaUseCase {

    private final PessoaRepository repository;

    public ExcluirPessoaUseCase(PessoaRepository repository) {
        this.repository = repository;
    }

    public void executar(Long id) {
        if (repository.findById(id).isEmpty()) {
            throw new PessoaNaoEncontradaException(id);
        }
        repository.deleteById(id);
    }
}