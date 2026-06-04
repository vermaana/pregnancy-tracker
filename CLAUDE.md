# CLAUDE.md — Pregnancy Tracker

Read this at the start of every session.

---

## Project Type

Pure Jetpack Compose Android app — no XML layouts, no View-based UI. Any UI work must use Compose exclusively.

---

## Branching

- Every new feature or fix goes on its own branch, branched off `main`.
- Branch naming: `feature/<short-description>` or `fix/<short-description>`.
- Never commit directly to `main`.

---

## Architecture

Follow MVVM strictly:

- **Model** — data classes, repositories, data sources (Room, network, etc.)
- **ViewModel** — holds UI state as `StateFlow` or `State`; contains all business logic; no Android framework imports except `ViewModel` and `Application` if strictly necessary
- **View (Composables)** — stateless where possible; receive state and lambdas; no logic beyond UI rendering

Layer rules:
- ViewModels must not reference Composables.
- Composables must not instantiate repositories or perform IO directly.
- Repositories are the single source of truth; they abstract all data sources.

---

## Clean Code

- One responsibility per class and per function.
- Functions longer than ~20 lines are a signal to extract.
- No magic numbers or strings — use named constants or string resources.
- Meaningful names: no abbreviations, no single-letter variables outside loops.
- No dead code, no commented-out code.

---

## Android Best Practices

- Use `hiltViewModel()` for ViewModel injection (Hilt is the DI framework).
- Observe state in Composables via `collectAsStateWithLifecycle()`.
- Side effects (navigation, snackbars) via `LaunchedEffect` / `rememberCoroutineScope` — not in ViewModels directly.
- Use `rememberSaveable` for UI state that must survive process death.
- Respect the unidirectional data flow: events go up, state comes down.
- Target the latest stable compileSdk and targetSdk.
- Handle edge cases: loading, error, empty states for every screen.

---

## Testing

### Unit Tests
- Every piece of business logic (ViewModel, repository, use case, utility) must have a corresponding unit test.
- Use JUnit 4, `kotlinx-coroutines-test`, and MockK for mocking.
- Test files mirror the source tree under `app/src/test/`.
- Aim for one test class per production class.

### Snapshot Tests
- Every Composable screen and reusable component must have a dedicated snapshot test using [Paparazzi](https://github.com/cashapp/paparazzi).
- Snapshot tests live under `app/src/test/` alongside unit tests.
- Cover at minimum: default state, loading state, error state, and any significant visual variant.
- Run `./gradlew recordPaparazziDebug` to record; `./gradlew verifyPaparazziDebug` to verify.
- Commit recorded snapshots with the feature branch.

---

## File & Package Structure

```
com.anni.pregnancytracker
├── data
│   ├── local          # Room DAOs, entities, database
│   ├── remote         # API services, DTOs
│   └── repository     # Repository implementations
├── domain
│   ├── model          # Domain models
│   └── usecase        # Use cases (optional, for complex logic)
├── ui
│   ├── <feature>
│   │   ├── <Feature>Screen.kt
│   │   ├── <Feature>ViewModel.kt
│   │   └── components/
│   └── theme          # Color, Type, Theme
└── di                 # Hilt modules
```
