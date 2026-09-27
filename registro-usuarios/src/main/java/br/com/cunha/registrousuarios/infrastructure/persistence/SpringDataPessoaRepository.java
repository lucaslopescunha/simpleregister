package br.com.cunha.registrousuarios.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataPessoaRepository extends JpaRepository<PessoaEntity, Long> {

    Optional<PessoaEntity> findByCpf(String cpf);
}