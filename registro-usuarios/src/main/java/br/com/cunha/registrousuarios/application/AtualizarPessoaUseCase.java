package br.com.cunha.registrousuarios.application;

import br.com.cunha.registrousuarios.domain.Pessoa;

public class AtualizarPessoaUseCase {

    private final PessoaRepository repository;

    public AtualizarPessoaUseCase(PessoaRepository repository) {
        this.repository = repository;
    }

    public Pessoa executar(Long id, PatchPessoa patch) {
        if (patch == null || patch.vazio()) {
            throw new IllegalArgumentException("PATCH must contain at least one field");
        }
        Pessoa atual = repository.findById(id).orElseThrow(() -> new PessoaNaoEncontradaException(id));
        String nome = patch.nomeInformado() ? patch.nome() : atual.nome();
        String cpf = patch.cpfInformado() ? patch.cpf() : atual.cpf();
        String endereço = patch.enderecoInformado() ? patch.endereço() : atual.endereço();
        Pessoa atualizada = new Pessoa(id, nome, cpf, endereço);
        repository.findByCpf(cpf).filter(pessoa -> !pessoa.id().equals(id))
                .ifPresent(pessoa -> {
                    throw new CpfDuplicadoException();
                });
        return repository.save(atualizada);
    }
}