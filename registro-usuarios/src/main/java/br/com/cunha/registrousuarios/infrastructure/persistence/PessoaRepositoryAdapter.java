package br.com.cunha.registrousuarios.infrastructure.persistence;

import br.com.cunha.registrousuarios.application.PessoaRepository;
import br.com.cunha.registrousuarios.domain.Pessoa;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PessoaRepositoryAdapter implements PessoaRepository {

    private final SpringDataPessoaRepository repository;

    public PessoaRepositoryAdapter(SpringDataPessoaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Pessoa save(Pessoa pessoa) {
        PessoaEntity entity = new PessoaEntity(pessoa.id(), pessoa.nome(), pessoa.cpf(), pessoa.endereço());
        PessoaEntity salva = repository.save(entity);
        return toDomain(salva);
    }

    @Override
    public Optional<Pessoa> findById(Long id) {
        return repository.findById(id).map(PessoaRepositoryAdapter::toDomain);
    }

    @Override
    public Optional<Pessoa> findByCpf(String cpf) {
        return repository.findByCpf(cpf).map(PessoaRepositoryAdapter::toDomain);
    }

    @Override
    public List<Pessoa> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(PessoaRepositoryAdapter::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private static Pessoa toDomain(PessoaEntity entity) {
        return new Pessoa(entity.getId(), entity.getNome(), entity.getCpf(), entity.getEndereço());
    }
}