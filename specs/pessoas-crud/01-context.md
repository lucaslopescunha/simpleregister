# Contexto: CRUD de Pessoas

## Objetivo

Definir o contexto para desenvolver operações de criação, consulta, alteração parcial, listagem e exclusão de pessoas na aplicação `registro-usuarios`.

## Contexto do produto

A aplicação tem como foco oferecer um CRUD simples de pessoas. Cada pessoa contém somente os dados `nome`, `cpf` e `endereço`; o endereço é armazenado como uma única string. O CPF não deve receber validação neste escopo.

## Escopo confirmado

O recurso deve oferecer cinco operações:

- Criar uma pessoa por `POST`.
- Alterar parcialmente uma pessoa por `PATCH`.
- Excluir uma pessoa por ID.
- Pesquisar uma pessoa por ID.
- Listar todas as pessoas.

## Restrições conhecidas

- O banco de dados do projeto deve ser H2.
- O projeto exige Java 21 e Spring Boot 4.0.0 ou posterior.
- A arquitetura deve respeitar a direção de dependências `infrastructure → application → domain`; `domain` e `application` não devem conter annotations.
- A implementação deve seguir as especificações aprovadas antes de começar a codificação.

## Fora do escopo conhecido

- Validar o formato ou a validade do CPF.
- Dividir o endereço em múltiplos campos.

## Questões para a etapa de especificação

- Quais são os contratos HTTP exatos, incluindo caminhos, formatos de requisição e resposta e códigos de status?
- Quais campos podem ser enviados no `PATCH`, e como tratar campos omitidos, nulos ou vazios?
- Quais regras se aplicam a `nome` e `endereço`, já que nenhuma validação foi definida?
- Como a API deve responder quando um ID não existir?
- Qual formato e ordenação devem ser usados ao listar pessoas?