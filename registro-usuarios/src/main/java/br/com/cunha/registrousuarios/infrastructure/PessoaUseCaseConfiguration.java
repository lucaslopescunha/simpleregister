package br.com.cunha.registrousuarios.infrastructure;

import br.com.cunha.registrousuarios.application.AtualizarPessoaUseCase;
import br.com.cunha.registrousuarios.application.BuscarPessoaPorIdUseCase;
import br.com.cunha.registrousuarios.application.CriarPessoaUseCase;
import br.com.cunha.registrousuarios.application.ExcluirPessoaUseCase;
import br.com.cunha.registrousuarios.application.ListarPessoasUseCase;
import br.com.cunha.registrousuarios.application.PessoaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PessoaUseCaseConfiguration {

    @Bean
    public CriarPessoaUseCase criarPessoaUseCase(PessoaRepository repository) {
        return new CriarPessoaUseCase(repository);
    }

    @Bean
    public AtualizarPessoaUseCase atualizarPessoaUseCase(PessoaRepository repository) {
        return new AtualizarPessoaUseCase(repository);
    }

    @Bean
    public ExcluirPessoaUseCase excluirPessoaUseCase(PessoaRepository repository) {
        return new ExcluirPessoaUseCase(repository);
    }

    @Bean
    public BuscarPessoaPorIdUseCase buscarPessoaPorIdUseCase(PessoaRepository repository) {
        return new BuscarPessoaPorIdUseCase(repository);
    }

    @Bean
    public ListarPessoasUseCase listarPessoasUseCase(PessoaRepository repository) {
        return new ListarPessoasUseCase(repository);
    }
}