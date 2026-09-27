package br.com.cunha.registrousuarios.infrastructure;

import br.com.cunha.registrousuarios.infrastructure.persistence.SpringDataPessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.datasource.url=jdbc:h2:mem:pessoas-test;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.h2.console.enabled=false"
})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PessoaApiIntegrationTests {

    private final HttpClient client = HttpClient.newHttpClient();

    @LocalServerPort
    private int port;

    private final SpringDataPessoaRepository repository;

    PessoaApiIntegrationTests(SpringDataPessoaRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    @Test
    void supportsCreateReadPatchListAndDelete() throws IOException, InterruptedException {
        assertEquals("[]", enviar("GET", "/pessoas", null).body());
        HttpResponse<String> criada = enviar("POST", "/pessoas",
                "{\"nome\":\"Ana\",\"cpf\":\"não-validado\",\"endereço\":\"Rua A\"}");
        assertEquals(201, criada.statusCode());
        String id = idDa(criada.body());

        HttpResponse<String> segunda = enviar("POST", "/pessoas",
                "{\"nome\":\"Caio\",\"cpf\":\"cpf-2\",\"endereço\":\"Rua B\"}");
        assertEquals(201, segunda.statusCode());
        String segundoId = idDa(segunda.body());

        HttpResponse<String> patch = enviar("PATCH", "/pessoas/" + id, "{\"nome\":\"Bia\"}");
        assertEquals(200, patch.statusCode());
        assertTrue(patch.body().contains("\"nome\":\"Bia\""));
        assertTrue(patch.body().contains("\"cpf\":\"não-validado\""));

        assertEquals(200, enviar("GET", "/pessoas/" + id, null).statusCode());
        HttpResponse<String> listagem = enviar("GET", "/pessoas", null);
        assertEquals(200, listagem.statusCode());
        assertTrue(listagem.body().startsWith("["));
        assertTrue(listagem.body().indexOf("\"id\":" + id) < listagem.body().indexOf("\"id\":" + segundoId));

        assertEquals(204, enviar("DELETE", "/pessoas/" + id, null).statusCode());
        assertEquals(404, enviar("GET", "/pessoas/" + id, null).statusCode());
    }

    @Test
    void reportsInvalidDuplicateAndMissingRequests() throws IOException, InterruptedException {
        String body = "{\"nome\":\"Ana\",\"cpf\":\"cpf\",\"endereço\":\"Rua A\"}";
        HttpResponse<String> criada = enviar("POST", "/pessoas", body);
        assertEquals(201, criada.statusCode());
        String id = idDa(criada.body());
        HttpResponse<String> duplicada = enviar("POST", "/pessoas",
                "{\"nome\":\"Bia\",\"cpf\":\"cpf\",\"endereço\":\"Rua B\"}");
        assertEquals(409, duplicada.statusCode());
        assertTrue(duplicada.body().contains("\"status\":409"));

        HttpResponse<String> inválida = enviar("POST", "/pessoas",
                "{\"nome\":\"   \",\"cpf\":\"x\",\"endereço\":\"Rua\"}");
        assertEquals(400, inválida.statusCode());
        assertEquals(400, enviar("PATCH", "/pessoas/" + id, "{}").statusCode());
        assertEquals(404, enviar("PATCH", "/pessoas/99", "{\"nome\":\"Bia\"}").statusCode());
        assertEquals(404, enviar("DELETE", "/pessoas/99", null).statusCode());
    }

    private HttpResponse<String> enviar(String method, String path, String body)
            throws IOException, InterruptedException {
        HttpRequest.Builder request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path));
        if (body == null) {
            request.method(method, HttpRequest.BodyPublishers.noBody());
        } else {
            request.header("Content-Type", "application/json")
                    .method(method, HttpRequest.BodyPublishers.ofString(body));
        }
        return client.send(request.build(), HttpResponse.BodyHandlers.ofString());
    }

    private static String idDa(String responseBody) {
        Matcher matcher = Pattern.compile("\\\"id\\\":(\\d+)").matcher(responseBody);
        if (!matcher.find()) {
            throw new AssertionError("Response did not contain a person ID: " + responseBody);
        }
        return matcher.group(1);
    }
}