package br.com.cunha.registrousuarios.application;

import br.com.cunha.registrousuarios.domain.Pessoa;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PessoaUseCasesTests {

    @Test
    void createsPersonWithoutValidatingCpfFormat() {
        InMemoryPessoaRepository repository = new InMemoryPessoaRepository();

        Pessoa criada = new CriarPessoaUseCase(repository).executar("Ana", "cpf-informal", "Rua A");

        assertEquals(new Pessoa(1L, "Ana", "cpf-informal", "Rua A"), criada);
    }

    @Test
    void rejectsDuplicateCpf() {
        InMemoryPessoaRepository repository = new InMemoryPessoaRepository();
        CriarPessoaUseCase useCase = new CriarPessoaUseCase(repository);
        useCase.executar("Ana", "same", "Rua A");

        assertThrows(CpfDuplicadoException.class, () -> useCase.executar("Bia", "same", "Rua B"));
    }

    @Test
    void patchPreservesOmittedFieldsAndRejectsNullValues() {
        InMemoryPessoaRepository repository = new InMemoryPessoaRepository();
        Pessoa original = new CriarPessoaUseCase(repository).executar("Ana", "cpf", "Rua A");
        AtualizarPessoaUseCase useCase = new AtualizarPessoaUseCase(repository);

        Pessoa atualizada = useCase.executar(original.id(), new PatchPessoa(true, "Bia", false, null, false, null));

        assertEquals(new Pessoa(original.id(), "Bia", "cpf", "Rua A"), atualizada);
        assertThrows(IllegalArgumentException.class,
                () -> useCase.executar(original.id(), new PatchPessoa(true, null, false, null, false, null)));
        assertThrows(IllegalArgumentException.class,
                () -> useCase.executar(original.id(), new PatchPessoa(false, null, false, null, false, null)));
    }

    @Test
    void missingPersonRaisesNotFoundForReadAndDelete() {
        InMemoryPessoaRepository repository = new InMemoryPessoaRepository();

        assertThrows(PessoaNaoEncontradaException.class, () -> new BuscarPessoaPorIdUseCase(repository).executar(7L));
        assertThrows(PessoaNaoEncontradaException.class, () -> new ExcluirPessoaUseCase(repository).executar(7L));
    }

    @Test
    void listsPeopleInAscendingIdOrder() {
        InMemoryPessoaRepository repository = new InMemoryPessoaRepository();
        repository.save(new Pessoa(2L, "Bia", "2", "Rua B"));
        repository.save(new Pessoa(1L, "Ana", "1", "Rua A"));

        List<Long> ids = new ListarPessoasUseCase(repository).executar().stream().map(Pessoa::id).toList();

        assertEquals(List.of(1L, 2L), ids);
    }

    private static class InMemoryPessoaRepository implements PessoaRepository {
        private final Map<Long, Pessoa> pessoas = new HashMap<>();
        private long nextId = 1;

        @Override
        public Pessoa save(Pessoa pessoa) {
            Long id = pessoa.id() == null ? nextId++ : pessoa.id();
            Pessoa salva = new Pessoa(id, pessoa.nome(), pessoa.cpf(), pessoa.endereço());
            pessoas.put(id, salva);
            return salva;
        }

        @Override
        public Optional<Pessoa> findById(Long id) {
            return Optional.ofNullable(pessoas.get(id));
        }

        @Override
        public Optional<Pessoa> findByCpf(String cpf) {
            return pessoas.values().stream().filter(pessoa -> pessoa.cpf().equals(cpf)).findFirst();
        }

        @Override
        public List<Pessoa> findAll() {
            return new ArrayList<>(pessoas.values());
        }

        @Override
        public void deleteById(Long id) {
            pessoas.remove(id);
        }
    }
}