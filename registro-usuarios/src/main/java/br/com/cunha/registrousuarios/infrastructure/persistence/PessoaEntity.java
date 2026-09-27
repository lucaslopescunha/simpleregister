package br.com.cunha.registrousuarios.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pessoas")
public class PessoaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(name = "endereco", nullable = false)
    private String endereço;

    protected PessoaEntity() {
    }

    public PessoaEntity(Long id, String nome, String cpf, String endereço) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.endereço = endereço;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEndereço() {
        return endereço;
    }
}