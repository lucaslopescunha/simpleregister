package br.com.cunha.registrousuarios.infrastructure;

import br.com.cunha.registrousuarios.application.AtualizarPessoaUseCase;
import br.com.cunha.registrousuarios.application.BuscarPessoaPorIdUseCase;
import br.com.cunha.registrousuarios.application.CriarPessoaUseCase;
import br.com.cunha.registrousuarios.application.ExcluirPessoaUseCase;
import br.com.cunha.registrousuarios.application.ListarPessoasUseCase;
import br.com.cunha.registrousuarios.application.PatchPessoa;
import br.com.cunha.registrousuarios.domain.Pessoa;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    private static final Set<String> CAMPOS = Set.of("nome", "cpf", "endereço");

    private final CriarPessoaUseCase criar;
    private final AtualizarPessoaUseCase atualizar;
    private final ExcluirPessoaUseCase excluir;
    private final BuscarPessoaPorIdUseCase buscar;
    private final ListarPessoasUseCase listar;

    public PessoaController(CriarPessoaUseCase criar, AtualizarPessoaUseCase atualizar,
                            ExcluirPessoaUseCase excluir, BuscarPessoaPorIdUseCase buscar,
                            ListarPessoasUseCase listar) {
        this.criar = criar;
        this.atualizar = atualizar;
        this.excluir = excluir;
        this.buscar = buscar;
        this.listar = listar;
    }

    @PostMapping
    public ResponseEntity<PessoaResponse> criar(@RequestBody Map<String, Object> body) {
        validarCampos(body, CAMPOS);
        if (!body.keySet().equals(CAMPOS)) {
            throw new IllegalArgumentException("nome, cpf e endereço são obrigatórios");
        }
        Pessoa pessoa = criar.executar(campo(body, "nome"), campo(body, "cpf"), campo(body, "endereço"));
        return ResponseEntity.status(HttpStatus.CREATED).body(PessoaResponse.from(pessoa));
    }

    @GetMapping("/{id}")
    public PessoaResponse buscar(@PathVariable Long id) {
        return PessoaResponse.from(buscar.executar(id));
    }

    @GetMapping
    public List<PessoaResponse> listar() {
        return listar.executar().stream().map(PessoaResponse::from).toList();
    }

    @PatchMapping("/{id}")
    public PessoaResponse atualizar(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        validarCampos(body, CAMPOS);
        if (body.isEmpty()) {
            throw new IllegalArgumentException("PATCH deve conter pelo menos um campo");
        }
        PatchPessoa patch = new PatchPessoa(body.containsKey("nome"), campo(body, "nome", false),
                body.containsKey("cpf"), campo(body, "cpf", false),
                body.containsKey("endereço"), campo(body, "endereço", false));
        return PessoaResponse.from(atualizar.executar(id, patch));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        excluir.executar(id);
        return ResponseEntity.noContent().build();
    }

    private static void validarCampos(Map<String, Object> body, Set<String> permitidos) {
        if (body == null || !permitidos.containsAll(body.keySet())) {
            throw new IllegalArgumentException("Corpo da requisição contém campos inválidos");
        }
    }

    private static String campo(Map<String, Object> body, String nome) {
        return campo(body, nome, true);
    }

    private static String campo(Map<String, Object> body, String nome, boolean obrigatório) {
        if (!body.containsKey(nome)) {
            if (obrigatório) {
                throw new IllegalArgumentException(nome + " é obrigatório");
            }
            return null;
        }
        Object valor = body.get(nome);
        if (!(valor instanceof String texto)) {
            throw new IllegalArgumentException(nome + " deve ser uma string não nula");
        }
        return texto;
    }

    public record PessoaResponse(Long id, String nome, String cpf, String endereço) {
        private static PessoaResponse from(Pessoa pessoa) {
            return new PessoaResponse(pessoa.id(), pessoa.nome(), pessoa.cpf(), pessoa.endereço());
        }
    }
}