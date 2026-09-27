package br.com.cunha.registrousuarios.infrastructure;

import br.com.cunha.registrousuarios.application.CpfDuplicadoException;
import br.com.cunha.registrousuarios.application.PessoaNaoEncontradaException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(PessoaNaoEncontradaException.class)
    public ResponseEntity<ErroResponse> nãoEncontrada(PessoaNaoEncontradaException exception) {
        return erro(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler({CpfDuplicadoException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ErroResponse> conflito(RuntimeException exception) {
        return erro(HttpStatus.CONFLICT, "CPF já cadastrado");
    }

    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErroResponse> requisiçãoInválida(Exception exception) {
        return erro(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    private static ResponseEntity<ErroResponse> erro(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ErroResponse(status.value(), status.getReasonPhrase(), message));
    }

    public record ErroResponse(int status, String error, String message) {
    }
}