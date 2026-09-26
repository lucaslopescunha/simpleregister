# SpecJam workspace

SpecJam is a harness-neutral engineering method. Use natural language at the entry point, but make stage transitions deterministic and durable.

## Operating contract

- Select a graph: `discovery`, `delivery`, or `postmortem`.
- Load the current stage and required artifacts before taking action.
- If an artifact is missing, report it and stop at the gate.
- Keep graph routing pure; adapters may persist the returned decision to the run trail.
- The graph coordinator owns state and transitions; record transitions in the append-only run trail.
- Start a new implementation session for each increment.
- Reviewer sessions are isolated and read-only.
- Reviewers return findings and evidence; one synthesis step is the only writer.
- Preserve failed or blocked results instead of silently advancing the graph.
- Keep run trails local and opt-in aggregation only.
- Normalize provider execution into status, evidence, usage, duration, and summary.
- Treat automatic diagnosis as a confidence-scored hypothesis, never as root cause without evidence.
- Capture only evaluated executions as immutable trajectories; replays point to their baseline.
- Reject incomplete comparative benchmark suites before harness promotion.
- Synchronize external skills explicitly, pin their commit and content hash in `skills.lock.json`, and use the immutable cache during execution and replay.
- Select task-aware skills from routing metadata with a bounded context; do not load an entire provider pack into every session.
- Keep semantic memory autowiring enabled for implementation sessions after local model preparation; reviewers and exact-request replays remain unprimed.

## Flow vocabulary

- **Discovery**: turn an initiative into mapped stories and delivery increments (create Epic, define Stories, map to Features).
- **Delivery**: execute `CONTEXT → SPEC → DESIGN? → BUILD → VALIDATE`; the design stage is conditional.
- **Postmortem**: follow evidence, root-cause, actions, synthesis, and follow-up.
- **Daily** is a supporting L0–L3 loop, not a graph.

## Shared system of record

- Tracker integrations are adapters; the core does not require a specific vendor.
- Epic, Story, and Feature describe flow roles, not vendor-specific issue types.
- Handoffs must be understandable without another developer's local workspace.
- Never write credentials, absolute local paths, or private operational data into shared descriptions, generated artifacts, or run trails.

## Skills and harnesses

- Skills are referenced as `provider/name@version` and resolved with provenance.
- Harness adapters translate neutral session requests into vendor-specific calls.
- Vendor SDKs, credentials, and organization-specific domain packs belong in external adapters or the consuming workspace, never in SpecJam core.
- A workspace may configure Devin, Codex, Claude Code, or a local runner without changing the graph engine.

## RWSA vocabulary

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
- Keep domain policy independent from frameworks and external systems.
- Treat API and event schemas as explicit contracts.
- Add resilience, security, and observability when an integration requires them.
- Prefer deterministic validation scripts for rules that must not be subjective.
- Keep generated state, caches, credentials, and machine-specific configuration outside the package and committed source.
- Favor clear names over comments.
- Do not hide errors.
- Do not ignore failing tests.
- Never remove tests merely to make the build pass.
- Before changing code, inspect the relevant existing implementation.

## 3. Spec-Driven Development

This project follows Spec-Driven Development (SDD).
The specification is the primary source of truth for feature development.

When a task references a specification, follow this lifecycle:

`CONTEXT → SPEC → DESIGN? → BUILD → VALIDATE`

Do not jump directly from a vague requirement to implementation.

For feature work:
1. Identify the relevant specification and context.
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

## 4. SpecJam

SpecJam is the project's engineering meta-harness.
The `.specjam/` directory contains SpecJam configuration, workflows, state, and managed artifacts.
Treat SpecJam-managed files as part of the engineering workflow.
Do not manually modify generated or managed SpecJam artifacts unless the task explicitly requires it.

Useful commands include:
```bash
specjam verify
specjam inspect
specjam classify "<task description>"
```

### Routing
- **Trivial change** (typo, config, obvious bug): direct execution without stage ceremony.
- **Feature / multi-file refactor**: follow the pipeline below without skipping phases.

### Pipeline
| Phase    | Artifact                    | Code Editing Allowed  |
|----------|-----------------------------|-----------------------|
| context  | `specs/<feat>/01-context.md` | No                    |
| spec     | `specs/<feat>/02-spec.md`    | No                    |
| design   | `specs/<feat>/03-design.md`  | No (if required)      |
| build    | `specs/<feat>/04-build.md`   | Yes                   |
| validate | `specs/<feat>/05-validate.md`| Yes (fixes only)      |

- Do not write implementation code until the active phase artifact exists and is verified.
- Reviewers and subagents are read-only; consolidation is performed by the primary agent.
- Only execute irreversible actions upon explicit request.

### State & Handoffs
- Handoffs and decisions are logged in `daily/YYYY-MM-DD-*.md` so work can be resumed in another session without conversational context.
- Never commit credentials, tokens, or conversation dumps.

## 5. Specifications

Look for specifications in the project's specification directories before implementing a new feature:
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
- If the specification is incomplete, identify the missing information. A specification change and an implementation change are separate decisions.

## 6. Architecture

- **Domain-Driven & Hexagonal**: Domain logic does not depend on frameworks.
- **Runtime Boundaries**: For the meta-harness runtime itself, use modular layers and ports only at external boundaries; do not introduce unnecessary business domain abstractions.
- **Thin Interfaces**: Interfaces are thin; infrastructure integrates with external systems.

## 7. RTK (Rust Token Killer)

- RTK is installed in this environment (`/home/lucas/.local/bin/rtk`) to compress CLI output and conserve LLM context tokens.
- Junie does not currently have a native command-rewrite hook, so explicitly use `rtk` subcommands whenever available.

### Preferred Commands
```bash
rtk git status
rtk git diff
rtk git log
rtk grep "<pattern>"
rtk find "<pattern>"
rtk test              # Only emits failures/errors
rtk read <file>       # Reads files with intelligent token filtering
rtk gain              # Check token savings
```

### Execution Rule
- Prepend `rtk` to compatible shell commands to minimize output bloat; fall back to raw commands only when no RTK subcommand exists or uncompressed output is explicitly required.

## 8. Verification & Packaging

- Run the complete test suite before changing a graph, public contract, installer behavior, or packaged payload:
```bash
PYTHONPATH=src python -m unittest discover -s tests -v
```
- Build distributions from a clean checkout and inspect their contents before a release. A public package must not contain generated workspaces, run trails, credentials, internal hostnames, or organization-specific configuration.
