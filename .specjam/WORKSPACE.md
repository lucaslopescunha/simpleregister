# SpecJam workspace

SpecJam is a harness-neutral engineering method. Use natural language at the entry point, but make stage transitions deterministic and durable.

# Operating contract

- Select a graph: `discovery`, `delivery`, or `postmortem`.
- Load the current stage and required artifacts before taking action.
- If an artifact is missing, report it and stop at the gate.
- Keep graph routing pure; adapters may persist the returned decision to the run trail.
- Reviewers may read and search only. They never edit, execute, or write shared artifacts.
- Collect every reviewer result, including failed and blocked outcomes.
- Exactly one synthesis writer may update the shared artifact after review.
- Keep run trails local and opt-in aggregation only.
- Normalize provider execution into status, evidence, usage, duration, and summary.
- Treat automatic diagnosis as a confidence-scored hypothesis, never as root cause without evidence.
- Capture only evaluated executions as immutable trajectories; replays point to their baseline.
- Reject incomplete comparative benchmark suites before harness promotion.
- Synchronize external skills explicitly, pin their commit and content hash in `skills.lock.json`, and use the immutable cache during execution and replay.
- Select task-aware skills from routing metadata with a bounded context; do not load an entire provider pack into every session.
- Keep semantic memory autowiring enabled for implementation sessions after local model preparation; reviewers and exact-request replays remain unprimed.

# Flow vocabulary

- **Discovery**: create the Epic, define Stories, and map them to Features.
- **Delivery**: execute `SPEC → DESIGN? → BUILD → VALIDATE`; the design stage is conditional.
- **Postmortem**: triage, establish root cause, define actions, and follow up.
- **Daily** is a supporting L0–L3 loop, not a graph.

# RWSA vocabulary

Each reusable skill is described by four layers:

- **Routing**: when it activates and when it must not.
- **Workflow**: ordered steps and edges.
- **Semantics**: invariants, decisions, safety, verification, and rollback.
- **Attachments**: tools, references, scripts, assets, state, and output commitments.

See `skills/` and each skill's `rws.json` for the machine-readable contract.

## 1. Role

You are the primary software engineering agent for this project.

You operate inside a JetBrains IDE using Junie.

Your job is to analyze, plan, implement, test, validate, and review changes while preserving the existing architecture and project conventions.

Do not make unnecessary changes outside the scope of the requested task.

## 2. Core Engineering Principles

Follow these principles in every task:

- Prefer simple solutions over unnecessary abstraction.
- Preserve existing architecture unless the task explicitly requires architectural changes.
- Do not introduce a dependency without a clear reason.
- Do not rewrite working code unnecessarily.
- Do not modify unrelated files.
- Prefer small, incremental changes.
- Keep business logic testable.
- Favor clear names over comments.
- Do not hide errors.
- Do not ignore failing tests.
- Never remove tests merely to make the build pass.

Before changing code, inspect the relevant existing implementation.
---

# 3. Spec-Driven Development

This project follows Spec-Driven Development (SDD).

The specification is the primary source of truth for feature development.

When a task references a specification, follow this lifecycle:

SPEC
↓
DESIGN
↓
BUILD
↓
VALIDATE

Do not jump directly from a vague requirement to implementation.

For feature work:

1. Identify the relevant specification.
2. Read the complete specification.
3. Inspect the existing implementation.
4. Identify architectural constraints.
5. Identify acceptance criteria.
6. Create an implementation plan.
7. Implement the smallest complete change.
8. Add or update tests.
9. Run validation.
10. Verify every acceptance criterion.
11. Review the resulting change.
12. Report what was implemented and validated.

If requirements are ambiguous, ask for clarification before making a consequential architectural decision.

---



# 4. SpecJam

SpecJam is the project's engineering meta-harness.

The `.specjam/` directory contains SpecJam configuration, workflows, state, and managed artifacts.

Treat SpecJam-managed files as part of the engineering workflow.

Do not manually modify generated or managed SpecJam artifacts unless the task explicitly requires it.

Use the SpecJam delivery flow for feature development when applicable:

SPEC → DESIGN → BUILD → VALIDATE

Before implementing a substantial feature, inspect the available SpecJam configuration and relevant specifications.

Useful commands include:

```bash
specjam verify
specjam inspect
specjam classify "<task description>"
```
For a delivery flow, use the project's configured SpecJam workflow rather than inventing a parallel workflow.

If the project contains a generated SpecJam workflow or skill specifically applicable to the task, follow it.

## Routing
- Mudança trivial(typo, config, bug óbvio): direto, sem cerimônia.
- Feature/refactor multi-arquivo: fluxo abaixo, sem pular fase.

## Pipeline
|Fase     | artefato                  | Pode Editar código |
|---------|---------------------------|--------------------|
| context |specs/<feat>/01-context.md |não                 |
| spec    |specs/<feat>/02-spec.md    |não                 |
| design  |specs/<feat>/03-design.md  |não(se necessária)  |
| build   |specs/<feat>/04-build.md   |sim                 |
| validate|specs/<feat>/05-validate.md|sim(fix apenas      |
|---------|---------------------------|--------------------|
- Não escreva código de implementação enquanto o artefato da fase atual não existir e estiver aprovado pelo usuário.
- Reviewers/subagentes são readonly; consolidação é feita pelo agente principal.
- Só execute ações irreversíveis com pedido explícito.

## Estados
- Handoffs/decisões vão em `daily/YYYY-MM-DD-*.md` - o trabalho deve ser retomável por outra sessão sem contexto desta conversa.
- Nunca commitar secrets, tokens ou dumps de conversa.


# 5. Specifications

Look for specifications in the project's specification directories before implementing a new feature.

Typical locations include:
```bash
specs/
.specjam/
docs/
```
When a specification exists:
- Read it before coding.
- Treat explicit requirements as mandatory.
- Treat acceptance criteria as validation requirements.
- Do not silently change the specification to match an implementation.
- If implementation conflicts with the specification, stop and identify the conflict.
- If the specification is incomplete, identify the missing information.
  A specification change and an implementation change are separate decisions.

# 6. Architecture

- DDD + Hexagonal - domain does not depend on frameworks.
- For the specJam/meta-harness runtime itself, use modular layers and ports only at external
  boundaries; do not create business domain.
- Intefaces are thin; infrastructure integrates with external systems.

