package br.com.cunha.registrousuarios.application;

import br.com.cunha.registrousuarios.domain.Pessoa;

import java.util.List;
import java.util.Optional;

public interface PessoaRepository {

    Pessoa save(Pessoa pessoa);

    Optional<Pessoa> findById(Long id);

    Optional<Pessoa> findByCpf(String cpf);

    List<Pessoa> findAll();

    void deleteById(Long id);
}