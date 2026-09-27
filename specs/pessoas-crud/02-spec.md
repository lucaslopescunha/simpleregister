# Especificação: CRUD de Pessoas

**Branch da feature**: Não definida  
**Criado em**: 2026-09-26  
**Status**: Aprovado para implementação  
**Entrada**: Contexto em `specs/pessoas-crud/01-context.md`: CRUD de pessoas com nome, CPF sem validação e endereço em campo único.

## Cenários do usuário e testes

### História 1 — Cadastrar pessoa (Prioridade: P1)

Como consumidor da API, quero cadastrar uma pessoa para que seus dados fiquem disponíveis para consulta.

**Motivo da prioridade**: O cadastro cria os registros necessários para as demais operações.

**Teste independente**: Enviar uma requisição `POST` com os dados de uma pessoa e verificar que o cadastro é confirmado e pode ser consultado depois.

**Cenários de aceitação**:

1. **Dado** que recebo os campos `nome`, `cpf` e `endereço`, **quando** envio uma requisição de criação, **então** o sistema cadastra a pessoa e confirma a operação.
2. **Dado** que o CPF não deve ser validado neste escopo, **quando** envio um CPF em qualquer formato, **então** o sistema não rejeita o cadastro por causa do formato ou validade do CPF.

---

### História 2 — Consultar pessoa por ID (Prioridade: P1)

Como consumidor da API, quero consultar uma pessoa por seu ID para recuperar seus dados individualmente.

**Motivo da prioridade**: Permite confirmar e recuperar um cadastro específico.

**Teste independente**: Consultar por ID uma pessoa previamente cadastrada e verificar os dados retornados.

**Cenários de aceitação**:

1. **Dado** que existe uma pessoa cadastrada, **quando** consulto seu ID, **então** o sistema retorna essa pessoa com `nome`, `cpf` e `endereço`.
2. **Dado** um ID sem pessoa correspondente, **quando** tento consultar esse ID, **então** o sistema retorna `404 Not Found`.

---

### História 3 — Listar pessoas (Prioridade: P1)

Como consumidor da API, quero listar as pessoas cadastradas para consultar a coleção de registros.

**Motivo da prioridade**: Oferece acesso à coleção completa, conforme o escopo confirmado.

**Teste independente**: Cadastrar pessoas e solicitar a listagem, verificando que todas aparecem com os campos definidos.

**Cenários de aceitação**:

1. **Dado** que existem pessoas cadastradas, **quando** solicito a listagem, **então** o sistema retorna todas elas com `nome`, `cpf` e `endereço`.
2. **Dado** que não existem pessoas cadastradas, **quando** solicito a listagem, **então** o sistema retorna uma coleção vazia.

---

### História 4 — Alterar parcialmente pessoa (Prioridade: P2)

Como consumidor da API, quero alterar parcialmente uma pessoa existente para atualizar os dados necessários sem reenviar todos os campos.

**Motivo da prioridade**: Completa as operações de manutenção de um cadastro existente.

**Teste independente**: Alterar um campo de uma pessoa existente e verificar que o campo foi atualizado e os demais foram preservados.

**Cenários de aceitação**:

1. **Dado** que existe uma pessoa cadastrada, **quando** envio uma requisição `PATCH` com um ou mais campos aceitos, **então** o sistema atualiza esses campos.
2. **Dado** que um campo não foi enviado no `PATCH`, **quando** a alteração é processada, **então** o valor existente desse campo permanece inalterado.
3. **Dado** um ID sem pessoa correspondente, **quando** tento alterá-lo, **então** o sistema retorna `404 Not Found`.
4. **Dado** um PATCH com pelo menos um campo válido, **quando** a atualização é concluída, **então** o sistema retorna `200 OK` com a representação atualizada.
5. **Dado** um PATCH sem campos, **quando** tento atualizar uma pessoa, **então** o sistema retorna `400 Bad Request`.

---

### História 5 — Excluir pessoa por ID (Prioridade: P2)

Como consumidor da API, quero excluir uma pessoa por ID para remover um cadastro específico.

**Motivo da prioridade**: Permite remover um registro sem depender de atributos mutáveis ou não exclusivos, como o nome.

**Teste independente**: Excluir por ID uma pessoa cadastrada e confirmar que ela deixa de ser retornada pela consulta e listagem.

**Cenários de aceitação**:

1. **Dado** que existe uma pessoa cadastrada, **quando** solicito sua exclusão pelo ID, **então** o sistema remove essa pessoa.
2. **Dado** um ID sem pessoa correspondente, **quando** solicito sua exclusão, **então** o sistema retorna `404 Not Found`.
3. **Dado** uma pessoa existente, **quando** a excluo, **então** o sistema retorna `204 No Content` sem corpo.

## Casos de borda

- O CPF não deve ser rejeitado por formato ou validade.
- Uma listagem sem registros deve resultar em coleção vazia.
- Consulta, alteração ou exclusão com ID inexistente retornam `404 Not Found`.
- Na criação, `nome`, `cpf` e `endereço` são obrigatórios; `nome` e `endereço` não podem ser vazios ou conter somente espaços em branco.
- No PATCH, campos omitidos permanecem inalterados; campos explicitamente nulos, nomes desconhecidos, valores inválidos ou objeto vazio são rejeitados com `400 Bad Request`.
- CPF duplicado é rejeitado com `409 Conflict`; CPF é armazenado como enviado, sem validar formato ou validade.
- Erros de requisição retornam JSON com `status`, `error` e `message`; respostas de erro de validação usam `400`, enquanto CPF duplicado usa `409`.

## Requisitos

### Requisitos funcionais

- **FR-001**: O sistema DEVE permitir criar uma pessoa por `POST`.
- **FR-002**: O sistema DEVE permitir alterar parcialmente uma pessoa por `PATCH`.
- **FR-003**: O sistema DEVE permitir excluir uma pessoa identificada por ID.
- **FR-004**: O sistema DEVE permitir consultar uma pessoa identificada por ID.
- **FR-005**: O sistema DEVE permitir listar todas as pessoas cadastradas.
- **FR-006**: Cada pessoa DEVE conter somente os dados de negócio `nome`, `cpf` e `endereço`, sendo `endereço` uma string única.
- **FR-007**: O sistema NÃO DEVE validar o formato ou a validade do CPF.
- **FR-008**: O `PATCH` DEVE preservar os valores dos campos omitidos da requisição.
- **FR-009**: O sistema DEVE persistir os registros de pessoas usando H2, conforme a constituição do projeto.
- **FR-010**: Os caminhos HTTP, os esquemas completos de requisição e resposta e os códigos de status DEVEM ser definidos antes da implementação.
- **FR-011**: A API DEVE expor `POST /pessoas`, `GET /pessoas/{id}`, `GET /pessoas`, `PATCH /pessoas/{id}` e `DELETE /pessoas/{id}`; o ID DEVE ser `Long` sequencial.
- **FR-012**: A criação DEVE exigir `nome`, `cpf` e `endereço`, rejeitar `nome` ou `endereço` nulos, vazios ou em branco, e retornar `201 Created` com a representação criada, incluindo `id`.
- **FR-013**: A resposta da pessoa DEVE conter `id`, `nome`, `cpf` e `endereço`; a listagem DEVE retornar uma coleção JSON, inclusive vazia, em ordem crescente de ID.
- **FR-014**: PATCH DEVE permitir somente `nome`, `cpf` e `endereço`; campos omitidos permanecem inalterados, valores explicitamente nulos e corpos sem campos são inválidos; sucesso retorna `200 OK` com a representação atualizada.
- **FR-015**: DELETE bem-sucedido DEVE retornar `204 No Content` sem corpo. Busca, alteração e exclusão para ID inexistente DEVEM retornar `404 Not Found`.
- **FR-016**: Requisições inválidas DEVEM retornar `400 Bad Request` com JSON contendo `status`, `error` e `message`. CPF duplicado DEVE retornar `409 Conflict` com o mesmo formato; nenhuma validação de formato/validade do CPF DEVE ser aplicada.

### Entidades principais

- **Pessoa**: registro identificado por um ID `Long` sequencial, contendo `nome`, `cpf` e `endereço` como string única.

## Critérios de sucesso

- **SC-001**: As cinco operações acordadas (criação, alteração parcial, exclusão por ID, consulta por ID e listagem) têm critérios de aceitação verificáveis antes da implementação.
- **SC-002**: Em testes de aceitação, as cinco operações produzem os resultados especificados para registros existentes e retornam `404` para IDs inexistentes em busca, atualização e exclusão.
- **SC-003**: O CPF é aceito sem validação de formato ou validade.
- **SC-004**: Uma alteração parcial modifica apenas os campos enviados e aceitos, preservando os campos omitidos.

## Contrato HTTP

- `POST /pessoas`: recebe `{"nome":"...","cpf":"...","endereço":"..."}`; sucesso `201 Created` e JSON `{"id":1,"nome":"...","cpf":"...","endereço":"..."}`. Campos ausentes/nulos, ou `nome`/`endereço` vazios ou em branco, retornam `400 Bad Request`.
- `GET /pessoas/{id}`: sucesso `200 OK` com a representação da pessoa; inexistente `404 Not Found`.
- `GET /pessoas`: sucesso `200 OK` com array JSON de representações em ordem crescente de ID; sem registros retorna `[]`.
- `PATCH /pessoas/{id}`: aceita um objeto JSON não vazio contendo somente os campos de negócio; omissões preservam valores, valores `null`, campos desconhecidos, valores inválidos e objeto vazio retornam `400 Bad Request`; sucesso `200 OK` com a representação atualizada; ID inexistente `404 Not Found`.
- `DELETE /pessoas/{id}`: sucesso `204 No Content` sem corpo; ID inexistente `404 Not Found`.
- Erros em JSON incluem `status` (número HTTP), `error` (razão HTTP) e `message` (descrição legível). CPF duplicado retorna `409 Conflict`; não há validação de formato de CPF.

## Premissas e limites

- O recurso é uma API para operações de pessoas; autenticação, autorização, paginação e limites de volume não foram definidos neste escopo.
- O único campo de endereço é uma string, não decomposta em partes.
- `nome` e `endereço` são obrigatórios na criação e não podem ser vazios ou em branco; no PATCH, valores enviados para esses campos seguem a mesma regra.
- A implementação deve respeitar Java 21, Spring Boot 4.0.0 ou posterior, H2, ausência de annotations em `domain` e `application`, e o fluxo de dependências `infrastructure → application → domain`.
- Os contratos deste arquivo refletem as decisões aprovadas e os detalhes HTTP completados pelo plano de implementação.